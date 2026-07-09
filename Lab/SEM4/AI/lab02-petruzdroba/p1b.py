import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

def bar_chart(data: dict, title: str, file_name: str, xlabel: str = 'xlabel', ylabel: str = 'ylabel'):
    plt.bar(data.keys(), data.values())
    
    plt.title(title)
    plt.xlabel(xlabel, fontsize=12)
    plt.ylabel(ylabel, fontsize=12)
    
    plt.savefig(f'/home/petru/Desktop/Ai/lab02-petruzdroba/charts/{file_name}.png')
    plt.clf()  

data = pd.read_csv('/home/petru/Desktop/Ai/lab02-petruzdroba/data/surveyDataSience.csv', delimiter=',', low_memory=False, header=1)

python_programming = data[data['What programming languages do you use on a regular basis? (Select all that apply) - Selected Choice - Python'] == 'Python']
bar_chart(python_programming['What is your age (# years)?'].dropna().value_counts().to_dict(), 'Python age distribution', 'python_age_dist', 'Age Group', 'Count')

python_programming_RO = data[(data['What programming languages do you use on a regular basis? (Select all that apply) - Selected Choice - Python'] == 'Python') & (data['In which country do you currently reside?'] == 'Romania')]
bar_chart(python_programming_RO['What is your age (# years)?'].dropna().value_counts().to_dict(), 'Python age distribution in Romania', 'python_age_dist_RO', 'Age Group', 'Count')

python_programming_RO_W = data[(data['What programming languages do you use on a regular basis? (Select all that apply) - Selected Choice - Python'] == 'Python') & (data['In which country do you currently reside?'] == 'Romania') & (data['What is your gender? - Selected Choice'] == 'Woman')]
bar_chart(python_programming_RO_W['What is your age (# years)?'].dropna().value_counts().to_dict(), 'Python age distribution in Romanian Women', 'python_age_dist_RO_W', 'Age Group', 'Count')


years = {
    '< 1 years': 0.5,
    '1-3 years': 2,
    '3-5 years': 4,
    '5-10 years': 7.5,
    '10-20 years': 15,
    '20+ years': 20,
}

mapped = data['For how many years have you been writing code and/or programming?'].map(years)# replace with equivalent experience
programming_data = mapped.dropna().values  # numpy array direct

plt.boxplot(programming_data)
plt.title('Programming Experience')
plt.ylabel('Years of Experience')
plt.xlabel('Respondents')
plt.savefig('/home/petru/Desktop/Ai/lab02-petruzdroba/charts/boxplot.png')
plt.clf()