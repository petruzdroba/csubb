import csv
import re
import numpy as np
from sklearn.model_selection import train_test_split

class Utils:
    @staticmethod
    def loadData(filepath):
        texts = []
        labels = []
        with open(filepath, newline='', encoding='utf-8') as f:
            reader = csv.reader(f)
            for row in reader:
                if len(row) >= 2:
                    labels.append(row[0].strip())
                    texts.append(row[1].strip())
        print(f"Loaded {len(texts)} samples")
        print(f"Labels found: {set(labels)}")
        return texts, labels

    @staticmethod
    def cleanData(texts):
        cleaned = []
        for text in texts:
            text = text.lower()
            # remove punctuation
            text = re.sub(r'[^\w\s]', '', text)
            text = text.strip()
            cleaned.append(text)
        return cleaned

    @staticmethod
    def splitData(texts, labels, test_size=0.2, random_state=42):
        return train_test_split(
            texts, labels,
            test_size=test_size,
            random_state=random_state
        )