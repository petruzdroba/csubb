import re
import numpy as np
import matplotlib.pyplot as plt

text = ""
with open('./data/texts.txt', mode='r', encoding='utf-8') as file:
    text = file.read()
    
sentences = re.split('[.!?]+', text)
sentence_lengths = np.array([len(re.split('[ ,.!?:()"”\n]+', sentence.strip())) for sentence in sentences if sentence.strip()])

minmax = (sentence_lengths - sentence_lengths.min()) / (sentence_lengths.max() - sentence_lengths.min())
znorm = (sentence_lengths - sentence_lengths.mean()) / sentence_lengths.std()
log = np.log1p(sentence_lengths)

_, (ax1, ax2, ax3, ax4) = plt.subplots(1, 4)
ax1.hist(sentence_lengths, bins=10)
ax1.set_title('Original word counts')
ax1.set_xlabel('Word count')
ax1.set_ylabel('Number of sentences')

ax2.hist(minmax, bins=10)
ax2.set_title('Min-Max')
ax2.set_xlabel('Normalized word count')
ax2.set_ylabel('Number of sentences')

ax3.hist(znorm, bins=10)
ax3.set_title('Z-Score')
ax3.set_xlabel('Z-Score')
ax3.set_ylabel('Number of sentences')

ax4.hist(log, bins=10)
ax4.set_title('Log Normalized')
ax4.set_xlabel('log(1 + word count)')
ax4.set_ylabel('Number of sentences')

plt.show()