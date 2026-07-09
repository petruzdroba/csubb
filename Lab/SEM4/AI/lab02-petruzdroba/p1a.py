import pandas as pd
import numpy as np

data = pd.read_csv('/home/petru/Desktop/Ai/lab02-petruzdroba/data/surveyDataSience.csv', delimiter=',', low_memory=False, header=1)
print('Number of respondends: ', len(data) - 1)

print('Types of columns: ', data.dtypes)
print('Number of columns: ',len(data.columns))

print('Number of respondends with complete data: ',data.dropna().shape[0]) # drop not available


def avg_duration(education_frequency:dict):
    bachelor, masters, doctorate = 3, 3+2, 3+2+3
    total = education_frequency.get('Master’s degree', 0) * masters
    total += (education_frequency.get('Bachelor’s degree', 0) + 
                     education_frequency.get('Some college/university study without earning a bachelor’s degree', 0)) * bachelor
    total += (education_frequency.get('Doctoral degree', 0) + 
                     education_frequency.get('Professional doctorate', 0)) * doctorate

    return total / (sum(education_frequency.values()) - education_frequency.get('I prefer not to answer', 0))


education_frequency = data['What is the highest level of formal education that you have attained or plan to attain within the next 2 years?'].value_counts().to_dict()
print('Average duration of studies: ',avg_duration(education_frequency))

education_frequency = data[ data['In which country do you currently reside?'] =='Romania']['What is the highest level of formal education that you have attained or plan to attain within the next 2 years?'].value_counts().to_dict()
print('Average duration of studies for Romanians: ',avg_duration(education_frequency))

education_frequency = data[(data['What is your gender? - Selected Choice'] == 'Woman') & (data['In which country do you currently reside?'] == 'Romania')]['What is the highest level of formal education that you have attained or plan to attain within the next 2 years?'].value_counts().to_dict()
print('Average duration of studies for Romanian Women: ',avg_duration(education_frequency))


print('Number of Romanian Women with complete data: ',  data[(data['What is your gender? - Selected Choice'] == 'Woman') & (data['In which country do you currently reside?'] == 'Romania')].dropna().shape[0])


python_women = data[(data['What is your gender? - Selected Choice'] == 'Woman') & (data['In which country do you currently reside?'] == 'Romania') & (data['What programming languages do you use on a regular basis? (Select all that apply) - Selected Choice - Python'] == 'Python')]
print('Number of Romanian Women that program in Python: ', python_women.shape[0])
print(python_women['What is your age (# years)?'].value_counts().index[0])

c_women = data[(data['What is your gender? - Selected Choice'] == 'Woman') & (data['In which country do you currently reside?'] == 'Romania') & (data['What programming languages do you use on a regular basis? (Select all that apply) - Selected Choice - C++'] == 'C++')]
print('Number of Romanian Women that program in C++: ', c_women.shape[0])
print(c_women['What is your age (# years)?'].value_counts().index[0])


data2 = pd.read_csv('/home/petru/Desktop/Ai/lab02-petruzdroba/data/surveyDataSience.csv', delimiter=',', low_memory=False, header=0, skiprows=[1])

for question in data2.columns:
    base = question.split('_Part_')[0].split('_OTHER')[0] # get all questions 
    
grouped = {}# group part questions in the same category
for question in data2.columns:
    base = question.split('_Part_')[0].split('_OTHER')[0] # question key : [ parts ] like Q23 : ['Q23_Part_1' ...]
    if base not in grouped:
        grouped[base] = []
    grouped[base].append(question)

for base, parts in grouped.items():
    all_vals = set() # use a set to ignore duplicates
    for part in parts:
        col = data2[part]
        if col.dtype not in ['int64', 'float64']: 
            all_vals.update(col.dropna().unique().tolist()) # unique works per column but not across multiple parts
    
    if all_vals:
        print(f"{base}:(string): {', '.join(str(v) for v in all_vals)}\n") #prnt all values possible for question
    else:
        if col.dropna().empty:
            print(f"{base}: (int): No data\n")
        else:
            print(f"{base}: (int): {int(col.dropna().min())} - {int(col.dropna().max())}\n") # print ends of the possible values
            
            
            
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
median = np.median(programming_data)

print(f'Minimum: {minn}\nMaximum: {maxx}\nMean: {mean}\nStandard deviation: {std}\nMedian: {median}')