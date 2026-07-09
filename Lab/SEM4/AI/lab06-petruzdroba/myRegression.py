class MyBGDRegression:
    
    def __init__(self):
        self.intercept = 0.0
        self.weights = []
        
    def fit(self, inputs, outputs, learning_rate=0.0001, max_iter=1000):
        self.weights = [0.0 for _ in range(len(inputs[0])+1)]
        n = len(inputs)
        
        for _ in range(max_iter):
            gradients = [0.0 for _ in range(len(inputs[0])+1)]
            
            for i in range(n):
                output_computed = self.eval(inputs[i])
                crtError = output_computed - outputs[i]
                
                for j in range(len(inputs[0])):
                    gradients[j] += crtError * inputs[i][j]
                gradients[len(inputs[0])] += crtError
                
            for j in range(len(self.weights)):
                self.weights[j] = self.weights[j] - learning_rate * (gradients[j]/n)
                
            self.intercept = self.weights[-1]
            
    def eval(self, input):
        output = self.weights[-1]
        for i in range(len(input)):
            output += self.weights[i] * input[i]
        
        return output
    
    def predict(self, inputs):
        return [self.eval(input) for input in inputs]

import math
    
class MyLogisticRegression:
    
    def __init__(self):
        self.intercept = 0.0
        self.weights = []
        self.classes = []
        
    def encode(self, outputs):
        self.classes = list(set(outputs))
        return [self.classes.index(o) for o in outputs]

    def decode(self, outputs):
        return [self.classes[o] for o in outputs]
    
    def fit(self, inputs, outputs, learning_rate=0.0001, max_iter=1000):
        encodedOutputs = self.encode(outputs)
        self.weights = [0.0 for _ in range(len(inputs[0]) + 1)]
        n = len(inputs)

        for _ in range(max_iter):
            gradients = [0.0 for _ in range(len(inputs[0]) + 1)]
            
            for i in range(n):
                output_computed = self.eval(inputs[i])
                crtError = output_computed - encodedOutputs[i]
                
                for j in range(len(inputs[0])):
                    gradients[j] += crtError * inputs[i][j]
                gradients[len(inputs[0])] += crtError
                
            for j in range(len(self.weights)):
                self.weights[j] = self.weights[j] - learning_rate * (gradients[j] / n)

        self.intercept = self.weights[-1]

    def eval(self, input):
        z = self.weights[-1]
        for i in range(len(input)):
            z += self.weights[i] * input[i]
            
        return 1 / (1 + math.exp(-z))

    def predict(self, inputs):
        return self.decode([1 if self.eval(input) >= 0.5 else 0 for input in inputs])