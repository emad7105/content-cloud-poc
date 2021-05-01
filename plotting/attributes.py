import sqlite3
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import re

data = {'attr': [], 'mode': [], 'avg': [], '50%': [], '95%': [], '99%': []}
for exp in ['p-aks-10m-attr5-s1-pg50', 'p-aks-10m-attr15-s1-pg50', 'p-aks-10m-attr25-s1-pg50', 'q-aks-10m-attr5-s1-pg50', 'q-aks-10m-attr15-s1-pg50', 'q-aks-10m-attr25-s1-pg50']:
    print('reading: ', f'../results/attr_tests/{exp}/latencies.sqlite')
    con = sqlite3.connect(f'../results/attr_tests/{exp}/latencies.sqlite')
    df = pd.read_sql_query('select * from latencies', con).iloc[1:]
    data['attr'].append(int(re.search("attr(\d+)", exp).group(1)))
    if exp[0] == 'p':
        data['mode'].append('postfilter')
    else:
        data['mode'].append('query')

    data['avg'].append(df['latency'].mean())
    data['50%'].append(df['latency'].median())
    data['95%'].append(df['latency'].quantile(0.95))
    data['99%'].append(df['latency'].quantile(0.99))

df = pd.DataFrame(data)
ax = sns.lineplot(data=df[df['mode'] == 'query'], x='attr', y='99%', hue='mode',  markers=True, dashes=False, style="mode")
plt.xlabel('attributes')
# plt.xticks([1, 10, 100])

plt.ylabel('latency (ms)')
# plt.ylim(0, 4000)
plt.savefig('selectivity.png')
plt.title('99th percentile latency')
plt.show()

ax = sns.lineplot(data=data, x='attr', y='95%', hue='mode',  markers=True, dashes=False, style="mode")
# ax.set(xscale='log')
# plt.ylim(0, 4000)
plt.show()

ax = sns.lineplot(data=data, x='attr', y='50%', hue='mode',  markers=True, dashes=False, style="mode")
# ax.set(xscale='log')
# plt.ylim(0, 4000)
plt.show()

ax = sns.lineplot(data=data, x='attr', y='avg', hue='mode',  markers=True, dashes=False, style="mode")
# ax.set(xscale='log')
# plt.ylim(0, 4000)
plt.show()

