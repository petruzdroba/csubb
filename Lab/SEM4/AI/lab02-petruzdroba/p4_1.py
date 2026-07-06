import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

data = pd.read_csv('/home/petru/Desktop/Ai/lab02-petruzdroba/data/surveyDataSience.csv', delimiter=',', low_memory=False, header=1)

years = {
    '1-3 years': 2,
    '< 1 years': 0.5,
    '3-5 years': 4,
    '5-10 years': 7.5,
    '10-20 years': 15,
    '20+ years': 20,
}

mapped = data['For how many years have you been writing code and/or programming?'].map(years)# replace with equivalent experience
programming_data = mapped.dropna().values  # numpy array direct

minn = np.min(programming_data)
maxx = np.max(programming_data)
mean = np.mean(programming_data)
std = np.std(programming_data)

programming_minmax = (programming_data - minn) / (maxx - minn) # smallest value = 0, largest becomes 1
programming_znorm = (programming_data - mean) / std #weigh everything on a scale 0 - 1
programming_log = np.log1p(programming_data)

fig, (ax1, ax2, ax3, ax4) = plt.subplots(1, 4)
fig.suptitle('Programming Experience')

ax1.hist(programming_data, bins=20)
ax1.set_title('Original')
ax1.set_xlabel('Years')
ax1.set_ylabel('Number of respondents')

ax2.hist(programming_minmax, bins=20)
ax2.set_title('Min-Max [0,1]')
ax2.set_xlabel('Years')
ax2.set_ylabel('Number of respondents')

ax3.hist(programming_znorm, bins=20)
ax3.set_title('Z-Score')
ax3.set_xlabel('Years')
ax3.set_ylabel('Number of respondents')

ax4.hist(programming_log, bins=20)
ax4.set_title('Log Normalized')
ax4.set_xlabel('log(1 + years)')
ax4.set_ylabel('Number of respondents')

plt.show()

years = {
    'I prefer not to answer':0,
    'Bachelor’s degree': 3,
    'Master’s degree' : 3+2,
    'Some college/university study without earning a bachelor’s degree': 3,
    'Doctoral degree' : 3+2+3,
    'Professional doctorate': 3+2+3,
}

mapped = data['What is the highest level of formal education that you have attained or plan to attain within the next 2 years?'].map(years)# replace with equivalent experience
programming_data = mapped.dropna().values  # numpy array direct

minn = np.min(programming_data)
maxx = np.max(programming_data)
mean = np.mean(programming_data)
std = np.std(programming_data)

programming_minmax = (programming_data - minn) / (maxx - minn) # min max 
programming_znorm = (programming_data - mean) / std #Z scale
programming_log = np.log1p(programming_data)

fig, (ax1, ax2, ax3, ax4) = plt.subplots(1, 4)
fig.suptitle('Years of Higher Studies')

ax1.hist(programming_data, bins=20)
ax1.set_title('Original') # raw values on a hist 
ax1.set_xlabel('Years')
ax1.set_ylabel('Number of respondents')

ax2.hist(programming_minmax, bins=20)
ax2.set_title('Min-Max [0,1]') # rescales raw values from 0 to 1
ax2.set_xlabel('Years')
ax2.set_ylabel('Number of respondents')

ax3.hist(programming_znorm, bins=20)
ax3.set_title('Z-Score') # values close to 0 are average and one unit is one std dev
ax3.set_xlabel('Years')
ax3.set_ylabel('Number of respondents')

ax4.hist(programming_log, bins=20)
ax4.set_title('Log Normalized')
ax4.set_xlabel('log(1 + years)')
ax4.set_ylabel('Number of respondents')

plt.show()