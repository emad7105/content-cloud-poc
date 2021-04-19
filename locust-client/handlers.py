import csv
import sqlite3


class RequestStats:
    def __init__(self, writer):
        self.data = []
        self.writer = writer

    def add_request(self, request_type, name, response_time, response_length, **kwargs):
        print(f'adding data {response_time}')
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
