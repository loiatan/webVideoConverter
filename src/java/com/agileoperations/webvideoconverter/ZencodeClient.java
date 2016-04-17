package com.agileoperations.webvideoconverter;

import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.Body;

public class ZencodeClient {
	
	public ZencodeClient(){
	}
	
	public HttpResponse<JsonNode> encodeToWeb(Map<String, String> uploadedS3FileInfo) throws UnirestException{
		
		String uploadedFileLocation = uploadedS3FileInfo.get("uploadedFileLocation");
		
		HttpResponse<JsonNode> jsonResponse = Unirest.post("https://app.zencoder.com/api/v2/jobs")
				.header("accept", "application/json")
				.header("Content-Type", "application/json")
				.header("Zencoder-Api-Key", "8c8cc8f67df571aeab9c450ea3fde3c4")
				.body("{\"input\":" + "\"" + uploadedFileLocation + "\", \"test\":\"true\"}")
				.asJson();
		
		return jsonResponse;
	}
	
//	public String encodeToWeb(Map<String, String> uploadedS3FileInfo) throws ClientProtocolException, IOException {
//		HttpClient client = new DefaultHttpClient();
//		HttpPost post = new HttpPost("http://restUrl");
//		
//		JSONObject json = new JSONObject();
//		json.put("name1", "value1");
//		json.put("name2", "value2");
//		StringEntity se = new StringEntity(json.toString());
//		
//		HttpResponse response = client.execute(request);
//		
//		BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
//		String line = "";
//		String result = "";
//		while ((line = rd.readLine()) != null) {
//			result += line + "\n";
//		}
//		return result;
//	}
	
}
