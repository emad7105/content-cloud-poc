import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import sqlite3

dfs = []

for x in [1, 10, 20, 40, 60]:
    con = sqlite3.connect(f'../aks-10m-s{x}-pg50-q/latencies.sqlite')
    df = pd.read_sql_query('select * from latencies', con)
    df['selectivity'] = x
    dfs.append(df)

data = pd.concat(dfs)
sns.boxplot(data=data, x='selectivity', y='latency')
plt.show()

print(df['latency'].mean())
print(df['latency'].median())
print(df['latency'].quantile(0.95))
print(df['latency'].quantile(0.99))



con.close()
