import sqlite3
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import matplotlib as mpl
import re
import json
from zipkin import QueryModeZipkinParser

# data = {'cpu': [], 'sel': [], 'mode': [], 'avg': [], '50%': [], '95%': [], '99%': []}
data = {'sel': [], 'mode': [], 'avg': [], '50%': [], '95%': [], '99%': []}
for exp in [
    ('q-1-s100', 100), ('q-2-s10', 10), ('q-3-s1', 1), ('q-4-s0_1', 0.1), ('q-5-s0_01', 0.01),
    ('p-c-s100', 100), ('p-c-s10', 10), ('p-c-s1', 1), ('p-c-s0_1', 0.1), ('p-c-s0_01', 0.01),
    ('h-s100', 100), ('h-s10', 10), ('h-s1', 1), ('h-s0_1', 0.1), ('h-s0_01', 0.01),
            ]:
    print('reading: ', f'../results/selectivity/{exp[0]}/latencies.sqlite')
    con = sqlite3.connect(f'../results/selectivity/{exp[0]}/latencies.sqlite')
    df = pd.read_sql_query('select * from latencies', con).iloc[1:]
    data['sel'].append(exp[1])
    if exp[0][0] == 'p':
        data['mode'].append('postfilter')
    elif exp[0][0] == 'h':
        data['mode'].append('hardcoded')
    else:
        data['mode'].append('query')

    data['avg'].append(df['latency'].mean())
    data['50%'].append(df['latency'].median())
    data['95%'].append(df['latency'].quantile(0.95))
    data['99%'].append(df['latency'].quantile(0.99))

    # df = pd.read_csv(f'../results/selectivity_tests/{exp}/metrics.csv')
    # data['cpu'].append(df['CPU'].max())


df = pd.DataFrame(data)
ax = sns.lineplot(data=df, x='sel', y='avg', hue='mode',  markers=True, dashes=False, style="mode")

ax.set(xscale='log')
plt.xlabel('Selectivity')
plt.xticks([0.01, 0.1, 1, 10, 100])

plt.ylabel('Latency (ms)')
plt.ylim(0, 4000)
plt.savefig(
    'out/selectivity.pdf',
    bbox_inches='tight',
    pad_inches=0,
)
plt.title('99th percentile latency')
plt.show()

csv = df.to_csv(index=False)
file = open('out/selectivity.csv', 'w')
file.write(csv)

# df = pd.DataFrame(data)
# ax = sns.lineplot(data=data, x='sel', y='cpu', hue='mode',  markers=True, dashes=False, style="mode")
#
# ax.set(xscale='log')
# plt.xlabel('Selectivity')
# plt.xticks([1, 10, 100])
#
# plt.ylabel('CPU%')
# plt.ylim(0, 100)
# plt.savefig('cpu.pdf')
# plt.title('Maximum CPU usage')
# plt.show()

print('--- post-filter ---')
pdf = df[df['mode'] == 'postfilter']
print(pdf)

print('--- query rewriting ---')
qdf = df[df['mode'] == 'query']
print(qdf)

print('--- hardcoded ---')
hdf = df[df['mode'] == 'hardcoded']
print(hdf)

def plot_zipkin(name, path_provider, experiments):
    rows = []
    for test in experiments:
        traces = open(path_provider(test[0]))
        z_trace = json.loads(traces.read())
        parser = QueryModeZipkinParser(z_trace)
        z_parse = parser.parse()

        z_df = pd.DataFrame.from_dict(z_parse)
        size = test[1]

        rows.append({
            'size': size,
            'OPA':  z_df['opa'].mean(),
            'Service': z_df['acc'].mean(),
            'Remainder': z_df['remainder'].mean(),
            'Query': z_df['query'].mean(),
        })

    m_df = pd.DataFrame(rows)
    m_df = m_df.sort_values('size', ascending=True)
    m_df = m_df.set_index('size')
    print(m_df)
    mpl.rcParams['axes.spines.left'] = False
    mpl.rcParams['axes.spines.right'] = False
    mpl.rcParams['axes.spines.top'] = False
    mpl.rcParams['axes.spines.bottom'] = False

    ax = m_df.plot.barh(stacked=True)
    ax.legend(loc='upper center', bbox_to_anchor=(0.5, 1.05),
              ncol=4, fancybox=True, shadow=True)
    ax.tick_params('y', left=False)
    # ax.axis('off')
    # handles, labels = ax.get_legend_handles_labels()
    # ax.legend(handles=handles[:], labels=labels[:])
    plt.xlabel('Contribution to latency')
    plt.ylabel('Selectivity')
    plt.savefig(
        name,
        bbox_inches='tight',
        pad_inches=0,
    )
    plt.show()

    csv = m_df.to_csv()
    file = open('out/selectivity_zipkin.csv', 'w')
    file.write(csv)


plot_zipkin(
    'out/selectivity_zipkin.pdf',
    lambda exp: f'../results/selectivity/{exp}/zipkin.json',
    [('q-5-s0_01', 0.01), ('q-4-s0_1', 0.1), ('q-3-s1', 1), ('q-2-s10', 10), ('q-1-s100', 100)],
)
