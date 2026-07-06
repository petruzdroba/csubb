import csv
import os
import matplotlib
matplotlib.use('Qt5Agg')
import matplotlib.pyplot as plt
import numpy as np  
from sklearn import linear_model
import pandas as pd
from mpl_toolkits.mplot3d import Axes3D

def loadData(fileName, inputVariabName1, inputVariableName2, outputVariabName):
    df = pd.read_csv(fileName)
    df = df.dropna(subset=[inputVariabName1, inputVariableName2, outputVariabName])
    
    input1 = df[inputVariabName1].tolist()
    input2 = df[inputVariableName2].tolist()
    outputs = df[outputVariabName].tolist()
    
    inputs = [[input1[i], input2[i]] for i in range(len(input1))]
    return inputs, outputs

crtDir =  os.getcwd()
filePath = os.path.join(crtDir, 'data', 'v2_world-happiness-report-2017.csv')

inputs, outputs = loadData(filePath, 'Economy..GDP.per.Capita.', 'Freedom', 'Happiness.Score')
print('in:  ', inputs[:5])
print('out: ', outputs[:5])

np.random.seed(5)
indexes = [i for i in range(len(inputs))]
trainSample = np.random.choice(indexes, int(0.8 * len(inputs)), replace=False)
validationSample = [i for i in indexes if not i in trainSample]

trainInputs = [inputs[i] for i in trainSample]
trainOutputs = [outputs[i] for i in trainSample]

validationInputs = [inputs[i] for i in validationSample]
validationOutputs = [outputs[i] for i in validationSample]

trainGDP = [x[0] for x in trainInputs]
trainFreedom = [x[1] for x in trainInputs]
valGDP = [x[0] for x in validationInputs]
valFreedom = [x[1] for x in validationInputs]

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

ax.scatter(trainGDP, trainFreedom, trainOutputs, c='blue', marker='o', label='training data')
ax.scatter(valGDP, valFreedom, validationOutputs, c='magenta', marker='^', label='validation data')

ax.set_xlabel('GDP')
ax.set_ylabel('Freedom')
ax.set_zlabel('Happiness')
ax.set_title('train and validation data')
ax.legend()
plt.show()

xx = trainInputs  #  already 2D

# model initialisation
regressor = linear_model.LinearRegression()
regressor.fit(xx, trainOutputs)

w0 = regressor.intercept_
w1 = regressor.coef_[0]  # GDP weight
w2 = regressor.coef_[1]  # Freedom weight
print('the learnt model: f(x1, x2) = ', w0, ' + ', w1, ' * x1 + ', w2, ' * x2')

gdpRange = np.linspace(min(trainGDP), max(trainGDP), 50)
freedomRange = np.linspace(min(trainFreedom), max(trainFreedom), 50)
gdpGrid, freedomGrid = np.meshgrid(gdpRange, freedomRange)
happinessGrid = w0 + w1 * gdpGrid + w2 * freedomGrid

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.plot_surface(gdpGrid, freedomGrid, happinessGrid, alpha=0.5, color='magenta', label='learnt model')
ax.scatter(trainGDP, trainFreedom, trainOutputs, c='blue', marker='o', label='training data')
ax.set_xlabel('GDP')
ax.set_ylabel('Freedom')
ax.set_zlabel('Happiness')
ax.set_title('train data and learnt model')
ax.legend()
plt.show()

computedValidationOutputs = regressor.predict(validationInputs)  # already 2D, no wrapping

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.scatter(valGDP, valFreedom, computedValidationOutputs, c='indigo', marker='o', label='computed validation data')
ax.scatter(valGDP, valFreedom, validationOutputs, c='darkgreen', marker='^', label='real validation data')
ax.set_xlabel('GDP')
ax.set_ylabel('Freedom')
ax.set_zlabel('Happiness')
ax.set_title('computed vs real validation data')
ax.legend()
plt.show()

from sklearn.metrics import mean_squared_error, r2_score

error = mean_squared_error(validationOutputs, computedValidationOutputs)
print('MSE: ', error)

r2 = r2_score(validationOutputs, computedValidationOutputs)
print('R2 score: ', r2)