import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Data_Tagging {

	public static void main(String arg[]) {

		JSONParser parser = new JSONParser();
		Stemmer stemmer = new Stemmer();
		
		String fileName = "tweet_tagged"+".json";
		
		HashMap<String, String> map = Stemmer.stemMap();
		JSONArray jsonArrayOutput = new JSONArray();
		
		//System.out.println("result   :   " + stemmer.stemWord());
		
		try {
			JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("C:\\UB_Workspace\\Data_Tagging\\tweet_Atlanta.json"));

			for (Object obj : jsonArray) {
				JSONObject json = (JSONObject) obj;
				List<String> tags = new ArrayList<String>();
				
				String text = (String) json.get("text");
				String[] words = text.split(" "); 
				
				for(String word : words){
					
					try {
						storeWord(word, "tmp.txt");
						String stemWord = stemmer.stemWord();
						
						if(map.containsKey(stemWord)) {
							tags.add(map.get(stemWord));
						} 
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				json.put("tags", tags);
				jsonArrayOutput.add(json);
				

			}
			try {
				storeJSON(jsonArrayOutput.toString(), fileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			System.out.println(jsonArrayOutput.toString());
			System.out.println("jsonArraySize : "+ jsonArrayOutput.size());

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void storeWord(String word, String fileName) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName,false);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(word);
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
