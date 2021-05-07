import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import sqlite3
import re

data = {'tenants': [], 'mode': [], 'avg': [], '50%': [], '95%': [], '99%': []}

for exp in ['q-b1-pg50-aks', 'q-b10-pg50-aks', 'q-b100-pg50-aks',
            'p-b1-pg50-aks', 'p-b10-pg50-aks', 'p-b100-pg50-aks',
            'h-b1-pg50-aks', 'h-b10-pg50-aks', 'h-b100-pg50-aks']:
    path = f'../results/tenant_tests/{exp}/latencies.sqlite'
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
ax = sns.lineplot(data=df, x='tenants', y='99%', hue='mode', markers=True, dashes=False, style="mode")

ax.set(xscale='log')
plt.xlabel('Tenants')
plt.xticks([1, 10, 100])

plt.ylabel('Latency(ms)')
plt.ylim(0, 6500)
plt.savefig('tenants.pdf')
plt.title('99th percentile latency')
plt.show()

print('--- post-filter ---')
pdf = df[df['mode'] == 'postfilter']
print(pdf)

print('--- query-rewriting ---')
qdf = df[df['mode'] == 'query']
print(qdf)

print('--- hardcoded ---')
hdf = df[df['mode'] == 'hardcoded']
print(hdf)
