# Smart-City-Crime-Management

Citizen journalism is a software force joining with municipal government agencies to provide a fast, efficient and less expensive solution to civic problems like crime management. The idea is to streamline analytics of issues corresponding to crime reports on social media platforms like twitter.

Following is the list of work done:

1) Collection of tweets : tweets are collected with the help of twitter4J library from 7 cities (New York, Detroit, Atlanta, Buffalo,Los Angeles, San francisco, Chicago) using their geo locations. 

2) Customized Tagger : tweets are then classified with the help of a customised tagger. It uses a list of keywords associated with crime and their different types. The tagger spots these words in tweets with the help of a stemmer based or Porter Stemming Algorithm.

3) Classifier : tweets are then classified with the help of a naive bayes classifier. 

4) Tagger and Classifier Evaluation :  Java code to evaluate agger and classifier accuracy. Calculations are done with the help of a manually judged data calculating true posities, negatives etc. 

5) UI : User interface is built with the help of HTML and Javascript. Javascript libraries to create various graphs for analytics

6) Data Storage : Data is stored and retrieved with the help of solr.
