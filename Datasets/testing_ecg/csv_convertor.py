import os
import pandas as pd
import sys


dir_path = os.path.dirname(os.path.realpath(__file__))
files = [x for x in os.listdir(dir_path) if x.endswith('.csv')]

for file in files:
    try:
        df = pd.read_csv(file, sep=';', header=None)
        print(df)
        df.drop(0, inplace=True)
        print(df)
        df.to_csv(dir_path + "\\" + file, index=False, header=None)
    except:
        pass
