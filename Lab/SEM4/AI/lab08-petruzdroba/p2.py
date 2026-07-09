from utils import Utils
from extractors import BagOfWordsExtractor, TfidfExtractor, Word2VecExtractor, CustomExtractor
import numpy as np
from sklearn.metrics import accuracy_score, confusion_matrix, ConfusionMatrixDisplay
from sklearn.preprocessing import LabelEncoder, OneHotEncoder
import matplotlib.pyplot as plt
from myANN import myANN

def train(model, encoder, trainFeatures, trainOutputs):
    trainOutputsEncoded = encoder.fit_transform(np.array(trainOutputs).reshape(-1, 1))
    model.fit(trainFeatures, trainOutputsEncoded)

def predict(model, encoder, testFeatures):
    predictedIndices = model.predict(testFeatures)
    return encoder.categories_[0][predictedIndices]

def predict_with_confidence(model, encoder, testFeatures):
    predictedIndices = model.predict(testFeatures)
    probabilities = model.predict_proba(testFeatures)
    predicted_classes = encoder.categories_[0][predictedIndices]
    confidences = np.max(probabilities, axis=1)
    return predicted_classes, confidences

def evaluate(model, encoder, testFeatures, testOutputs):
    predictions = predict(model, encoder, testFeatures)
    acc = accuracy_score(testOutputs, predictions)
    cm = confusion_matrix(testOutputs, predictions, labels=encoder.categories_[0])
    print(f"Accuracy: {acc:.2f}")
    disp = ConfusionMatrixDisplay(confusion_matrix=cm, display_labels=encoder.categories_[0])
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

encoder = OneHotEncoder(sparse_output=False)

model = myANN(
    input_size=trainFeatures.shape[1],
    hidden_size1=32,
    hidden_size2=16,
    output_size=7,
    learning_rate=0.0001,
    epochs=100
)

train(model, encoder, trainFeatures, trainOutputs)
evaluate(model, encoder, testFeatures, testOutputs)

phrase = "By choosing a bike over a car, I’m reducing my environmental footprint. Cycling promotes eco-friendly transportation, and I’m proud to be part of that movement."
phraseFeatures = extractor.transform([phrase])
prediction, confidence = predict_with_confidence(model, encoder, phraseFeatures)
print(f"\nPhrase: '{phrase}'")
print(f"Predicted sentiment: {prediction[0]}")
print(f"Confidence: {confidence[0]:.4f}")