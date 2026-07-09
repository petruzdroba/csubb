import os
from dotenv import load_dotenv
from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import VisualFeatureTypes
from msrest.authentication import CognitiveServicesCredentials
from PIL import Image, ImageDraw
import matplotlib
matplotlib.use('Qt5Agg')
import matplotlib.pyplot as plt
from shapely.geometry import box

load_dotenv()
subscription_key = os.environ["AZURE_KEY"]
endpoint = os.environ["AZURE_ENDPOINT"]
computervision_client = ComputerVisionClient(endpoint, CognitiveServicesCredentials(subscription_key))

bike_coords = {
    "./images/bike01.jpg": [(8, 20, 400, 408)],
    "./images/bike02.jpg": [(30, 83, 387, 323)],
    "./images/bike03.jpg": [(76, 144, 198, 388), (167, 151, 342, 394)],
    "./images/bike04.jpg": [(5, 4, 412, 412)],
    "./images/bike05.jpg": [(74, 57, 353, 342)],
    "./images/bike06.jpg": [(76, 144, 198, 388), (167, 151, 342, 394)],
    "./images/bike07.jpg": [(64, 217, 295, 414)],
    "./images/bike08.jpg": [(55, 39, 380, 350)],
    "./images/bike09.jpg": [(18, 14, 376, 387)],
    "./images/bike10.jpg": [(149, 138, 371, 405)],
}

def process_image(image_path, ground_truth_boxes):
    img_file = open(image_path, "rb")
    result = computervision_client.analyze_image_in_stream(
        img_file,
        visual_features=[VisualFeatureTypes.objects]
    )

    predicted_boxes = []
    for ob in result.objects:
        if ob.object_property in ("bicycle", "bike"):
            rect = ob.rectangle
            predicted_boxes.append((rect.x, rect.y, rect.x + rect.w, rect.y + rect.h))

    image = Image.open(image_path)
    draw = ImageDraw.Draw(image)

    # actual in blue
    for (x1, y1, x2, y2) in ground_truth_boxes:
        draw.rectangle([x1, y1, x2, y2], outline="blue", width=3)

    # predicted in red
    for (x1, y1, x2, y2) in predicted_boxes:
        draw.rectangle([x1, y1, x2, y2], outline="red", width=3)
        print(f"Predicted: x={x1}, y={y1}, w={x2-x1}, h={y2-y1}")

    plt.imshow(image)
    plt.axis('off')
    plt.title(f"Blue=Human | Red=Predicted")
    plt.gcf().canvas.manager.set_window_title(image_path)
    plt.show()

    return predicted_boxes

def compute_iou(predicted, expected):
    # % of overlap
    actual = box(expected[0], expected[1], expected[2], expected[3])
    detected = box(predicted[0], predicted[1], predicted[2], predicted[3])
    inter = actual.intersection(detected).area
    union = actual.union(detected).area
    
    if union == 0:
        return 0
    return inter / union

def compute_mse(predicted, expected):
    # Mean Square Error, avg pixel diff between 2 coordinate boxes
    return sum((p - g) ** 2 for p, g in zip(predicted, expected)) / 4

iou_scores = []
mse_scores = []

for image_path, ground_truth_boxes in bike_coords.items():
    predicted_boxes = process_image(image_path, ground_truth_boxes)
    
    for expected_box in ground_truth_boxes:
        for predicted_box in predicted_boxes:
            iou = compute_iou(predicted_box, expected_box)
            mse = compute_mse(predicted_box, expected_box)
            # print(f"{image_path} | IoU: {iou:.3f} | MSE: {mse:.3f}")
            iou_scores.append(iou)
            mse_scores.append(mse)
            
            
print(f"Average IoU: {sum(iou_scores) / len(iou_scores):.3f}")
print(f"Average MSE: {sum(mse_scores) / len(mse_scores):.3f}")

#run with QT_QPA_PLATFORM=wayland python p2.py