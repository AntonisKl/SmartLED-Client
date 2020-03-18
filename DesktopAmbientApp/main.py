import numpy as np
from PIL import ImageGrab, Image, ImageDraw
import time
import os
import requests
from pystray import Icon, Menu, MenuItem
import threading

#//////////////////////////////////////////////////////////////////////////////////////////////////////////
# GLOBAL DEFINES
#//////////////////////////////////////////////////////////////////////////////////////////////////////////
LOOP_INTERVAL = 1    # how often we calculate screen colour (in seconds)
SERVER_URL = "http://192.168.1.20:80"
ambientOn = True
ambientOnEvent = threading.Event()
ambientOnEvent.set() # initially internal flag is True

def main():
    # run loop
    while True:
        # if ambientOnEvent is False, wait for it to be set to True
        ambientOnEvent.wait()

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
        requests.get(SERVER_URL + "/setColor?r=" + str(red) + "&g=" + str(green) + "&b=" + str(blue)) # send request to light's controller

        time.sleep(LOOP_INTERVAL)


# system tray icon creation
width=120
height=120
# Generate an image
image = Image.new('RGB', (width, height))
dc = ImageDraw.Draw(image)
dc.ellipse((0, 0, 120, 120), fill = 'red')
dc.ellipse((20, 20, 100, 100), fill = 'blue')
dc.ellipse((40, 40, 80, 80), fill = 'green')

def turnOnOrOff(icon, item):
    global ambientOn
    ambientOn = not item.checked
    if ambientOn:
        ambientOnEvent.set()
    else:
        ambientOnEvent.clear()

icon = Icon("Ambient RGB Light", image, "Ambient RGB Light", 
                Menu(MenuItem('Ambient On', action=turnOnOrOff, checked=lambda item: ambientOn)))

def iconCallback(icon):
    icon.visible = True
    main()

icon.run(iconCallback)
