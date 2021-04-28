import http.client
import os
from session import setup_session, join_cookies

host = os.environ.get('ATTACKED_HOST') or 'gateway'
port = int(os.environ.get('ATTACKED_PORT') or 8060)
size = int(os.environ.get('SIZE') or 50)

cookies = setup_session(f"http://{host}:{port}", 'broker0', 'broker0')

conn = http.client.HTTPConnection(host, 8060, timeout=10)

conn.request('GET', f'/accountstateservice/accountStates?size={size}', headers={'Cookie': join_cookies(cookies)})
response = conn.getresponse()
data = response.read().decode()
print(f"status: {response.status}")
print(f"headers: {response.headers}")
print(f"data:\n{data}")
