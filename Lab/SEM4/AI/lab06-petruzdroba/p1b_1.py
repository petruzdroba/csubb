import csv
import os
import matplotlib
matplotlib.use('Qt5Agg')
import matplotlib.pyplot as plt
import numpy as np  

from myRegression import MyBGDRegression

def loadData(fileName, inputVariabName, outputVariabName):
    data = []
    dataNames = []
    with open(fileName) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                dataNames = row
            else:
                data.append(row)
            line_count += 1
    selectedVariable = dataNames.index(inputVariabName)
    inputs = [float(data[i][selectedVariable]) for i in range(len(data))]
    selectedOutput = dataNames.index(outputVariabName)
    outputs = [float(data[i][selectedOutput]) for i in range(len(data))]
    
    return inputs, outputs

crtDir =  os.getcwd()
filePath = os.path.join(crtDir, 'data', '2017.csv')

inputs, outputs = loadData(filePath, 'Economy..GDP.per.Capita.', 'Happiness.Score')
print('in:  ', inputs[:5])
print('out: ', outputs[:5])

# training data set plit 80/20

np.random.seed(5)
indexes = [i for i in range(len(inputs))]
trainSample = np.random.choice(indexes, int(0.8 * len(inputs)), replace = False)
testSample = [i for i in indexes  if not i in trainSample]

trainInputs = [inputs[i] for i in trainSample]
trainOutputs = [outputs[i] for i in trainSample]

testInputs = [inputs[i] for i in testSample]
testOutputs = [outputs[i] for i in testSample]

plt.plot(trainInputs, trainOutputs, 'bo', label = 'training data')
plt.plot(testInputs, testOutputs, 'm^', label = 'testing data')
plt.title('train and test data')
plt.xlabel('GDP capita')
plt.ylabel('happiness')
plt.legend()
plt.show()

xx = [[el] for el in trainInputs]

# model initialisation
regressor = MyBGDRegression()

# training the model by using the training inputs and known training outputs
regressor.fit(xx, trainOutputs, learning_rate=0.0034, max_iter=1000)
# save the model parameters
w0, w1 = regressor.intercept, regressor.weights[0]
print('the learnt model: f(x) = ', w0, ' + ', w1, ' * x')


noOfPoints = 1000
xref = []
val = min(trainInputs)
step = (max(trainInputs) - min(trainInputs)) / noOfPoints
for i in range(1, noOfPoints):
    xref.append(val)
    val += step
yref = [w0 + w1 * el for el in xref] 

plt.plot(trainInputs, trainOutputs, 'bo', label = 'training data')
plt.plot(xref, yref, 'm-', label = 'learnt model')                 
plt.title('train data and the learnt model')
plt.xlabel('GDP capita')
plt.ylabel('happiness')
plt.legend()
plt.show()

computedTestOutputs = regressor.predict([[x] for x in testInputs])

plt.plot(testInputs, computedTestOutputs, 'yo', label = 'computed test data')
plt.plot(testInputs, testOutputs, 'g^', label = 'real test data') 
plt.title('computed test and real test data')
plt.xlabel('GDP capita')
plt.ylabel('happiness')
plt.legend()
plt.show()

validationSample = [i for i in indexes if not i in trainSample]
validationInputs = [inputs[i] for i in validationSample]
validationOutputs = [outputs[i] for i in validationSample]

error = sum((t1 - t2)**2 for t1, t2 in zip(computedTestOutputs, validationOutputs)) / len(validationOutputs)
print('prediction error (MSE): ', error)