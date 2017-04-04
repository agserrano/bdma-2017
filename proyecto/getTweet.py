import json
import tweepy
from tweepy import OAuthHandler
from pymongo import MongoClient
 
consumer_key = 'XXX'
consumer_secret = 'YYY'
access_token = 'ZZZ'
access_secret = 'VVV'
 
auth = OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_secret)
 
api = tweepy.API(auth)

# for status in tweepy.Cursor(api.home_timeline).items(10):
#     # Process a single status
#     print(status.text)

client = MongoClient("localhost")
db = client.proyecto

# ejemplo de acceder a un usuario
# for tweet in tweepy.Cursor().items():
#     print json.dumps(tweet)

max_tweets = None
searched_tweets = [status._json for status in tweepy
    .Cursor(api.search, q='from:AdaColau', since='2010-01-01')
    .items(max_tweets)]
db.tweets.insert_many([{'x': i} for i in searched_tweets])
#json_strings = [db.tweets.insert_one(json.dumps(json_obj)) for json_obj in searched_tweets]




# ejemplo de busqueda por hastag
# for tweet in tweepy.Cursor(api.search, q='#TrobadesAlcaldessa', since='2017-02-10').items():
#     print tweet.text

# ejempo de busqueda por latitud longitud
# for tweet in tweepy.Cursor(api.search, q="", geocode='41.424619,2.190342,1km', since='2017-02-10').items():
#     print tweet.text



# TO START MONGO
# goto posgrado/mongo
# ./mongodb-osx-x86_64-3.4.1/bin/mongod --dbpath mongodb-osx-x86_64-3.4.1/db

# TO SHOW THE DATA IN MONGO
# db.tweets.find({})
# TO CLEAN THE DB
# db.tweets.drop()
# NUMBER OF TWEETS.
# db.tweets.count({})