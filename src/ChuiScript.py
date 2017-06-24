#Chui script to authenticate a user
import pyodbc
def main():
    server = 'ai-hackathon.database.windows.net'
    database = 'ai-hackathon'
    username = 'ron'
    password = 'ai-hackathon1'

    driver = '{ODBC Driver 13 for SQL Server}'
    cnxn = pyodbc.connect('Driver'+driver+';PORT=1433;S
