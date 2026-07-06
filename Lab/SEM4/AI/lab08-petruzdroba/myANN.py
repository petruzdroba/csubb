import numpy as np

class myANN:
    def __init__(
        self,
        hidden_size1,
        hidden_size2,
        output_size,
        input_size=3072,
        learning_rate=0.0001,
        epochs=100,
        verbose=True,
    ):
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.verbose = verbose
        self.loss_list = []
        self.b1 = np.zeros((1, hidden_size1))
        self.b2 = np.zeros((1, hidden_size2))
        self.b3 = np.zeros((1, output_size))
        self.W1 = np.random.randn(input_size, hidden_size1) * np.sqrt(2 / input_size)
        self.W2 = np.random.randn(hidden_size1, hidden_size2) * np.sqrt(2 / hidden_size1)
        self.W3 = np.random.randn(hidden_size2, output_size) * np.sqrt(2 / hidden_size2)

    def fit(self, data, y):
        batch_size = 32

        for epoch in range(self.epochs):
            indices = np.random.permutation(data.shape[0])
            data_shuffled = data[indices]
            y_shuffled = y[indices]

            for i in range(0, data.shape[0], batch_size):
                data_batch = data_shuffled[i: i + batch_size]
                y_batch = y_shuffled[i: i + batch_size]

                # forward
                z1 = np.dot(data_batch, self.W1) + self.b1
                a1 = relu(z1)
                z2 = np.dot(a1, self.W2) + self.b2
                a2 = relu(z2)
                z3 = np.dot(a2, self.W3) + self.b3
                a3 = softmax(z3)

                # back
                dz3 = a3 - y_batch
                dW3 = np.dot(a2.T, dz3)
                db3 = np.sum(dz3, axis=0, keepdims=True)
                dz2 = np.dot(dz3, self.W3.T) * relu_deriv(a2)
                dW2 = np.dot(a1.T, dz2)
                db2 = np.sum(dz2, axis=0, keepdims=True)
                dz1 = np.dot(dz2, self.W2.T) * relu_deriv(a1)
                dW1 = np.dot(data_batch.T, dz1)
                db1 = np.sum(dz1, axis=0, keepdims=True)

                self.W3 -= self.learning_rate * dW3
                self.b3 -= self.learning_rate * db3
                self.W2 -= self.learning_rate * dW2
                self.b2 -= self.learning_rate * db2
                self.W1 -= self.learning_rate * dW1
                self.b1 -= self.learning_rate * db1

            # compute loss on full dataset
            z1 = np.dot(data, self.W1) + self.b1
            a1 = relu(z1)
            z2 = np.dot(a1, self.W2) + self.b2
            a2 = relu(z2)
            z3 = np.dot(a2, self.W3) + self.b3
            a3 = softmax(z3)

            loss = -np.mean(np.sum(y * np.log(a3 + 1e-9), axis=1))
            self.loss_list.append(loss)

            if self.verbose:
                print(f"Epoch {epoch} -> Loss: {loss:.6f}")

    def predict(self, data):
        z1 = np.dot(data, self.W1) + self.b1
        a1 = relu(z1)
        z2 = np.dot(a1, self.W2) + self.b2
        a2 = relu(z2)
        z3 = np.dot(a2, self.W3) + self.b3
        a3 = softmax(z3)

        return np.argmax(a3, axis=1)

    def predict_proba(self, data):
        z1 = np.dot(data, self.W1) + self.b1
        a1 = relu(z1)
        z2 = np.dot(a1, self.W2) + self.b2
        a2 = relu(z2)
        z3 = np.dot(a2, self.W3) + self.b3
        a3 = softmax(z3)
        return a3


def relu(x):
    return np.maximum(0, x)

def relu_deriv(x):
    return (x > 0).astype(float)

def sigmoid(x):
    return 1 / (1 + np.exp(-x))

def sigmoid_deriv(x):
    return x * (1 - x)

def softmax(x):
    exp_x = np.exp(x - np.max(x, axis=1, keepdims=True))
    return exp_x / np.sum(exp_x, axis=1, keepdims=True)