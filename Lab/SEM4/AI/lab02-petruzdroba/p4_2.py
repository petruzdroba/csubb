from PIL import Image
import os
import matplotlib
matplotlib.use('Qt5Agg')
import matplotlib.pyplot as plt
import numpy as np

pixels = []

for file in os.listdir('./data/images/'):
    image = Image.open(f'./data/images/{file}').resize((128,128)).convert('L')
    pixels.extend(np.array(image).flatten()) # adds all from iterable
    
minmax_scaled_pixel = [(pixel - 0) / 255.0 for pixel in pixels]

mean = np.mean(pixels)
std = np.std(pixels)
normalized_pixel = [(pixel - mean) / std for pixel in pixels]

log_normalized_pixel = np.log1p(pixels)

fig, (ax1, ax2, ax3, ax4) = plt.subplots(1, 4)
ax1.hist(pixels, 20)
ax1.set_title('Pixel values')
ax2.hist(minmax_scaled_pixel, 20)
ax2.set_title('MinMax scaled pixel values')
ax3.hist(normalized_pixel, 20)
ax3.set_title('ZNormalized pixel values')
ax4.hist(log_normalized_pixel, 20)
ax4.set_title('Log normalized pixel values')
plt.show()