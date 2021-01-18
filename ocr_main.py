import cv2
import pytesseract
from PIL import Image


def main(path):
    # Get File Name from Command Line
    # path = input("Enter the file path : ").strip()

    # Load the required image
    image = cv2.imread(path)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    # Store grayscale image as a temporary file to apply OCR
    filename = "{}.png".format("temp")
    cv2.imwrite(filename, gray)

    # Load the image as a PIL/Pillow image, apply OCR, and then delete the temporary file
    #  edited   pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR'
    text = pytesseract.image_to_string(Image.open(filename))

    print("OCR Text is " + text.strip())
    val=text.strip().split('D')
    atte=val[0].split(',')
    print(atte[0],atte[1])
    print(val[1])
    return  text.strip()

# try:
#     main("C:\\Users\\geena\\Downloads\\uu.PNG")
# except Exception as e:
#     print(e.args)
#     print(e.__cause__)

