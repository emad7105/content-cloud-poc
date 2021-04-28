import http.client
from urllib.parse import urlparse
import urllib
from pyquery import PyQuery as pq
import logging

log = logging.getLogger('init')


def setup_session(gateway_url, username, password, init_path='/'):
    # first login at the resource server (gateway)
    gateway_url = urlparse(gateway_url)
    gateway_conn = http.client.HTTPConnection(gateway_url.hostname, gateway_url.port, timeout=10)
    (response, data) = do_request(gateway_conn, 'GET', init_path)
    assert response.status == 302
    log.debug(f"status: {response.status}; headers: {response.headers}")

    # follow redirect to dedicated oauth2 endpoint
    redirect_url = response.headers['Location']
    (response, data) = do_request(gateway_conn, 'GET', redirect_url)
    assert response.status == 302
    log.debug(f"status: {response.status}; headers: {response.headers}")
    gateway_cookies = get_cookies(response.headers)
    redirect_url = response.headers['Location']

    # fetch keycloak login page
    keycloak_url = urlparse(redirect_url)
    keycloak_conn = http.client.HTTPConnection(keycloak_url.hostname, keycloak_url.port, timeout=10)
    (response, data) = do_request(keycloak_conn, 'GET', f"{keycloak_url.path}?{keycloak_url.query}")
    assert response.status == 200
    log.debug(f"status: {response.status}; headers: {response.headers}")
    keycloak_cookies = get_cookies(response.headers)

    d = pq(data)
    form = d("#kc-form-login")
    login_url = urlparse(form.attr('action'))
    params = urllib.parse.urlencode({'username': username, 'password': password})
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Cookie': join_cookies(keycloak_cookies)
    }
    # login to keycloak
    (response, data) = do_request(keycloak_conn, 'POST', f"{login_url.path}?{login_url.query}", params, headers)
    log.debug(f"status: {response.status}; headers: {response.headers}")
    assert response.status == 302
    redirect_url = urlparse(response.headers['Location'])

    print(f"status: {response.status}; headers: {response.headers}")
    print(f"data: {response.read().decode()}")
    # follow redirect
    (response, data) = do_request(gateway_conn, 'GET', f"{redirect_url.path}?{redirect_url.query}", headers={'Cookie': join_cookies(gateway_cookies)})
    assert response.status == 302
    return get_cookies(response.headers)


def do_request(session, method, path, body=None, headers={}):
    session.request(method, path, body, headers)
    response = session.getresponse()
    data = response.read().decode()
    return response, data


def get_cookies(headers):
    header = headers['Set-Cookie'].split(',')
    cookie_tuples = map(lambda cookie: parse_cookie(cookie), header)
    return dict(list(cookie_tuples))


def parse_cookie(cookie):
    cookie = cookie.split(';')[0].strip()
    [key, value] = cookie.split('=')
    return key, value


def join_cookies(cookies):
    return '; '.join(f"{key}={value}" for key, value in cookies.items())
