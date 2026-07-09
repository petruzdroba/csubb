import os
from dotenv import load_dotenv
from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import OperationStatusCodes
from msrest.authentication import CognitiveServicesCredentials
from PIL import Image, ImageEnhance, ImageOps
import io
import time

load_dotenv()
subscription_key = os.environ["AZURE_KEY"]
endpoint = os.environ["AZURE_ENDPOINT"]
computervision_client = ComputerVisionClient(endpoint, CognitiveServicesCredentials(subscription_key))
time.sleep(3)

data1 = {
    "image": "images/test1.png",
    "expected": "Google Cloud Platform"
}
data2 = {
    "image": "images/test2.jpeg",
    "expected": "Succes în rezolvarea tEMELOR la LABORAtoarele de Inteligență Artificială!"
}

def preprocess_image(image_path: str):
    img = Image.open(image_path)

    img = ImageOps.grayscale(img)

    enhancer = ImageEnhance.Contrast(img)
    img = enhancer.enhance(2.5)

    buffer = io.BytesIO()
    img_format = "PNG" if image_path.lower().endswith(".png") else "JPEG"
    img.save(buffer, format=img_format)
    buffer.seek(0)

    return buffer

processed_image = preprocess_image(data2["image"])

read_response = computervision_client.read_in_stream(
    image=processed_image,
    mode="Printed",
    raw=True
)

operation_id = read_response.headers['Operation-Location'].split('/')[-1]

while True:
    read_result = computervision_client.get_read_result(operation_id)
    if read_result.status not in ['notStarted', 'running']:
        break
    time.sleep(1)

result = []
if read_result.status == OperationStatusCodes.succeeded:
    for text_result in read_result.analyze_result.read_results:
        for line in text_result.lines:
            result.append(line.text)

text = ' '.join(result)
import Levenshtein

def compute_cer(result, expected):
    # .distance: number of errors in chars Char Error Rate
    # number of errors / length == % of wrong chars
    return Levenshtein.distance(result, expected) / len (expected)

def compute_wer(result, expected):
    # Word Error Rate
    # number of wrong words / nr of words == % of wrong words
    return Levenshtein.distance(result, expected) / len (expected.split(' '))

def compute_hamming(result, expected):
    # number of positions where characters arent the same
    error_count = 0
    for l1, l2 in zip(result, expected):
        error_count += (l1 != l2)
    return error_count

def compute_jaccard(result, expected):
    # % of words overlapping sets, 1- identical 0 - no words match
    set1 = set(result.split())
    set2 = set(expected.split())
    return len(set1.intersection(set2)) / len(set1.union(set2))

def compute_jaro_winkler(result, expected):
    # similarity + bonux for beginning match
    return Levenshtein.jaro_winkler(result, expected)

print(f"CER: {compute_cer(text, data2["expected"]):.3f}")
print(f"WER: {compute_wer(text, data2["expected"]):.3f}")
print(f"Hamming distance: {compute_hamming(text, data2["expected"])}")
print(f"Jaccard similarity: {compute_jaccard(text, data2["expected"]):.3f}")
print(f"Jaro-Winkler: {compute_jaro_winkler(text, data2["expected"]):.3f}")