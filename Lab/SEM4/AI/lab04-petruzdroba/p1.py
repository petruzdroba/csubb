import os
from dotenv import load_dotenv
from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import VisualFeatureTypes
from msrest.authentication import CognitiveServicesCredentials

load_dotenv()

subscription_key = os.environ["AZURE_KEY"]
endpoint = os.environ["AZURE_ENDPOINT"]

computervision_client = ComputerVisionClient(endpoint, CognitiveServicesCredentials(subscription_key))

test_images = [
    ("images/bike01.jpg", 1),
    ("images/bike02.jpg", 1),
    ("images/bike03.jpg", 1),
    ("images/bike04.jpg", 1),
    ("images/bike05.jpg", 1),
    ("images/bike06.jpg", 1),
    ("images/bike07.jpg", 1),
    ("images/bike08.jpg", 1),
    ("images/bike09.jpg", 1),
    ("images/bike10.jpg", 1),
    ("images/traffic01.jpg", 0),
    ("images/traffic02.jpg", 0),
    ("images/traffic03.jpg", 0),
    ("images/traffic04.jpg", 0),
    ("images/traffic05.jpg", 0),
    ("images/traffic06.jpg", 0),
    ("images/traffic07.jpg", 0),
    ("images/traffic08.jpg", 0),
    ("images/traffic09.jpg", 0),
    ("images/traffic10.jpg", 0),
]

def classify_image(image_path, threshold=0.5):
    img = open(image_path, "rb")
    result = computervision_client.analyze_image_in_stream(
        img,
        visual_features=[VisualFeatureTypes.tags]
    )
    
    for tag in result.tags:
        if tag.name in ("bicycle", "bike", "cycling") and tag.confidence >= threshold:
            print(f"Bicycle detected in {image_path} with confidence: {tag.confidence:.3f}")
            return 1
    
    return 0

predicted = []
truth = []

for image, actual in test_images:
    result = classify_image(image)
    truth.append(actual)
    predicted.append(result)
    
TP = sum(1 for t, p in zip(truth, predicted) if t == 1 and p == 1) # yes bike, azure yes, True Positive
TN = sum(1 for t, p in zip(truth, predicted) if t == 0 and p == 0) # no bike, azure no, True Negativve
FP = sum(1 for t, p in zip(truth, predicted) if t == 0 and p == 1) # no bike, azure yes, False Positive (false alarm)
FN = sum(1 for t, p in zip(truth, predicted) if t == 1 and p == 0) # yes bike, azure no, False negative

accuracy  = (TP + TN) / len(truth) # no. of correct guesses out of all
precision = TP / (TP + FP) if (TP + FP) > 0 else 0 # out of all that was predicted bike, how many were bikes
recall    = TP / (TP + FN) if (TP + FN) > 0 else 0 # out of all bikes how many we caught

print(f"Accuracy:  {accuracy:.3f}")
print(f"Precision: {precision:.3f}")
print(f"Recall:    {recall:.3f}")