import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EvaluateClassifier {

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
			    
			    String rel = checkIfRelevant(tweet,jsonArrayRelevant);
			    
			    if(rel.equals("tp")) {
			    	truePositive++;
			    } else if (rel.equals("tn")) {
			    	trueNegative++;
			    } else if (rel.equals("fp")) {
			    	falsePositive++;
			    } else if (rel.equals("fn")) {
			    	falseNegative++;
			    }		    
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

	private static String checkIfRelevant(JSONObject tweet,
			JSONArray jsonArrayRelevant) {
		
		String result = null;
		
		for (Object obj : jsonArrayRelevant)
		  {
			JSONObject relTweet = (JSONObject) obj;			
			
		    if(((Long)tweet.get("id")).equals((Long)relTweet.get("id"))) {
		    	
		    	String tweetClass = (String)tweet.get("class");
		    	String relTweetClass = (String)relTweet.get("class");
		    	
		    	result = assignLable(tweetClass,relTweetClass);
		    	
		    	break;
		    }
		    
		  }
		
		return result;
	}

	private static String assignLable(String tweetClass, String relTweetClass) {
		String result = null;
		if (!tweetClass.equals("Unclassified")) {
			
			if (tweetClass.equals(relTweetClass)) {
				result = "tp";
			} else {
				result = "fp";
			}
			
		} else {
			
			if (tweetClass.equals(relTweetClass)) {
				result = "tn";
			} else {
				result = "fn";
			}
		}
		return result;
	}
}
