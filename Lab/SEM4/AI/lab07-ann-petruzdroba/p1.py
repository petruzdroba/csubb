from PIL import Image
import os
import numpy as np
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt 

def load_data(input_folder="images", path_1="wo_sepia", path_2="sepia", img_size=(32, 32)):
    image_data = [] # pixel data for each image
    label = []

    folder_1 = os.path.join(input_folder, path_1)
    for image in os.listdir(folder_1):
        if image.lower().endswith(('.jpg', '.jpeg', '.png')):
            img = Image.open(os.path.join(folder_1, image)).convert("RGB")
            img = img.resize(img_size)
            image_data.append(np.array(img).flatten())
            label.append(1)

    folder_2 = os.path.join(input_folder, path_2)
    for image in os.listdir(folder_2):
        if image.lower().endswith(('.jpg', '.jpeg', '.png')):
            img = Image.open(os.path.join(folder_2, image)).convert("RGB")
            img = img.resize(img_size)
            image_data.append(np.array(img).flatten())
            label.append(0)

    return np.array(image_data), np.array(label)

def normalize(X):
    return X / 255.0

def split_data(X, y, test_size=0.2, random_state=42):
    return train_test_split(X, y, test_size=test_size, random_state=random_state)


data, label = load_data()
data = normalize(data)
data_train, data_test, label_train, label_test = split_data(data, label)


from sklearn.neural_network import MLPClassifier
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix, ConfusionMatrixDisplay

def build_model():
    return MLPClassifier(
        hidden_layer_sizes=(128, 64),
        activation='relu',
        solver='adam',
        max_iter=100,
        random_state=42,
        learning_rate_init=0.001,
        verbose=True
    )

def train_model(model, X_train, y_train):
    model.fit(X_train, y_train)
    return model

def evaluate_model(model, data_test, label_test):
    label_prediction = model.predict(data_test)
    print(f"Accuracy: {accuracy_score(label_test, label_prediction) * 100:.2f}%")
    print(classification_report(label_test, label_prediction, target_names=['no sepia', 'sepia']))

    ConfusionMatrixDisplay(
    confusion_matrix = confusion_matrix(
        label_test, label_prediction
    )).plot(cmap=plt.cm.Blues)

    plt.show()

    print(classification_report(label_test, label_prediction))

model = build_model()
model = train_model(model, data_train, label_train)
evaluate_model(model, data_test, label_test)