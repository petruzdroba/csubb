from PIL import Image
import os

def apply_sepia(input_image_path, output_image_path):
    image = Image.open(input_image_path).convert("RGB")
    pixels = image.load()
    for i in range(image.width):
        for j in range(image.height):
            r, g, b = pixels[i, j]
            tr = min(int(0.393 * r + 0.769 * g + 0.189 * b), 255)
            tg = min(int(0.349 * r + 0.686 * g + 0.168 * b), 255)
            tb = min(int(0.272 * r + 0.534 * g + 0.131 * b), 255)
            pixels[i, j] = (tr, tg, tb)
    image.save(output_image_path)

def rename_files(folder_path, prefix):
    for filename in os.listdir(folder_path):
        file_path = os.path.join(folder_path, filename)
        if os.path.isfile(file_path):
            new_filename = prefix + '_' + filename
            new_file_path = os.path.join(folder_path, new_filename)
            os.rename(file_path, new_file_path)
            print(f"Renamed {filename} to {new_filename}")

def create_database(input_folder, output_sepia_folder):
    os.makedirs(output_sepia_folder, exist_ok=True)

    for image_name in os.listdir(input_folder):
        if image_name.lower().endswith(('.jpg', '.jpeg', '.png')):
            input_path = os.path.join(input_folder, image_name)
            apply_sepia(input_path, os.path.join(output_sepia_folder, image_name))
            print(f"Processed {image_name}")


INPUT_FOLDER = 'images/wo_sepia'
OUTPUT_FOLDER = 'images/sepia'

create_database(INPUT_FOLDER, OUTPUT_FOLDER)
rename_files(OUTPUT_FOLDER, "sepia")