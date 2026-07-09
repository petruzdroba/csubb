import os
from dotenv import load_dotenv
from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import OperationStatusCodes
from msrest.authentication import CognitiveServicesCredentials
import time

load_dotenv()

subscription_key = os.environ["AZURE_KEY"]
endpoint = os.environ["AZURE_ENDPOINT"]

computervision_client = ComputerVisionClient(endpoint, CognitiveServicesCredentials(subscription_key))
time.sleep(3)

data1 = {
    "image" : "images/test1.png",
    "expected": "Google Cloud Platform",
    "actual_box": (176, 41, 416, 151)   # x1, y1, x2, y2 
}

data2 = {
    "image" : "images/test2.jpeg",
    "expected": "Succes în rezolvarea tEMELOR la LABORAtoarele de Inteligență Artificială!",
    "actual_box": (69, 297, 1447, 1360)   # x1, y1, x2, y2
}

img = open(data2["image"], "rb")
read_response = computervision_client.read_in_stream(
    image=img,
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
all_x = []
all_y = []

if read_result.status == OperationStatusCodes.succeeded:
    for text_result in read_result.analyze_result.read_results:
        for line in text_result.lines:
            result.append(line.text)
            bb = line.bounding_box
            all_x += [bb[0], bb[2], bb[4], bb[6]]
            all_y += [bb[1], bb[3], bb[5], bb[7]]

text = (' ').join(result)

from shapely.geometry import box

def compute_iou(result, expected):
    # procentace of matching in between bounding boxes
    # reunion of 2 rectangles 
    actual = box(expected[0], expected[1], expected[2], expected[3])
    
    detected = box(result[0], result[1], result[2], result[3])
    
    inter = actual.intersection(detected).area
    union = actual.union(detected).area
    
    if union == 0:
        return 0
    return inter / union

merged_detected = (min(all_x), min(all_y), max(all_x), max(all_y))
iou = compute_iou(merged_detected, data2["actual_box"])
print(f"IoU: {iou:.3f}")