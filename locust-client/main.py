from locust import HttpUser, task, between
from session import setup_session
import os
import random

class AccountStateServiceUser(HttpUser):
    wait_time = between(1, 2.5)

    @task
    def fetch_brokers(self):
        self.client.get('/accountstateservice/accountStates')

    def on_start(self):
        broker_count = int(os.environ.get('BROKER_COUNT') or 10)
        broker_idx = random.randint(0, broker_count - 1)
        cookies = setup_session(self.host, f'broker{broker_idx}', f'broker{broker_idx}')

        for name, value in cookies.items():
            self.client.cookies.set(name, value)


# cookies = setup_session('http://localhost:8060', 'broker0', 'broker0')
# session = http.client.HTTPConnection('localhost', 8060)
# session.request('GET', '/accountstateservice/brokers', headers={'Cookie': join_cookies(cookies)})
# resp = session.getresponse()
# assert resp.status == 200
# data = resp.read().decode()
# print(data)
