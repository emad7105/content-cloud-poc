import http.client
from urllib.parse import urlparse
import urllib
from pyquery import PyQuery as pq


def get_cookies(headers):
    header = headers['Set-Cookie'].split(',')
    mapped = map(lambda cookie: parse_cookie(cookie), header)
    return list(mapped)


def parse_cookie(cookie):
    return cookie.split(';')[0].strip()


session = http.client.HTTPConnection('localhost', 8060, timeout=10)
session.request('GET', '/accountstateservice/brokers')
response = session.getresponse()
response.read()
assert response.status == 302
print(f"Status: {response.status}; reason: {response.reason}; headers: {response.headers}")

session.request('GET', response.headers['Location'])
response = session.getresponse()
response.read()
assert response.status == 302
print(f"Status: {response.status}; reason: {response.reason}; headers: {response.headers}")

# raw_cookie = response.headers['Set-Cookie']
# print(raw_cookie)
# cookies = [raw_cookie.split(';')[0].strip()]
cookies = get_cookies(response.headers)
print(cookies)


auth_server_url = urlparse(response.headers['Location'])
auth_server_conn = http.client.HTTPConnection(auth_server_url.hostname, auth_server_url.port, timeout=10)
auth_server_conn.request('GET', f"{auth_server_url.path}?{auth_server_url.query}")
response = auth_server_conn.getresponse()
print(f"Status: {response.status}; reason: {response.reason}; headers: {response.headers}")
print('cookies', get_cookies(response.headers))
auth_cookies = get_cookies(response.headers)

data = response.read().decode()
# print('data', data)
d = pq(data)
form = d("#kc-form-login")

# print('form', form.attr('action'), form.attr('method'))

# fetch the action attribute from the form
login_url = urlparse(form.attr('action'))
password = 'broker0'
username = 'broker0'

params = urllib.parse.urlencode({'username': username, 'password': password})
headers = {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Cookie': '; '.join(auth_cookies)
}

auth_server_conn.request('POST', f"{login_url.path}?{login_url.query}", params, headers)

response = auth_server_conn.getresponse()
print(f"status: {response.status}; header: {response.headers}")
assert response.status == 302

redirect_url = urlparse(response.headers['Location'])
session.request('GET', f"{redirect_url.path}?{redirect_url.query}", headers={'Cookie': '; '.join(cookies)})
response = session.getresponse()
response.read()
print(f"status: {response.status}; headers: {response.headers}")
assert response.status == 302

redirect_cookies = get_cookies(response.headers)
session.request('GET', response.headers['Location'], headers={'Cookie': '; '.join(redirect_cookies)})
response = session.getresponse()
response.read()
print(f"status: {response.status}; headers: {response.headers}")

session.request('GET', '/accountstateservice/brokers', headers={'Cookie': '; '.join(redirect_cookies)})
response = session.getresponse()
print(response.read().decode())

