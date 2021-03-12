from locust import HttpUser, task, between
from session import setup_session
import os


class AccountStateServiceUser(HttpUser):
    wait_time = between(1, 2.5)

    @task
    def fetch_brokers(self):
        self.client.get('/accountstateservice/brokers')

    def on_start(self):
        print(f"host: {self.host}")

        cookies = setup_session(self.host, 'broker0', 'broker0')

        for name, value in cookies.items():
            self.client.cookies.set(name, value)


# cookies = setup_session('http://localhost:8060', 'broker0', 'broker0')
# session = http.client.HTTPConnection('localhost', 8060)
# session.request('GET', '/accountstateservice/brokers', headers={'Cookie': join_cookies(cookies)})
# resp = session.getresponse()
# assert resp.status == 200
# data = resp.read().decode()
# print(data)
