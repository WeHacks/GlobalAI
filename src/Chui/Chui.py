import requests

def spoofDetection():
    headers = {
       "x-api-key":"vOjf0XRyf72QJzFOVxff7aKYtUeRBtgR6MXAMzPe",
       "Content-Type":"image/jpeg",
    }
    url = "https://api.chui.ai/v1/spdetect"
    r  = requests.post(url,data=open('/Users/brehski/GlobalAI/pics/Jinchi1.jpg','rb').read(),headers=headers)
    print r.json()

"""def enrollUser()
    headers = {
       "x-api-key":"vOjf0XRyf72QJzFOVxff7aKYtUeRBtgR6MXAMzPe",
       "Content-Type":"application/json",
    }

    url = "https://api.chui.ai/v1/enroll"
"""


spoofDetection()

