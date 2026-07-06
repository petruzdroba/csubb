import numpy as np

class myCNN:
    def __init__(self, input_shape, kernel_size=3, pool_size=2, random_state=42):
        np.random.seed(random_state)
        self.input_shape = input_shape
        self.kernel_size = kernel_size
        self.pool_size = pool_size
        self.kernel = np.random.randn(kernel_size, kernel_size, input_shape[2]) * 0.01
        self.bias_conv = 0.0
        conv_h = input_shape[0] - kernel_size + 1
        conv_w = input_shape[1] - kernel_size + 1
        pooled_h = conv_h // pool_size
        pooled_w = conv_w // pool_size
        self.fc_input_dim = pooled_h * pooled_w
        self.fc_weights = np.random.randn(self.fc_input_dim, 1) * 0.01
        self.fc_bias = 0.0
        self.loss_history = []
        self.accuracy_history = []

    def convolve(self, image):
        kh = self.kernel_size
        h, w, _ = image.shape
        out_h = h - kh + 1
        out_w = w - kh + 1
        patches = np.array([
            image[i:i+kh, j:j+kh, :]
            for i in range(out_h)
            for j in range(out_w)
        ])
        out = np.dot(patches.reshape(out_h * out_w, -1),
                     self.kernel.reshape(-1)) + self.bias_conv
        return out.reshape(out_h, out_w)

    def max_pool(self, feature_map):
        h, w = feature_map.shape
        ph = pw = self.pool_size
        out_h, out_w = h // ph, w // pw
        pooled = feature_map[:out_h*ph, :out_w*pw]\
            .reshape(out_h, ph, out_w, pw)\
            .max(axis=(1, 3))
        return pooled

    def forward(self, image):
        self.input = image
        self.conv_out = self.convolve(image)
        self.relu_out = relu(self.conv_out)
        self.pooled = self.max_pool(self.relu_out)
        self.flattened = self.pooled.flatten()
        self.fc_z = np.dot(self.flattened, self.fc_weights) + self.fc_bias
        self.output = sigmoid(self.fc_z).flatten()[0]
        return self.output

    def backward(self, y_true, learning_rate=0.01):
        d_loss = float(self.output - y_true)
        d_loss = np.clip(d_loss, -1, 1)

        # fc layer
        d_fc_weights = self.flattened[:, np.newaxis] * d_loss
        d_fc_bias = d_loss
        self.fc_weights -= learning_rate * d_fc_weights
        self.fc_bias -= learning_rate * d_fc_bias

        # backprop through flatten
        d_flat = (self.fc_weights * d_loss).flatten()
        d_pooled = d_flat.reshape(self.pooled.shape)

        # backprop through max pool
        ph = pw = self.pool_size
        d_relu = np.zeros_like(self.relu_out)
        for i in range(self.pooled.shape[0]):
            for j in range(self.pooled.shape[1]):
                region = self.relu_out[i*ph:(i+1)*ph, j*pw:(j+1)*pw]
                max_val = np.max(region)
                for di in range(ph):
                    for dj in range(pw):
                        if region[di, dj] == max_val:
                            d_relu[i*ph+di, j*pw+dj] = d_pooled[i, j]

        # backprop through relu
        d_conv = d_relu * relu_deriv(self.conv_out)

        # backprop through conv to kernel
        kh = self.kernel_size
        d_kernel = np.zeros_like(self.kernel)
        for i in range(d_conv.shape[0]):
            for j in range(d_conv.shape[1]):
                d_kernel += self.input[i:i+kh, j:j+kh, :] * d_conv[i, j]
        d_bias_conv = np.sum(d_conv)

        d_kernel = np.clip(d_kernel, -0.1, 0.1)
        d_fc_weights = np.clip(d_fc_weights, -0.1, 0.1)

        self.kernel -= learning_rate * d_kernel
        self.bias_conv -= learning_rate * d_bias_conv

    def fit(self, X, y, epochs=10, learning_rate=0.01):
        for epoch in range(epochs):
            losses = []
            correct = 0
            for i in range(len(X)):
                output = self.forward(X[i])
                loss = binary_cross_entropy(y[i], output)
                losses.append(loss)
                prediction = int(output > 0.5)
                correct += (prediction == y[i])
                self.backward(y[i], learning_rate)
            avg_loss = np.mean(losses)
            accuracy = correct / len(X)
            self.loss_history.append(avg_loss)
            self.accuracy_history.append(accuracy)
            print(f"Epoch {epoch+1}: Loss = {avg_loss:.4f}, Accuracy = {accuracy*100:.2f}%")
            print(f"kernel mean: {np.mean(self.kernel):.6f}, fc_weights mean: {np.mean(self.fc_weights):.6f}")

    def predict(self, X):
        predictions = []
        for i in range(len(X)):
            output = self.forward(X[i])
            predictions.append(int(output > 0.5))
        return np.array(predictions)


def sigmoid(x):
    return 1 / (1 + np.exp(-x))

def sigmoid_deriv(x):
    return x * (1 - x)

def relu(x):
    return np.maximum(0, x)

def relu_deriv(x):
    return (x > 0).astype(float)

def binary_cross_entropy(y_true, y_pred):
    eps = 1e-9
    return -np.mean(y_true * np.log(y_pred + eps) + (1 - y_true) * np.log(1 - y_pred + eps))