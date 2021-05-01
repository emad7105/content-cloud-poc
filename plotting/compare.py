import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import sqlite3

con_q = sqlite3.connect('../aks-10m-s10-pg50-q/latencies.sqlite')
con_p = sqlite3.connect('../p-s10-pg50-10m-aks-rand/latencies.sqlite')

df_q = pd.read_sql_query('select * from latencies', con_q).iloc[1:]
df_p = pd.read_sql_query('select * from latencies', con_p).iloc[1:]

df_q['mode'] = 'query'
df_p['mode'] = 'postfilter'

# pd.concat([df_q, df_p], ignore_index=True)

df = pd.concat([df_q, df_p])

ax = sns.histplot(data=df, x='latency', hue='mode')
ax.set(xscale='log')
plt.show()

ax = sns.boxplot(data=df, x='mode', y='latency')

print('query rewriting')
print(df_q['latency'].min())
print(df_q['latency'].mean())
print(df_q['latency'].median())
print(df_q['latency'].quantile(0.95))
print(df_q['latency'].quantile(0.99))
print(df_q['latency'].max())

print("------------------------------")
print('postfiltering')
print(df_p['latency'].min())
print(df_p['latency'].mean())
print(df_p['latency'].median())
print(df_p['latency'].quantile(0.95))
print(df_p['latency'].quantile(0.99))
print(df_p['latency'].max())



plt.show()
