import json
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd

OPA = 'opa'
ACC_STATE = 'acc-state'
GATEWAY = 'gateway'
REMAINDER = 'remainder'


def parse_zipkin(traces):
    delays = {OPA: [], ACC_STATE: [], GATEWAY: []}
    percent = {OPA: [], ACC_STATE: [], REMAINDER: []}
    for trace in traces:
        trace_delays, ok = parse_trace(trace)

        if not ok:
            continue

        delays[OPA].append(trace_delays[OPA])
        delays[ACC_STATE].append(trace_delays[ACC_STATE])
        delays[GATEWAY].append(trace_delays[GATEWAY])

        total_delay = trace_delays[GATEWAY]
        opa_delay = trace_delays[OPA]
        acc_state_delay = trace_delays[ACC_STATE]

        percent[OPA].append(opa_delay / total_delay)
        percent[ACC_STATE].append(acc_state_delay / total_delay)
        percent[REMAINDER].append((total_delay - opa_delay - acc_state_delay) / total_delay)

    return delays, percent


def parse_trace(trace):
    delays = {}
    for span in trace:
        if is_opa_call(span):
            # print('opa call')
            delays[OPA] = get_duration(span)
        if is_acc_state(span):
            # print('acc-state call')
            delays[ACC_STATE] = get_duration(span)
        if is_gateway_call(span):
            # print('gw call')
            delays[GATEWAY] = get_duration(span)
    return delays, len(delays) == 3


def is_opa_call(span):
    tags = get_tags(span)
    return tags['http.method'] == 'POST' \
        and tags['http.path'] == "/v1/compile"


def is_gateway_call(span):
    tags = get_tags(span)
    kind = get_kind(span)
    return tags['http.method'] == 'GET' \
        and tags['http.path'] == '/accountstateservice/accountStates' \
        and kind == 'SERVER'


def is_acc_state(span):
    tags = get_tags(span)
    kind = get_kind(span)
    return tags['http.method'] == 'GET' \
        and tags['http.path'] == '/accountStates' \
        and kind == 'SERVER'


def get_tags(span):
    return span['tags']


def get_kind(span):
    return span['kind']


def get_duration(span):
    return span['duration']


traces = open('zipkin.json')
z_trace = json.loads(traces.read())
_, z_parse = parse_zipkin(z_trace)

z_df = pd.DataFrame.from_dict(z_parse)
opa_mean = z_df[OPA].mean()
acc_mean = z_df[ACC_STATE].mean()
rem_mean = z_df[REMAINDER].mean()

m_df = pd.DataFrame.from_dict({
 'kind': ['OPA', 'Service', 'Remainder'],
 'percentage': [opa_mean, acc_mean, rem_mean],
})

sns.set()
m_df = m_df.sort_values('percentage')
m_df = m_df.set_index('kind')

print(m_df)


sns.set_style("ticks")
sns.despine()

ax = m_df.T.plot(kind='barh', stacked=True, )
handles, labels = ax.get_legend_handles_labels()
ax.legend(handles=handles[:], labels=labels[:])
plt.xlabel('Contribution to latency')
plt.ylabel(None)
plt.yticks([])

plt.show()
