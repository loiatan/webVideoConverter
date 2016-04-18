package com.agileoperations.webvideoconverter

import grails.transaction.Transactional
import groovy.json.JsonSlurper
import org.springframework.web.multipart.MultipartFile
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.JsonNode

@Transactional
class VideoConversionService {
	
	AmazonS3Client amazonS3Client
	ZencodeClient zencoderClient
	JsonSlurper jsonSlurper
	
	public VideoConversionService(){
		this.amazonS3Client = new AmazonS3Client()
		this.zencoderClient = new ZencodeClient()
		this.jsonSlurper = new JsonSlurper()
	}
	
    public Map convertToWebFormat(MultipartFile videoFile) {
		Map uploadedS3FileInfo = amazonS3Client.upload(videoFile)
		
		HttpResponse<JsonNode> encodedVideoFileInfo = zencoderClient.encodeToWeb(uploadedS3FileInfo)
		
		Map jsonResult = jsonSlurper.parseText(encodedVideoFileInfo.getBody().toString())
		return jsonResult
    }
	
	public Map getStatus(String jobId){
		HttpResponse<JsonNode> encodedVideoFileInfo = zencoderClient.getStatus(jobId)
		
		Map jsonResult = jsonSlurper.parseText(encodedVideoFileInfo.getBody().toString())
		return jsonResult
	}

}
