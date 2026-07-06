import os
import matplotlib
matplotlib.use('Qt5Agg')
import matplotlib.pyplot as plt
import numpy as np  
import pandas as pd
from sklearn.linear_model import LogisticRegression

def loadData(fileName, inputVariabName1, inputVariableName2, outputVariabName):
    df = pd.read_csv(fileName)
    df = df.dropna(subset=[inputVariabName1, inputVariableName2, outputVariabName])
    input1 = df[inputVariabName1].tolist()
    input2 = df[inputVariableName2].tolist()
    outputs = df[outputVariabName].tolist()
    inputs = [[input1[i], input2[i]] for i in range(len(input1))]
    return inputs, outputs

crtDir = os.getcwd()
filePath = os.path.join(crtDir, 'data', 'wdbc.csv')
inputs, outputs = loadData(filePath, 'Radius_Mean', 'Texture_Mean', 'Diagnosis')
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

trainRadius = [x[0] for x in trainInputs]
trainTexture = [x[1] for x in trainInputs]
valRadius = [x[0] for x in validationInputs]
valTexture = [x[1] for x in validationInputs]

for label, color in [('Malignant', 'red'), ('Benign', 'blue')]:
    idx = [i for i in range(len(trainOutputs)) if trainOutputs[i] == label]
    plt.scatter([trainRadius[i] for i in idx], [trainTexture[i] for i in idx], c=color, label=label)
plt.xlabel('Radius')
plt.ylabel('Texture')
plt.legend()
plt.show()

xx = trainInputs
classifier = LogisticRegression()
classifier.fit(xx, trainOutputs)
w0 = classifier.intercept_[0]
w1 = classifier.coef_[0][0]
w2 = classifier.coef_[0][1]
print('the learnt model: f(x1, x2) = ', w0, ' + ', w1, ' * x1 + ', w2, ' * x2')

for label, color in [('M', 'red'), ('B', 'blue')]:
    idx = [i for i in range(len(trainOutputs)) if trainOutputs[i] == label]
    plt.scatter([trainRadius[i] for i in idx], [trainTexture[i] for i in idx], c=color, label=label)

radiusRange = np.linspace(min(trainRadius), max(trainRadius), 100)
boundary = -(w0 + w1 * radiusRange) / w2
plt.plot(radiusRange, boundary, 'k-', label='decision boundary')
plt.xlabel('Radius')
plt.ylabel('Texture')
plt.legend()
plt.show()

computedValidationOutputs = classifier.predict(validationInputs)

for label, color in [('M', 'red'), ('B', 'blue')]:
    idx = [i for i in range(len(validationOutputs)) if validationOutputs[i] == label]
    plt.scatter([valRadius[i] for i in idx], [valTexture[i] for i in idx], c=color, label=f'real {label}')
for label, color in [('M', 'orange'), ('B', 'cyan')]:
    idx = [i for i in range(len(computedValidationOutputs)) if computedValidationOutputs[i] == label]
    plt.scatter([valRadius[i] for i in idx], [valTexture[i] for i in idx], c=color, marker='^', label=f'computed {label}')
plt.xlabel('Radius')
plt.ylabel('Texture')
plt.legend()
plt.show()

from sklearn.metrics import accuracy_score
accuracy = accuracy_score(validationOutputs, computedValidationOutputs)
print('Accuracy: ', accuracy)
print(classifier.predict([[18, 10]]))