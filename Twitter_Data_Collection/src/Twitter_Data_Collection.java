import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;

public final class Twitter_Data_Collection {

	public static void main(String[] args) {

		// Authentication

		final boolean debugEnabled = true;
		final boolean jsonStoreEnabled = true;
		final String oauthConsumerKey = "";
		final String oauthConsumerSecret = "";
		final String oauthAccessToken = "";
		final String oauthAccesstokenSecret = "";

		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

		configurationBuilder.setDebugEnabled(debugEnabled)
				.setOAuthConsumerKey(oauthConsumerKey)
				.setOAuthConsumerSecret(oauthConsumerSecret)
				.setOAuthAccessToken(oauthAccessToken)
				.setOAuthAccessTokenSecret(oauthAccesstokenSecret)
				.setJSONStoreEnabled(jsonStoreEnabled);

		// Creation of twitter factory

		TwitterFactory twitterFactory = new TwitterFactory(
				configurationBuilder.build());
		Twitter twitter = twitterFactory.getInstance();

		// Create directory and file in wokspace to store tweets

		new File("Tweets").mkdir();
		String lang = "en";
		
		double lat = 0;
		double lon = 0;
		
		String location = "";

		String locationFlag = "C";

		switch (locationFlag) {

		case "NY":
			lat = 40.7127;
			lon = -74.0059;
			location = "New York";
			break;
		case "D":
			lat = 42.3314;
			lon = -83.0458;
			location = "Detroit";
			break;
		case "A":
			lat = 33.7550;
			lon = -84.3900;
			location = "Atlanta";
			break;
		case "B":
			lat = 42.9047;
			lon = -78.8494;
			location = "Buffalo";
			break;
		case "LA":
			lat = 34.0500;
			lon = -78.8494;
			location = "Los Angeles";
			break;
		case "SF":
			lat = 37.7833;
			lon = -122.4167;
			location = "San Francisco";
			break;
		case "C":
			lat = 41.8369;
			lon = -87.6847;
			location = "Chicago";
			break;
		}

		double radius = 100.0;
		String unit = "mi";

		String queryString = "Jaywalking OR Murder OR Kidnapping OR Extortion OR Rape OR Burglary OR Assault OR speeding OR littering OR theft"; // query

		String fileName = "Tweets/" + "tweet_" + location + ".json";

		try {

			// List of tweets and query result

			List<Status> tweets;
			QueryResult queryResult;

			GeoLocation geoLocation = new GeoLocation(lat, lon);

			Query query = new Query(queryString).geoCode(geoLocation, radius,unit);
			query.setLang(lang);
			query.setCount(100);
		
			queryResult = twitter.search(query); // search twitter

			tweets = queryResult.getTweets(); // get tweets

			JSONArray jsonArray = new JSONArray();

			for (Status tweet : tweets) {

				JSONObject jsonObject;

				try {

					jsonObject = new JSONObject();

					jsonObject.put("id", tweet.getId());
					formatTwitterCreatedAt(jsonObject, tweet.getCreatedAt());
					jsonObject.put("text", tweet.getText());
					jsonObject.put("source", "Twitter");
					jsonObject.put("lang", tweet.getLang());
					jsonObject.put("location", location);
					formatTwitterUrls(jsonObject, tweet.getURLEntities());
					formatTwitterHashtag(jsonObject, tweet.getHashtagEntities());
					jsonArray.put(jsonObject);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			try {
				storeJSON(jsonArray.toString(), fileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			System.out.println(jsonArray.toString());
			System.out.println("jsonArraySize : " + jsonArray.length());
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	private static void formatTwitterCreatedAt(JSONObject jsonObject,
			Date createdAt) throws JSONException {

		String formatedDateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("EDT"));
		sdf.setLenient(false);
		formatedDateString = sdf.format(createdAt);
		jsonObject.put("date", formatedDateString);
	}

	private static void formatTwitterUrls(JSONObject jsonObject,
			URLEntity[] urlEntities) throws JSONException {

		List<String> urls = new ArrayList<String>();

		for (URLEntity u : urlEntities) {
			urls.add(u.getExpandedURL());
		}
		jsonObject.put("urls", urls);

	}

	private static void formatTwitterHashtag(JSONObject jsonObject,
			HashtagEntity[] hashtagEntities) throws JSONException {

		List<String> hashtags = new ArrayList<String>();

		for (HashtagEntity h : hashtagEntities) {
			hashtags.add(h.getText());
		}
		jsonObject.put("keywords", hashtags);
	}

	private static void storeJSON(String jsonString, String fileName)
			throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			fos = new FileOutputStream(fileName, true);
			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			bw.write(jsonString);
			bw.flush();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ignore) {
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException ignore) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ignore) {

				}
			}
		}
	}

}
