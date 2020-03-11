import numpy as np
from PIL import ImageGrab
import time
import os
import requests

#//////////////////////////////////////////////////////////////////////////////////////////////////////////
# GLOBAL DEFINES
#//////////////////////////////////////////////////////////////////////////////////////////////////////////
LOOP_INTERVAL = 1    # how often we calculate screen colour (in seconds)
SERVER_URL = "http://192.168.1.20:80"

# run loop
while True:
    #init counters/accumulators
    red   = 0
    green = 0
    blue  = 0
    
    image = ImageGrab.grab()  # take a screenshot
    image = np.array(image)
    
    average = image.mean(axis=0).mean(axis=0) # take the average color

    red = int(average[0])
    green = int(average[1])
    blue = int(average[2])
    requests.get(SERVER_URL + "/setColor?r=" + str(red) + "&g=" + str(green) + "&b=" + str(blue))

    time.sleep(LOOP_INTERVAL)
