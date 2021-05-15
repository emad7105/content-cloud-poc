class Config:
    def __init__(self, env):
        self.broker_count = int(env.get('CLIENT_BROKER_COUNT') or 100)
        self.active_user_count = int(env.get('CLIENT_ACTIVE_BROKER_COUNT') or 10)
        self.page_size = int(env.get('CLIENT_PAGE_SIZE') or 50)
        self.user_rps = float(env.get('CLIENT_USER_RPS') or 1)
        self.history_enabled = bool(env.get('CLIENT_ENABLE_HISTORY_DB') or True)
        self.history_file = str(env.get('CLIENT_HISTORY_DB_FILE') or 'latencies.sqlite')
        self.detached = bool(env.get('CLIENT_DETACHED') or True)
        self.zipkin_enabled = bool(env.get('CLIENT_ZIPKIN_ENABLED') or True)
        self.zipkin_addr = str(env.get('CLIENT_ZIPKIN_ADDR') or 'localhost:9411')
        self.zipkin_file = str(env.get('CLIENT_ZIPKIN_FILE') or 'results/zipkin.json')
        self.zipkin_max_requests = int(env.get('CLIENT_ZIPKIN_MAX_REQUESTS') or 1 << 16)

