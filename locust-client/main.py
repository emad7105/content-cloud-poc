import time

from locust import HttpUser, task, constant_pacing, events
from session import setup_session
import os
import random

import handlers
import config

c = config.Config(os.environ)

if c.history_enabled:
    writer = handlers.SQLiteWriter(c.history_file)
    stats = handlers.RequestStats(writer)

    events.request_success.add_listener(stats.add_request)
    events.test_stop.add_listener(stats.flush)


def inf_wait(**kwargs):
    print("waiting")
    while True:
        pass
        time.sleep(1)


if c.detached:
    events.quitting.add_listener(inf_wait)

users = []

class AccountStateServiceUser(HttpUser):
    wait_time = constant_pacing(1.0 / c.user_rps)

    @task
    def fetch_brokers(self):
        user = random.choice(users)
        for name, value in user.items():
            self.client.cookies.set(name, value)

        self.client.get(f'/accountstateservice/accountStates?size={c.page_size}')

    def on_start(self):
        # broker_count = c.broker_count
        # broker_idx = random.randint(0, broker_count - 1)

        # print('using broker', broker_idx)
        for broker_idx in random.sample(
                range(0, c.broker_count),
                c.active_user_count
        ):
            print('using broker', broker_idx)
            cookies = setup_session(
                self.host,
                f'broker{broker_idx}',
                f'broker{broker_idx}',
            )
            users.append(cookies)

        # if c.switch_brokers:
        #     for broker_idx in range(0, broker_count - 1):
        #         cookies = setup_session(
        #             self.host,
        #             f'broker{broker_idx}',
        #             f'broker{broker_idx}',
        #         )
        #         users.append(cookies)
        # else:
        #     cookies = setup_session(
        #         self.host,
        #         f'broker{broker_idx}',
        #         f'broker{broker_idx}',
        #     )
        #     users.append(cookies)
        #
        # for broker_idx in range(0, broker_count - 1):
        #     users.append()
        #
        # for name, value in cookies.items():
        #     self.client.cookies.set(name, value)

