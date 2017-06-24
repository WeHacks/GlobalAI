import pyrebase

# Firebase DB Connection

config = {
  "apiKey": "AIzaSyDaj_0mZZ09cATbWtzQ7cK3VnxNpbwpbWc",
  "authDomain": "ai-hackathon-4edf3.firebaseapp.com",
  "databaseURL": "https://ai-hackathon-4edf3.firebaseio.com",
  "projectId": "ai-hackathon-4edf3",
  "storageBucket": "ai-hackathon-4edf3.appspot.com",
  "messagingSenderId": "399052073630"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

# Set up firebase user

auth = firebase.auth()
#authenticate a user
user = auth.sign_in_with_email_and_password("test@abc.com", "12345678")

# print(user['idToken'])
# print(user['email'])


# CREATE RECORD
daniel = {"name": "daniel9231", "enrollment_id": "jl23jr"}
db.child("customers").child("daniel").set(daniel, user['idToken'])

jinchi = {"name": "jinchi3423", "enrollment_id": "23jf2523f"}
db.child("customers").child("jinchi").set(jinchi, user['idToken'])

wilson = {"name": "wilson8640", "enrollment_id": "pdsff341"}
db.child("customers").child("wilson").set(wilson, user['idToken'])

ron = {"name": "ron1673", "agency": "pdsff341"}
db.child("customers").child("ron").set(ron, user['idToken'])

test_dummy = {"name": "test", "agency": "fslkdfjwo23423"}
db.child("customers").child("test_dummy").set(test_dummy, user['idToken'])


# GET ALL RECORDS
all_customers = db.child("customers").get(user['idToken']).val()
print(all_customers)

# GET SINGLE RECORD
daniel_data = db.child("customers").child("daniel").get(user['idToken']).val()
print(daniel_data)

# UPDATE RECORD
db.child("customers").child("ron").update({"name": "RON1111"}, user['idToken'])

# DELETE RECORD
db.child("customers").child("test_dummy").remove(user['idToken'])





