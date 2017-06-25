import Chui
import base64
img0 = base64.b64encode(open('Jinchi1.jpg', 'rb').read())
img1 = base64.b64encode(open('Jinchi2.jpg', 'rb').read())
img2 = base64.b64encode(open('Jinchi3.jpg', 'rb').read())
fake = open('FAKE.jpg', 'rb').read()
test = open('Test.jpg', 'rb').read()
Chui.spoofDetection(test)
Chui.faceMatch('ahBzfmNodWlzcGRldGVjdG9ychcLEgpFbnJvbGxtZW50GICAgMCozZcLDA', test)
