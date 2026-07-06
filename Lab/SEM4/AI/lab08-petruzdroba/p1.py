from utils import Utils
from extractors import BagOfWordsExtractor, TfidfExtractor, Word2VecExtractor, CustomExtractor

import numpy as np
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import accuracy_score, confusion_matrix
from sklearn.preprocessing import LabelEncoder

from sklearn.metrics import accuracy_score, confusion_matrix, ConfusionMatrixDisplay
import matplotlib.pyplot as plt

def train(model, encoder, trainInputs, trainOutputs):
    trainOutputsEncoded = encoder.fit_transform(trainOutputs)
    model.fit(trainInputs, trainOutputsEncoded)

def predict(model, encoder, testInputs):
    predicted = model.predict(testInputs)
    return encoder.inverse_transform(predicted)

def predict_with_confidence(model, encoder, testInputs):
    predicted = model.predict(testInputs)
    probabilities = model.predict_proba(testInputs)
    predicted_classes = encoder.inverse_transform(predicted)
    confidences = np.max(probabilities, axis=1)
    return predicted_classes, confidences

def evaluate(model, encoder, testFeatures, testOutputs):
    predictions = predict(model, encoder, testFeatures)

    acc = accuracy_score(testOutputs, predictions)
    cm = confusion_matrix(testOutputs, predictions, labels=encoder.classes_)

    print(f"Accuracy: {acc:.2f}")

    disp = ConfusionMatrixDisplay(confusion_matrix=cm, display_labels=encoder.classes_)
    disp.plot(cmap=plt.cm.Blues)
    plt.title("Confusion Matrix")
    plt.show()

    return acc, cm

texts, labels = Utils.loadData("data/ISEAR.csv")
texts = Utils.cleanData(texts)
trainInputs, testInputs, trainOutputs, testOutputs = Utils.splitData(texts, labels)

# extractor = BagOfWordsExtractor()
# extractor = TfidfExtractor()
# extractor = Word2VecExtractor()
extractor = CustomExtractor()

trainFeatures = extractor.fit_transform(trainInputs)
testFeatures = extractor.transform(testInputs)

model = MLPClassifier(hidden_layer_sizes=(32, 16), max_iter=1000, random_state=42)
encoder = LabelEncoder()

train(model, encoder, trainFeatures, trainOutputs)
evaluate(model, encoder, testFeatures, testOutputs)

phrase = "By choosing a bike over a car, I’m reducing my environmental footprint. Cycling promotes eco-friendly transportation, and I’m proud to be part of that movement."
phraseFeatures = extractor.transform([phrase])
prediction, confidence = predict_with_confidence(model, encoder, phraseFeatures)
print(f"\nPhrase: '{phrase}'")
print(f"Predicted sentiment: {prediction[0]}")
print(f"Confidence: {confidence[0]:.4f}")