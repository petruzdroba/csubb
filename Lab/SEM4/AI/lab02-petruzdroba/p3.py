import re
import nltk
nltk.download('wordnet')
nltk.download('omw-1.4') # for multi language support
from nltk.corpus import wordnet

text = ""
with open('./data/texts.txt', mode='r', encoding='utf-8') as file:
    text = file.read()
    
sentence_count = len(re.split('[.!?:]', text))
print('Number of sentences: ', sentence_count)

word_count = len(re.split('[ ,.!?:()"”\n]+', text))
print('Number of words: ', word_count)

unique_words = set(re.split('[ ,.!?:()"”\n]+', text.lower()))
print('Number of unique words: ', len(unique_words))

min_len = min(len(w) for w in unique_words)
shortest_words = [w for w in unique_words if len(w) == min_len]
max_len = max(len(w) for w in unique_words)
longest_words = [w for w in unique_words if len(w) == max_len]
        
print(f'Shortest word: {shortest_words}\n Longest word: {longest_words}')

text_US = text.replace('ș', 's').replace('ț', 't').replace('ă', 'a').replace('â', 'a').replace('î', 'i').replace('Ș', 'S').replace('Ț', 'T').replace('Ă', 'A').replace('Â', 'A').replace('Î', 'I')
print(text_US)

def get_synonyms(word, lang='eng'):
    synonyms = set()
    for syn in wordnet.synsets(word, lang=lang):
        for lemma in syn.lemmas(lang=lang):
            synonyms.add(lemma.name())
    return synonyms

longest = re.sub(r'(.)\1+', r'\1', longest_words[0])
print(get_synonyms(longest))

#TODO: Sinonime RO
print(get_synonyms('confirma', 'ron'))
