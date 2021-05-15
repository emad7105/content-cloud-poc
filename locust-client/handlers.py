import csv
import sqlite3
import time
import requests


class RequestStats:
    def __init__(self, writer):
        self.data = []
        self.writer = writer

    def add_request(self, request_type, name, response_time, response_length, **kwargs):
        self.data.append({'time': response_time})

        # flush async
        if len(self.data) > 100:
            print('flushing!')
            self.writer.write_latencies(self.data)
            self.data = []

    def flush(self, **kwargs):
        print('flushing')
        self.writer.write_latencies(self.data)


class SQLiteWriter:
    def __init__(self, db_name):
        self.db_conn = sqlite3.connect(db_name)

    def write_latencies(self, latencies):
        latencies = [(latency['time'],) for latency in latencies]
        cursor = self.db_conn.cursor()

        cursor.execute("""
        CREATE TABLE IF NOT EXISTS latencies (
            latency FLOAT NOT NULL
        );
        """)

        cursor.executemany("""
            insert into latencies values ( ? )
        """, latencies)

        cursor.close()
        self.db_conn.commit()

        print('done')

    def close(self):
        self.db_conn.close()


class CsvWriter:
    def __init__(self, filename):
        self.file = open(filename, 'w', newline='')

    def write_latencies(self, dict_list):
        if len(dict_list) == 0:
            return

        print(dict_list)

        writer = csv.DictWriter(self.file, fieldnames=dict_list[0].keys())
        writer.writeheader()
        for row in dict_list:
            writer.writerow(row)


def inf_wait(**kwargs):
    print("waiting")
    while True:
        pass
        time.sleep(1)


def current_time_millis():
    return round(time.time_ns() / 1000_000)


def pull_zipkin(zipkin_addr, zipkin_file, max_requests):
    start_time = current_time_millis()
    print(f'start time: {start_time}')

    def do_pull(**kwargs):
        r = requests.get(f'http://{zipkin_addr}/api/v2/traces', params={
            'limit': max_requests,
            'lookback': current_time_millis() - start_time,
        })
        if r.status_code != 200:
            print(f'failed to pull zipkin statistics {r.content.decode("utf-8")}')
            return

        content = r.content.decode('utf-8')
        f = open(zipkin_file, 'w')
        f.write(content)
        print('### finished writing zipkin statistics ###')

    return do_pull
