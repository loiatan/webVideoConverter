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
	
    public Map convertToWebFormat(MultipartFile videoFile) {
		amazonS3Client = initialize(amazonS3Client)
		Map uploadedS3FileInfo = amazonS3Client.upload(videoFile)
		
		zencoderClient = initialize(zencoderClient)
		HttpResponse<JsonNode> encodedVideoFileInfo = zencoderClient.encodeToWeb(uploadedS3FileInfo)
		
		jsonSlurper = initialize(jsonSlurper)
		Map jsonResult = jsonSlurper.parseText(encodedVideoFileInfo.getBody().toString())
		return jsonResult
    }
	
	public Map getStatus(String jobId){
		zencoderClient = initialize(zencoderClient)
		HttpResponse<JsonNode> encodedVideoFileInfo = zencoderClient.getStatus(jobId)
		
		jsonSlurper = initialize(jsonSlurper)
		Map jsonResult = jsonSlurper.parseText(encodedVideoFileInfo.getBody().toString())
		return jsonResult
	}
	
	private Object initialize(Object obj){
		if(obj.class instanceof AmazonS3Client && amazonS3Client == null){
			obj = new AmazonS3Client()
		} else if (obj.class instanceof ZencodeClient && zencoderClient == null){
			obj = new ZencodeClient()
		} else if (obj.class instanceof JsonSlurper && jsonSlurper == null){
			obj = new JsonSlurper()
		}
		return obj
	}
}
