class MyLinearUnivariateRegression:
    
    def __init__(self):
        self.w0 = 0.0
        self.w1 = 0.0
    
    def fit(self, inputs, outputs):
        sum_inputs = sum(inputs)
        sum_outputs = sum(outputs)
        
        sum_inputs_2 = sum(input**2 for input in inputs)
        sum_input_output = sum(input*output for input,output in zip(inputs,outputs))
        
        n = len(inputs)
        w1 = (n*sum_input_output - sum_inputs*sum_outputs)/(n*sum_inputs_2 - sum_inputs*sum_inputs)
        w0 = (sum_outputs - w1*sum_inputs)/n
        
        self.w0, self.w1 = w0, w1
        
    def predict(self, input):
        if (isinstance(input[0], list)):
            return [self.w0 + self.w1 * val[0] for val in input]
        else:
            return [self.w0 + self.w1 * val for val in input]
    
    
class MyLinearBivariateRegression:
    def __init__(self):
        self.w0 = 0.0
        self.w1 = 0.0
        self.w2 = 0.0
    
    def get_transpose_matrix(self, matrix):
        transpose = [[0] * len(matrix) for _ in range(len(matrix[0]))]
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                transpose[j][i] = matrix[i][j]
        return transpose

    def get_determinant_matrix(self, matrix):
        if len(matrix) == 2 and len(matrix[0]) == 2:
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        
        det = 0
        for count in range(len(matrix)):
            det += (((-1) ** count) * matrix[0][count] * 
                    self.get_determinant_matrix(
                        [x[:count] + x[count + 1:] for x in matrix[1:]])
                    )
        
        return det


    def get_inverse_matrix(self, matrix):
        det = self.get_determinant_matrix(matrix)
        if det == 0:
            return None
        if len(matrix) != len(matrix[0]):
            return None
        
        if len(matrix) == 2:
            return [
                [matrix[1][1] / det, -1 * matrix[0][1] / det],
                [-1 * matrix[1][0] / det, matrix[0][0] / det]
            ]
        
        inverse = [[0] * len(matrix) for _ in range(len(matrix[0]))]
        for row in range(len(matrix)):
            for col in range(len(matrix)):
                inverse[row][col] += (((-1) ** (row + col)) * 
                                    self.get_determinant_matrix(
                                        [[matrix[i][j] for j in range(len(matrix)) if j != col]
                                                        for i in range(len(matrix)) if i != row]
                                        )
                                    )
        inverse = self.get_transpose_matrix(inverse)
        return [
            [y / det for y in row] for row in inverse
        ]

    def get_produce_matrix(self, matrix1, matrix2):
        if len(matrix1[0]) != len(matrix2):
            return None
        
        produce = [[0] * len(matrix2[0]) for _ in range(len(matrix1))]
        for row in range(len(matrix1)):
            for col1 in range(len(matrix2[0])):
                for col2 in range(len(matrix1[0])):
                    produce[row][col1] += matrix1[row][col2] * matrix2[col2][col1]
                    
        return produce
    
    def fit(self, inputs, outputs):
        X = [[1, sample[0], sample[1]] for sample in inputs]
        y = [[val] for val in outputs]
        
        Xt = self.get_transpose_matrix(X)
        XtX = self.get_produce_matrix(Xt, X)
        XtX_inv = self.get_inverse_matrix(XtX)
        Xty = self.get_produce_matrix(Xt, y)
        W = self.get_produce_matrix(XtX_inv, Xty)
        
        self.w0 = W[0][0]
        self.w1 = W[1][0]
        self.w2 = W[2][0]
        
    def predict(self, inputs):
        return [self.w0 + self.w1 * sample[0] + self.w2 * sample[1] for sample in inputs]