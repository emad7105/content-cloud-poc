class QueryModeZipkinParser:
    def __init__(self, traces):
        self.traces = traces

    def parse(self):
        delays = {'opa': [], 'acc': [], 'query': [], 'remainder': []}
        for trace in self.traces:
            trace_delays, ok = self.parse_trace(trace)

            if not ok:
                continue

            total_delay = trace_delays['gw']
            opa_delay = trace_delays['opa']
            acc_state_delay = trace_delays['acc'] - trace_delays['query']
            query_delay = trace_delays['query']

            delays['opa'].append(opa_delay / total_delay)
            delays['acc'].append(acc_state_delay / total_delay)
            delays['query'].append(query_delay / total_delay)
            delays['remainder'].append((total_delay - opa_delay - acc_state_delay - query_delay) / total_delay)

        return delays

    def parse_trace(self, trace):
        delays = {}
        for span in trace:
            if self.is_opa_call(span):
                # print('opa call')
                delays['opa'] = span['duration']
            if self.is_acc_state(span):
                # print('acc-state call')
                delays['acc'] = span['duration']
            if self.is_query(span):
                delays['query'] = span['duration']
            if self.is_gateway_call(span):
                # print('gw call')
                delays['gw'] = span['duration']

        return delays, len(delays) == 4

    def parse_old(self):
        delays = {'opa': [], 'acc': [], 'remainder': []}
        for trace in self.traces:
            trace_delays, ok = self.parse_trace_old(trace)

            if not ok:
                continue

            total_delay = trace_delays['gw']
            acc_state_delay = trace_delays['acc']
            opa_delay = trace_delays['opa']

            delays['opa'].append(opa_delay / total_delay)
            delays['acc'].append(acc_state_delay / total_delay)
            delays['remainder'].append((total_delay - opa_delay - acc_state_delay) / total_delay)

        return delays

    def parse_trace_old(self, trace):
        delays = {}
        for span in trace:
            if self.is_opa_call(span):
                # print('opa call')
                delays['opa'] = span['duration']
            if self.is_acc_state(span):
                # print('acc-state call')
                delays['acc'] = span['duration']
            if self.is_gateway_call(span):
                # print('gw call')
                delays['gw'] = span['duration']
        return delays, len(delays) == 3

    def is_opa_call(self, span):
        if 'tags' not in span:
            return False
        tags = span['tags']
        return tags['http.method'] == 'POST' \
               and tags['http.path'] == "/v1/compile"

    def is_gateway_call(self, span):
        if 'tags' not in span:
            return False
        tags = span['tags']

        if 'kind' not in span:
            return False
        kind = span['kind']
        return tags['http.method'] == 'GET' \
               and tags['http.path'] == '/accountstateservice/accountStates' \
               and kind == 'SERVER'

    def is_acc_state(self, span):
        if 'tags' not in span:
            return False
        tags = span['tags']

        if 'kind' not in span:
            return False
        kind = span['kind']

        return tags['http.method'] == 'GET' \
               and tags['http.path'] == '/accountStates' \
               and kind == 'SERVER'

    def is_query(self, span):
        return span['name'] == 'query'

class PostfilterModeZipkinParser:

    def __init__(self, traces):
        self.traces = traces

    def parse(self):
        percent = {'postfilter': [], 'acc': [], 'remainder': []}
        for trace in self.traces:
            delay, ok = self.trace_delays(trace)
            if not ok:
                continue

            total_delay = delay['gw']
            postfilter = delay['postfilter'] / total_delay
            acc_state = (delay['acc'] - delay['postfilter']) / total_delay
            remainder = (total_delay - delay['acc']) / total_delay

            percent['postfilter'].append(postfilter)
            percent['acc'].append(acc_state)
            percent['remainder'].append(remainder)
        return percent

    def trace_delays(self, trace):
        delays = {}

        for span in trace:
            if self.is_postfilter_call(span):
                # print('opa call')
                delays['postfilter'] = span['duration']
            if self.is_acc_call(span):
                # print('acc call')
                delays['acc'] = span['duration']
            if self.is_gw_call(span):
                # print('gw call')
                delays['gw'] = span['duration']

        return delays, len(delays) == 3

    def is_postfilter_call(self, span):
        return span['name'] == 'postfilter-opa'

    def is_gw_call(self, span):
        if 'tags' not in span:
            return False

        tags = span['tags']
        return tags['http.method'] == 'GET' \
               and tags['http.path'] == '/accountstateservice/accountStates' \
               and span['kind'] == 'SERVER'

    def is_acc_call(self, span):
        if 'tags' not in span:
            return False
        tags = span['tags']
        return tags['http.method'] == 'GET' \
               and tags['http.path'] == '/accountStates' \
               and span['kind'] == 'SERVER'
