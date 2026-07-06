from PIL import Image
import os
import matplotlib
matplotlib.use('Qt5Agg')
import matplotlib.pyplot as plt
import math

image = Image.open('./data/images/Leskovec.jpg')
plt.imshow(image)
plt.axis('off')
plt.show()


images = []
for file in os.listdir('./data/images/'):
    image = Image.open(f'./data/images/{file}').resize((128,128)).convert('L')
    images.append(image)
    
columns = 3
rows = math.ceil(len(images)/columns)

_, axes = plt.subplots(rows, columns)
axes = axes.flatten()

for idx in range(len(images)):
    axes[idx].imshow(images[idx], cmap='gray')
    axes[idx].axis('off')
    
plt.show()

from PIL import ImageFilter

image = Image.open('./data/images/Leskovec.jpg')
image_blur = image.filter(ImageFilter.GaussianBlur(radius=2))

_, (ax1, ax2) = plt.subplots(1, 2)
ax1.imshow(image)
ax2.imshow(image_blur)
ax1.axis('off')
ax2.axis('off')

plt.show()


image_edge = image.filter(ImageFilter.FIND_EDGES)

_, (ax1, ax2) = plt.subplots(1, 2)
ax1.imshow(image)
ax2.imshow(image_edge)
ax1.axis('off')
ax2.axis('off')

plt.show()
