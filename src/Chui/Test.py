import requests

#url = 'https://globalai-171801.appspot.com/'
url = 'http://localhost:8080'
data = {
        'pictureStream':open('Test.jpg', 'rb').read()
       }
print data
r = requests.post(url, data)
print r
