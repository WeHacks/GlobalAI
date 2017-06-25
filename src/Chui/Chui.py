import requests
import webapp2
import base64
import json

class DoChuiAuth(webapp2.RequestHandler):
    def get(self):
        self.response.out.write("Hello,world")
    def post(self):
        pictureStream = self.request.get('pictureStream')
        print pictureStream
        spoofDetection(pictureStream)
        #enrollmentID = self.request.get('enrollmentID')
#        response = webapp2.Response(faceMatch(enrollmentID, pictureStream))
#        return response


application = webapp2.WSGIApplication([
    ('/', DoChuiAuth)
    ], debug=True)
        

def spoofDetection(pictureStream):
    headers = {
       "x-api-key":"vOjf0XRyf72QJzFOVxff7aKYtUeRBtgR6MXAMzPe",
       "Content-Type":"image/jpeg",
    }
    url = "https://api.chui.ai/v1/spdetect"
    r  = requests.post(url, data = pictureStream, headers = headers)
    return r.json()['data']['success']

# Enroll a new user. Params(3 pictures) returns enrollment
def enrollUser(img0, img1, img2, name):
    headers = {
       "x-api-key":"vOjf0XRyf72QJzFOVxff7aKYtUeRBtgR6MXAMzPe",
       "Content-Type":"application/json",
    }
    url = "https://api.chui.ai/v1/enroll"
    data = {
    "img0":base64.b64encode(open('Jinchi1.jpg', 'rb').read()),
    "img1":base64.b64encode(open('Jinchi2.jpg', 'rb').read()),
    "img2":base64.b64encode(open('Jinchi3.jpg', 'rb').read()),
    "name":name
    }
    r = requests.post(url, data=json.dumps(data), headers=headers)
    print(r.json())

def faceMatch(enrollmentID, pictureStream):
    headers = {
       "x-api-key":"vOjf0XRyf72QJzFOVxff7aKYtUeRBtgR6MXAMzPe",
       "Content-Type":"application/json",
    }
    url = "https://api.chui.ai/v1/match"
    data = {
        "img":base64.b64encode(pictureStream),
        "id":enrollmentID
    }
    r = requests.post(url, data=json.dumps(data), headers=headers)
    return r.json()['data']['success']

def main():
    spoofDetection(open('Test.jpg', 'rb').read())


