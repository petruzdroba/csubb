import os
from azure.core.credentials import AzureKeyCredential
from azure.ai.textanalytics import TextAnalyticsClient
from dotenv import load_dotenv

load_dotenv()
endpoint = os.environ["ENDPOINT"]
key = os.environ["KEY"]

client = TextAnalyticsClient(endpoint=endpoint, credential=AzureKeyCredential(key))

def analyze_sentiment(documents):
    result = client.analyze_sentiment(documents, show_opinion_mining=True)
    docs = [doc for doc in result if not doc.is_error]

    sentiments = []

    print("Let's visualize the sentiment of each of these documents")
    for idx, doc in enumerate(docs):
        sentiments.append(doc.sentiment)

    return sentiments

documents = [
    "By choosing a bike over a car, I’m reducing my environmental footprint. Cycling promotes eco-friendly transportation, and I’m proud to be part of that movement.",
    "A new dawn burning away the darkness",
    "I am so sad that I lost my job. I have been working there for 10 years and now I have to find a new one.",
    "I just lost 10k on blackjack, I am so happy! I am going to go out and celebrate with my friends."
]

sentiments = analyze_sentiment(documents)
print(sentiments)
