# isurunix-bot
A simple twitter retweet bot using Twitter4j and Java.  
The bot is capable of retweeting by hashtag or a specified twitter handle.

## How To Use
1. Import the project as a Maven project in your favourite IDE.
2. Replace CONSUMER_KEY, CONSUMER_KEY_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET with values obtained from Twitter for your account.
3. Use the retweetByHash method to retweet by hashtag or retweetById to retweet, tweets from a specific account.

## Limitations
* Tweet limit not checked.
* Only support English by default. (Code can be modified to retweet other languages)

## ToDo
1. Add tweet limit check to avoid blocking by Twitter
2. Add support to configure language without editing code.
3. Add support to configure multiple twitter accounts for the bot
