import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import sqlite3
import re
import json
import matplotlib as mpl
from zipkin import QueryModeZipkinParser

data = {'tenants': [], 'mode': [], 'avg': [], '50%': [], '95%': [], '99%': []}

for exp in [
    'q-1-b1', 'q-2-b10', 'q-3-b100', 'q-4-b1000',
    'p-1-b1', 'p-2-b10', 'p-3-b100', 'p-4-b1000',
    'h-b1', 'h-b10', 'h-b100', 'h-b1000',
            ]:
    path = f'../results/tenants/{exp}/latencies.sqlite'
    print(f'reading {path}')
    con = sqlite3.connect(path)
    data['tenants'].append(int(re.search("b(\d+)", exp).group(1)))
    if exp[0] == 'p':
        data['mode'].append('postfilter')
    elif exp[0] == 'q':
        data['mode'].append('query')
    elif exp[0] == 'h':
        data['mode'].append('hardcoded')
    else:
        data['mode'] = 'unknown'

    df = pd.read_sql_query('select * from latencies', con).iloc[1:]
    data['avg'].append(df['latency'].mean())
    data['50%'].append(df['latency'].median())
    data['95%'].append(df['latency'].quantile(0.95))
    data['99%'].append(df['latency'].quantile(0.99))

df = pd.DataFrame(data)
ax = sns.lineplot(data=df, x='tenants', y='avg', hue='mode', markers=True, dashes=False, style="mode")

ax.set(xscale='log')
plt.xlabel('Tenants')
plt.xticks([1, 10, 100, 1000])

plt.ylabel('Latency(ms)')
plt.ylim(0, 6500)
plt.savefig(
    'out/tenants.pdf',
    bbox_inches='tight',
    pad_inches=0,

)
plt.show()

csv = df.to_csv(index=False)
file = open('out/tenants.csv', 'w')
file.write(csv)

#
# print('--- post-filter ---')
# pdf = df[df['mode'] == 'postfilter']
# print(pdf)
#
# print('--- query-rewriting ---')
# qdf = df[df['mode'] == 'query']
# print(qdf)
#
# print('--- hardcoded ---')
# hdf = df[df['mode'] == 'hardcoded']
# print(hdf)

def plot_zipkin(name, path_provider, experiments):
    rows = []
    for test in experiments:
        traces = open(path_provider(test))
        z_trace = json.loads(traces.read())
        parser = QueryModeZipkinParser(z_trace)
        z_parse = parser.parse()

        z_df = pd.DataFrame.from_dict(z_parse)
        size = int(re.search('b(\d+)', test).group(1))

        rows.append({
            'size': size,
            'OPA':  z_df['opa'].mean(),
            'Account-State': z_df['acc'].mean(),
            'Routing': z_df['remainder'].mean(),
            'Query': z_df['query'].mean(),
        })

    m_df = pd.DataFrame(rows)
    m_df = m_df.sort_values('size', ascending=False)
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
    plt.ylabel('Broker Count')
    plt.savefig(
        name,
        bbox_inches='tight',
        pad_inches=0,
    )
    plt.show()

    csv = m_df.to_csv()
    file = open('out/tenants_zipkin.csv', 'w')
    file.write(csv)



plot_zipkin(
    'out/tenants_zipkin.pdf',
    lambda exp: f'../results/tenants/{exp}/zipkin.json',
    ['q-1-b1', 'q-2-b10', 'q-3-b100', 'q-4-b1000'],
)