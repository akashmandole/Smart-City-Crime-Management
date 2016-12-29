import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Data_Classification {

	public static class CrimeClasses {
		final static String Murder = "Murder";
		final static String Rape = "Rape";
		final static String Robbery = "Robbery";
		final static String Assault = "Assault";
		final static String Burglary = "Burglary";
		final static String Theft = "Theft";
		final static String Motor_Vehicle_Theft = "Motor Vehicle Theft"; 
		final static String Arson = "Arson";
		final static String Others = "Others";
		final static String Unclassified = "Unclassified";
	}
	
	public static void main(String arg[]) {
	
		JSONParser parser = new JSONParser();
		
		try {
			JSONArray jsonArrayTrain = (JSONArray) parser.parse(new FileReader(
					"C:\\UB_Workspace\\Data_Classification\\train.json"));
			
			// Initialize hash map for prior probability of class
			HashMap<String, Double> priorProbabilityOfClass = new HashMap<String, Double>();
			initializePriorProbabilityOfClass(priorProbabilityOfClass);
			
			// Initialize hash map for probability of evidences
			HashMap<String, Double> probabilityOfEvidences = new HashMap<String, Double>();
			initializeProbabilityOfEvidences(probabilityOfEvidences);
			
			
			// Initialize hash map for probability of likelihood
			HashMap<String, Double> probabilityOfLikelihood = new HashMap<String, Double>();
			initializeProbabilityOfLikelihood(probabilityOfLikelihood);
			
			// Calculating prior probabilities of class
			
			for (Object obj : jsonArrayTrain) {
				JSONObject jsonObject = (JSONObject) obj;
				String objClass = (String) jsonObject.get("class");
				calculatePriorProbabilitiesOfClass(priorProbabilityOfClass,objClass);
			}

			// Calculating probabilities of evidences

			for (Object obj : jsonArrayTrain) {
				JSONObject jsonObject = (JSONObject) obj;
				JSONArray objtags = (JSONArray) jsonObject.get("tags");
				calculateProbabilityOfEvidences(probabilityOfEvidences,objtags);
			}
			
			// Calculating probabilities of likelihood

			for (Object obj : jsonArrayTrain) {
				JSONObject jsonObject = (JSONObject) obj;
				JSONArray objtags = (JSONArray) jsonObject.get("tags");
				String objClass = (String) jsonObject.get("class");
				calculateProbabilityOfLikelihood(probabilityOfLikelihood,objtags,objClass);
			}
			
			// Classify 
			
			classify(priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
		
			// Display Hashmap
		
			Iterator<String> iterator = probabilityOfLikelihood.keySet().iterator();

			while (iterator.hasNext()) {
			   String key = iterator.next().toString();
			   Double value = probabilityOfLikelihood.get(key);

			   System.out.println(key + " " + value);
			}
			
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Double max(Double firstValue, Double... otherValues) {
	    for (Double value : otherValues) {
	        if (firstValue < value ) {
	            firstValue = value;
	        }
	    }
	    return firstValue;
	}

	private static void classify(HashMap<String, Double> priorProbabilityOfClass, HashMap<String, Double> probabilityOfEvidences, HashMap<String, Double> probabilityOfLikelihood) {
		
		JSONParser parser = new JSONParser();
		JSONArray jsonArrayOutput = new JSONArray();
		String fileName = "result"+".json";
		
		try {
			JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("C:\\UB_Workspace\\Data_Classification\\test.json"));

			for (Object obj : jsonArray) {
				JSONObject jsonObject = (JSONObject) obj;
				JSONArray objTags = (JSONArray)jsonObject.get("tags");
				
				String classObj = CrimeClasses.Unclassified.trim();
				
				Double p_Murder = predict(CrimeClasses.Murder.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				Double p_Rape = predict(CrimeClasses.Rape.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				Double p_Robbery = predict(CrimeClasses.Robbery.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				Double p_Assault = predict(CrimeClasses.Assault.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				Double p_Burglary = predict(CrimeClasses.Burglary.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				Double p_Theft = predict(CrimeClasses.Theft.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				Double p_Motor_Vehicle_Theft = predict(CrimeClasses.Motor_Vehicle_Theft.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood); 
				Double p_Arson = predict(CrimeClasses.Arson.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				Double p_Others = predict(CrimeClasses.Others.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				Double p_Unclassified = predict(CrimeClasses.Unclassified.trim(),objTags,priorProbabilityOfClass,probabilityOfEvidences,probabilityOfLikelihood);
				
				Double max = max(p_Murder,p_Rape,p_Robbery,p_Assault,p_Burglary,p_Theft,p_Motor_Vehicle_Theft,p_Arson,p_Others,p_Unclassified);
				
				if(max == p_Murder){
					classObj = CrimeClasses.Murder.trim();
				}else if (max == p_Rape) {
					classObj = CrimeClasses.Rape.trim();
				}else if (max == p_Robbery) {
					classObj = CrimeClasses.Robbery.trim();
				}else if (max == p_Assault) {
					classObj = CrimeClasses.Assault.trim();
				}else if (max == p_Burglary) {
					classObj = CrimeClasses.Burglary.trim();
				}else if (max == p_Theft) {
					classObj = CrimeClasses.Theft.trim();
				}else if (max == p_Motor_Vehicle_Theft) {
					classObj = CrimeClasses.Motor_Vehicle_Theft.trim();
				}else if (max == p_Arson) {
					classObj = CrimeClasses.Arson.trim();
				}else if (max == p_Others) {
					classObj = CrimeClasses.Others.trim();
				}else if (max == p_Unclassified) {
					classObj = CrimeClasses.Unclassified.trim();
				}
	
				jsonObject.put("class", classObj);
				jsonArrayOutput.add(jsonObject);

			}
			try {
				storeJSON(jsonArrayOutput.toString(), fileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println(jsonArrayOutput.toString());
			System.out.println("jsonArraySize : "+ jsonArrayOutput.size());

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
	}

	private static Double predict(String objClass, JSONArray objTags, HashMap<String, Double> priorProbabilityOfClass, HashMap<String, Double> probabilityOfEvidences, HashMap<String, Double> probabilityOfLikelihood) {
		Double result = 0.0;
		
		Double priorProbability = priorProbabilityOfClass.get(objClass);

		Double evidenceProbability = 1.0;
		Double likelihoodProbability = 1.0;
		
		for (Object obj : objTags) {
			String tag = (String) obj;
			if (probabilityOfEvidences.containsKey(tag.trim())) {
				evidenceProbability = evidenceProbability * probabilityOfEvidences.get(tag.trim());				
			}
			if (probabilityOfLikelihood.containsKey(tag.trim()+"|"+objClass.trim())) {
				likelihoodProbability = likelihoodProbability * probabilityOfLikelihood.get(tag.trim()+"|"+objClass.trim());
			}
		}
		
		result = (likelihoodProbability * priorProbability)/evidenceProbability;
		
		return result;
	}

	private static void calculateProbabilityOfLikelihood(HashMap<String, Double> probabilityOfLikelihood, JSONArray objTags, String objClass) {
		for (Object obj : objTags) {
			String tag = (String) obj;
		
			if (probabilityOfLikelihood.containsKey(tag.trim()+"|"+objClass.trim())) {
				Double value = probabilityOfLikelihood.get(tag.trim()+"|"+objClass.trim());
				Double newValue = value + 1;
				probabilityOfLikelihood.put(tag.trim()+"|"+objClass.trim(), newValue);
				
			}
		}
		
	}

	private static void initializeProbabilityOfLikelihood(HashMap<String, Double> probabilityOfLikelihood) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("C:\\UB_Workspace\\Data_Classification\\tags.txt"));
		while(sc.hasNext()){
		 String tag = sc.nextLine();
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Murder.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Rape.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Robbery.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Assault.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Burglary.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Theft.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Motor_Vehicle_Theft.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Arson.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Others.trim(), Double.valueOf(0));
		 probabilityOfLikelihood.put(tag.trim()+"|"+CrimeClasses.Unclassified.trim(), Double.valueOf(0));
		}
		sc.close();
	}

	private static void calculateProbabilityOfEvidences(HashMap<String, Double> probabilityOfEvidences, JSONArray objTags) {
		for (Object obj : objTags) {
			String tag = (String) obj;
		
			if (probabilityOfEvidences.containsKey(tag)) {
				Double value = probabilityOfEvidences.get(tag);
				Double newValue = value + 1;
				probabilityOfEvidences.put(tag, newValue);
				
			}
		}
	}

	private static void initializeProbabilityOfEvidences(HashMap<String, Double> probabilityOfEvidences) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("C:\\UB_Workspace\\Data_Classification\\tags.txt"));
		while(sc.hasNext()){
		 String tag = sc.nextLine();
		 probabilityOfEvidences.put(tag, Double.valueOf(0));
		}
		sc.close();
	}

	private static void calculatePriorProbabilitiesOfClass(HashMap<String, Double> priorProbabilityOfClass, String objClass) {
		
		if (priorProbabilityOfClass.containsKey(objClass)) {
			Double value = priorProbabilityOfClass.get(objClass);
			Double newValue = value + 1;
			priorProbabilityOfClass.put(objClass, newValue);
		}
	}

	private static void initializePriorProbabilityOfClass(HashMap<String, Double> priorProbabilityOfClass) {
		
		priorProbabilityOfClass.put(CrimeClasses.Murder.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Rape.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Robbery.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Assault.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Burglary.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Theft.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Motor_Vehicle_Theft.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Arson.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Others.trim(), Double.valueOf(0));
		priorProbabilityOfClass.put(CrimeClasses.Unclassified.trim(), Double.valueOf(0));
		
	}
	
	private static void storeJSON(String jsonString, String fileName) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName,true);
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
