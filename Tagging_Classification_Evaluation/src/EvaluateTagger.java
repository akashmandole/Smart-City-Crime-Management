import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EvaluateTagger {

	static float truePositive = 0;
	static float trueNegative = 0;
	static float falsePositive = 0;
	static float falseNegative = 0;
	
	public static void main(String[] args) {
		evaluate();
	}
	
	public static void evaluate() {

		JSONParser parser = new JSONParser();
		
		try {
			JSONArray jsonArrayTest = (JSONArray) parser.parse(new FileReader(
					"C:\\UB_Workspace\\AIR_Tagger_Classification_Evaluation\\tweet_Atlanta.json"));
			
			JSONArray jsonArrayRelevant = (JSONArray) parser.parse(new FileReader(
					"C:\\UB_Workspace\\AIR_Tagger_Classification_Evaluation\\tweet_Atlanta_relevant.json"));
		
			for (Object obj : jsonArrayTest)
			  {
			    JSONObject tweet = (JSONObject) obj;
			    
			    checkIfRelevant(tweet,jsonArrayRelevant);
			    
			  }
			
			System.out.println("True Positive : "  + truePositive);
			System.out.println("false Positive : " + falsePositive);
			System.out.println("false Negative : " + falseNegative);
			System.out.println("True Negative : "  + trueNegative);
			
			float precesion = truePositive/(truePositive+falsePositive);
			System.out.println("Precesion : " + precesion);
			
			float recall = truePositive/(truePositive+falseNegative);
			System.out.println("recall : " + recall);
			
			float accuracy = (truePositive+trueNegative)/(truePositive+trueNegative+falsePositive+falseNegative);
			System.out.println("accuracy : " + accuracy);
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private static void checkIfRelevant(JSONObject tweet,
			JSONArray jsonArrayRelevant) {
		
		for (Object obj : jsonArrayRelevant)
		  {
			JSONObject relTweet = (JSONObject) obj;			
			
			
		    if(((Long)tweet.get("id")).equals((Long)relTweet.get("id"))) {
		    	
		    	
		    	JSONArray tweetTags = (JSONArray)tweet.get("tags");
		    	JSONArray relTweetTags = (JSONArray)relTweet.get("tags");

		    	assignLable(tweetTags,relTweetTags);
		    	
		    	break;
		    }
		    
		  }
		
	}

	private static void assignLable(JSONArray tweetTags, JSONArray relTweetTags) {
		
		for (Object obj : tweetTags) {
			String tag = (String) obj;
			boolean check = checkIfInRelevant(tag,relTweetTags);
			if (check) {
				truePositive++;
			} else {
				falsePositive++;
			}
		}
		
		for (Object obj : relTweetTags) {
			String tag = (String) obj;
			boolean check = checkIfInPredicted(tag,tweetTags);
			if (check == false) {
				falseNegative++;
			} 
		}
	}

	private static boolean checkIfInPredicted(String relTag, JSONArray tweetTags) {
		boolean result = false;
		
		for (Object obj : tweetTags) {
			String tag = (String) obj;
			
			if (tag.equals(relTag)) {
				result = true;
				break;
			}
		}
		
		return result;
	}

	private static boolean checkIfInRelevant(String tag, JSONArray relTweetTags) {
		boolean result = false;
		
		for (Object obj : relTweetTags) {
			String relTag = (String) obj;
			
			if (tag.equals(relTag)) {
				result = true;
				break;
			}
		}
		
		return result;
	}
}
