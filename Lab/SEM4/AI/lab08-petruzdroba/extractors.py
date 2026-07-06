from sklearn.feature_extraction.text import CountVectorizer

class BagOfWordsExtractor:
    def __init__(self):
        self.vectorizer = CountVectorizer()
    
    def fit_transform(self, trainInputs):
        return self.vectorizer.fit_transform(trainInputs).toarray()
    
    def transform(self, testInputs):
        return self.vectorizer.transform(testInputs).toarray()


from sklearn.feature_extraction.text import TfidfVectorizer

class TfidfExtractor:
    def __init__(self):
        self.vectorizer = TfidfVectorizer(max_features=1000)
    
    def fit_transform(self, trainInputs):
        return self.vectorizer.fit_transform(trainInputs).toarray()
    
    def transform(self, testInputs):
        return self.vectorizer.transform(testInputs).toarray()


import gensim.downloader as api
import numpy as np

class Word2VecExtractor:
    def __init__(self, model_name="glove-wiki-gigaword-50"):
        self.model = api.load(model_name)  #cache
        self.vector_size = self.model.vector_size
    
    def _compute_features(self, inputs):
        features = []
        for text in inputs:
            words = [w for w in text.lower().split() if w in self.model]
            if words:
                vector = np.mean([self.model[w] for w in words], axis=0)
            else:
                vector = np.zeros(self.vector_size)
            features.append(vector)
        return np.array(features)
    
    def fit_transform(self, trainInputs):
        return self._compute_features(trainInputs)
    
    def transform(self, testInputs):
        return self._compute_features(testInputs)


from nrclex import NRCLex
import numpy as np

class CustomExtractor:
    def __init__(self):
        self.emotions = ['fear', 'anger', 'trust', 'sadness', 'disgust', 'joy', 'surprise']

    def _compute_features(self, inputs):
        features = []
        for text in inputs:
            emotion = NRCLex(lexicon_file='nrc_en.json')
            emotion.load_raw_text(text)
            scores = emotion.raw_emotion_scores

            vector = [scores.get(e, 0) for e in self.emotions]
            
            words = text.lower().split()
            vector.append(len(words)) 
            vector.append(text.count('!'))        
            vector.append(text.count('?'))           
            vector.append(sum(1 for w in words if w.isupper())) 
            
            features.append(vector)
        return np.array(features)

    def fit_transform(self, trainInputs):
        return self._compute_features(trainInputs)

    def transform(self, testInputs):
        return self._compute_features(testInputs)