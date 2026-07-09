import os
import matplotlib
matplotlib.use('Qt5Agg')
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from mpl_toolkits.mplot3d import Axes3D
from sklearn.linear_model import LogisticRegression

def loadData(fileName, inputCols, outputCol):
    df = pd.read_csv(fileName)
    df = df.dropna(subset=inputCols + [outputCol])
    inputs = df[inputCols].values.tolist()
    outputs = df[outputCol].tolist()
    return inputs, outputs

crtDir = os.getcwd()
filePath = os.path.join(crtDir, 'data', 'iris.csv')
inputCols = ['Sepal.Length', 'Sepal.Width', 'Petal.Length', 'Petal.Width']
inputs, outputs = loadData(filePath, inputCols, 'Class')
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

classes = list(set(outputs))
colors = ['red', 'blue', 'green']
computedColors = ['orange', 'cyan', 'lime']

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
for label, color in zip(classes, colors):
    idx = [i for i in range(len(trainOutputs)) if trainOutputs[i] == label]
    ax.scatter([trainInputs[i][2] for i in idx],
               [trainInputs[i][3] for i in idx],
               [trainInputs[i][0] for i in idx],
               c=color, label=label, marker='o')
ax.set_xlabel('Petal Length')
ax.set_ylabel('Petal Width')
ax.set_zlabel('Sepal Length')
ax.set_title('train data')
ax.legend()
plt.show()

xx = trainInputs
classifier = LogisticRegression(max_iter=1000)
classifier.fit(xx, trainOutputs)

computedValidationOutputs = classifier.predict(validationInputs)

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
for label, color, compColor in zip(classes, colors, computedColors):
    idx = [i for i in range(len(validationOutputs)) if validationOutputs[i] == label]
    ax.scatter([validationInputs[i][2] for i in idx],
               [validationInputs[i][3] for i in idx],
               [validationInputs[i][0] for i in idx],
               c=color, label=f'real {label}', marker='o')
    idx2 = [i for i in range(len(computedValidationOutputs)) if computedValidationOutputs[i] == label]
    ax.scatter([validationInputs[i][2] for i in idx2],
               [validationInputs[i][3] for i in idx2],
               [validationInputs[i][0] for i in idx2],
               c=compColor, label=f'computed {label}', marker='^', alpha=0.8)
ax.set_xlabel('Petal Length')
ax.set_ylabel('Petal Width')
ax.set_zlabel('Sepal Length')
ax.set_title('computed vs real validation data')
ax.legend()
plt.show()

from sklearn.metrics import accuracy_score
accuracy = accuracy_score(validationOutputs, computedValidationOutputs)
print('Accuracy: ', accuracy)

print(classifier.predict([[5.35, 3.85, 1.25, 0.4]]))

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')




# plot train data
for label, color in zip(classes, colors):
    idx = [i for i in range(len(trainOutputs)) if trainOutputs[i] == label]
    ax.scatter([trainInputs[i][2] for i in idx],
               [trainInputs[i][3] for i in idx],
               [trainInputs[i][0] for i in idx],
               c=color, label=label, marker='o')

# decision boundary planes
petalLengthRange = np.linspace(min(t[2] for t in trainInputs), max(t[2] for t in trainInputs), 30)
petalWidthRange = np.linspace(min(t[3] for t in trainInputs), max(t[3] for t in trainInputs), 30)
petalLengthGrid, petalWidthGrid = np.meshgrid(petalLengthRange, petalWidthRange)

for idx_class, color in enumerate(colors):
    w = classifier.coef_[idx_class]      # w1, w2, w3, w4
    b = classifier.intercept_[idx_class] # w0
    # solve for sepal length (feature 0): w0 + w1*x1 + w2*x2 + w3*x3 + w4*x4 = 0
    # x1 (sepal length) = -(b + w[1]*sepalWidth_mean + w[2]*petalLength + w[3]*petalWidth) / w[0]
    sepalWidthMean = np.mean([t[1] for t in trainInputs])
    sepalLengthGrid = -(b + w[1] * sepalWidthMean + w[2] * petalLengthGrid + w[3] * petalWidthGrid) / w[0]
    ax.plot_surface(petalLengthGrid, petalWidthGrid, sepalLengthGrid, alpha=0.3, color=color)

ax.set_xlabel('Petal Length')
ax.set_ylabel('Petal Width')
ax.set_zlabel('Sepal Length')
ax.set_title('train data and decision boundaries')
ax.legend()
plt.show()