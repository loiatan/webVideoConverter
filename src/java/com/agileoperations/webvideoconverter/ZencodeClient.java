package com.agileoperations.webvideoconverter;

import java.util.Map;

import org.apache.log4j.Logger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.Body;

public class ZencodeClient {
	
	// Injecting log4j
	Logger log = Logger.getLogger(getClass());
	
	public ZencodeClient(){
	}
	
	public HttpResponse<JsonNode> encodeToWeb(Map<String, String> uploadedS3FileInfo) throws UnirestException{
		
		String uploadedFileLocation = uploadedS3FileInfo.get("uploadedFileLocation");
		String timestamp = String.valueOf(uploadedS3FileInfo.get("timestamp"));
		String multipartFilename = uploadedS3FileInfo.get("multipartFilename").replaceAll("\\.\\w+", ".mp4"); //.replace(".dv", ".mp4");
		
		String jsonBody = "{\"input\":" + "\"" + uploadedFileLocation + "\"" 
				+ ", \"test\":\"true\""
				+ ", \"outputs\": ["
				+ "{ \"public\": \"true\""
				+ ", \"url\":" + "\"s3://agileoperations.com.br/webvideoconverter/output/"+ timestamp + "/" + multipartFilename + "\""
				+ ", \"label\": \"mp4 low\""
				+ ", \"size\": \"640x480\""
				+" }]"
				+ "}";
		
		log.info("POST request body: " + jsonBody);
		
		HttpResponse<JsonNode> jsonResponse = Unirest.post("https://app.zencoder.com/api/v2/jobs")
				.header("accept", "application/json")
				.header("Content-Type", "application/json")
				.header("Zencoder-Api-Key", "8c8cc8f67df571aeab9c450ea3fde3c4")
				.body(jsonBody)
				.asJson();
		
		return jsonResponse;
	}
	
	public HttpResponse<JsonNode> getJobStatus(String jobId) throws UnirestException{
		
		HttpResponse<JsonNode> jsonResponse = Unirest.get("https://app.zencoder.com/api/v2/jobs/" + jobId + "/progress")
				.header("accept", "application/json")
				.header("Content-Type", "application/json")
				.header("Zencoder-Api-Key", "8c8cc8f67df571aeab9c450ea3fde3c4")
				.asJson();
		
		return jsonResponse;
	}
	
}
