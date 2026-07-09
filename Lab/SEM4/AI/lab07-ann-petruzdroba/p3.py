from PIL import Image
import os
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix, ConfusionMatrixDisplay
import matplotlib.pyplot as plt
from myCNN import myCNN

def load_data_cnn(input_folder="images", img_size=(32, 32)):
    X, y = [], []
    for image_name in os.listdir(os.path.join(input_folder, 'sepia')):
        if image_name.lower().endswith(('.jpg', '.jpeg', '.png')):
            img = Image.open(os.path.join(input_folder, 'sepia', image_name)).convert("RGB")
            img = img.resize(img_size)
            X.append(np.array(img) / 255.0)
            y.append(0)
    for image_name in os.listdir(os.path.join(input_folder, 'wo_sepia')):
        if image_name.lower().endswith(('.jpg', '.jpeg', '.png')):
            img = Image.open(os.path.join(input_folder, 'wo_sepia', image_name)).convert("RGB")
            img = img.resize(img_size)
            X.append(np.array(img) / 255.0)
            y.append(1)
    return np.array(X), np.array(y)

def build_model():
    return myCNN(input_shape=(32, 32, 3), kernel_size=3, pool_size=2, random_state=9284)

def train_model(model, X_train, y_train):
    model.fit(X_train, y_train, epochs=55, learning_rate=0.0002)
    return model

def evaluate_model(model, X_test, y_test):
    y_pred = model.predict(X_test)
    print(f"Accuracy: {accuracy_score(y_test, y_pred) * 100:.2f}%")
    print(classification_report(y_test, y_pred, target_names=['no sepia', 'sepia']))
    ConfusionMatrixDisplay(
        confusion_matrix=confusion_matrix(y_test, y_pred)
    ).plot(cmap=plt.cm.Blues)
    plt.show()

X, y = load_data_cnn()
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

model = build_model()
model = train_model(model, X_train, y_train)
evaluate_model(model, X_test, y_test)