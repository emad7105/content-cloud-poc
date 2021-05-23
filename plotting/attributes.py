import sqlite3
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import re

data = {'attr': [], 'mode': [], 'avg': [], '50%': [], '95%': [], '99%': []}
for exp in [
        'q-a1', 'q-a5', 'q-a15', 'q-a25',
        'h-a1', 'h-a5', 'h-a15', 'h-a25']:
    print('reading: ', f'../results/attributes/{exp}/latencies.sqlite')
    con = sqlite3.connect(f'../results/attributes/{exp}/latencies.sqlite')
    df = pd.read_sql_query('select * from latencies', con).iloc[1:]
    data['attr'].append(int(re.search("(\d+)", exp).group(1)))
    if exp[0] == 'p':
        data['mode'].append('postfilter')
    if exp[0] == 'h':
        data['mode'].append('hardcoded')
    else:
        data['mode'].append('query')

    data['avg'].append(df['latency'].mean())
    data['50%'].append(df['latency'].median())
    data['95%'].append(df['latency'].quantile(0.95))
    data['99%'].append(df['latency'].quantile(0.99))

df = pd.DataFrame(data)
ax = sns.lineplot(data=df, x='attr', y='99%', hue='mode',  markers=True, dashes=False, style="mode")
plt.xlabel('attributes')
# plt.xticks([1, 10, 100])

plt.ylabel('latency (ms)')
# plt.ylim(0, 4000)
# plt.title('99th percentile latency')
plt.savefig(
    'out/attribute.pdf',
    bbox_inches='tight',
    pad_inches=0,
)
plt.show()

csv = df.to_csv(index=False)
file = open('out/attribute.csv', 'w')
file.write(csv)


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

