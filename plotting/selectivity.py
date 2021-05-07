import sqlite3
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import re

data = {'cpu': [], 'sel': [], 'mode': [], 'avg': [], '50%': [], '95%': [], '99%': []}
for exp in ['aks-10m-s1-pg50-q', 'aks-10m-s10-pg50-q', 'aks-10m-s100-pg50-q',
            'p-s1-pg50-10m-aks-rand', 'p-s10-pg50-10m-aks-mod', 'p-s100-pg50-10m-aks-rand',
            'h-s1-pg50-10m-aks', 'h-s10-pg50-10m-aks', 'h-s100-pg50-10m-aks']:
    print('reading: ', f'../results/selectivity_tests/{exp}/latencies.sqlite')
    con = sqlite3.connect(f'../results/selectivity_tests/{exp}/latencies.sqlite')
    df = pd.read_sql_query('select * from latencies', con).iloc[1:]
    data['sel'].append(int(re.search("s(\d+)", exp).group(1)))
    if exp[0] == 'p':
        data['mode'].append('postfilter')
    elif exp[0] == 'h':
        data['mode'].append('hardcoded')
    else:
        data['mode'].append('query')

    data['avg'].append(df['latency'].mean())
    data['50%'].append(df['latency'].median())
    data['95%'].append(df['latency'].quantile(0.95))
    data['99%'].append(df['latency'].quantile(0.99))

    df = pd.read_csv(f'../results/selectivity_tests/{exp}/metrics.csv')
    data['cpu'].append(df['CPU'].max())

df = pd.DataFrame(data)
ax = sns.lineplot(data=data, x='sel', y='99%', hue='mode',  markers=True, dashes=False, style="mode")

ax.set(xscale='log')
plt.xlabel('Selectivity')
plt.xticks([1, 10, 100])

plt.ylabel('Latency (ms)')
plt.ylim(0, 4000)
plt.savefig('selectivity.pdf')
plt.title('99th percentile latency')
plt.show()

df = pd.DataFrame(data)
ax = sns.lineplot(data=data, x='sel', y='cpu', hue='mode',  markers=True, dashes=False, style="mode")

ax.set(xscale='log')
plt.xlabel('Selectivity')
plt.xticks([1, 10, 100])

plt.ylabel('CPU%')
plt.ylim(0, 100)
plt.savefig('cpu.pdf')
plt.title('Maximum CPU usage')
plt.show()

print('--- post-filter ---')
pdf = df[df['mode'] == 'postfilter']
print(pdf)

print('--- query rewriting ---')
qdf = df[df['mode'] == 'query']
print(qdf)

print('--- hardcoded ---')
hdf = df[df['mode'] == 'hardcoded']
print(hdf)
# ax = sns.lineplot(data=data, x='sel', y='95%', hue='mode',  markers=True, dashes=False, style="mode")
# ax.set(xscale='log')
# plt.ylim(0, 4000)
# plt.show()
#
# ax = sns.lineplot(data=data, x='sel', y='50%', hue='mode',  markers=True, dashes=False, style="mode")
# ax.set(xscale='log')
# plt.ylim(0, 4000)
# plt.show()
#
# ax = sns.lineplot(data=data, x='sel', y='avg', hue='mode',  markers=True, dashes=False, style="mode")
# ax.set(xscale='log')
# plt.ylim(0, 4000)
# plt.show()
