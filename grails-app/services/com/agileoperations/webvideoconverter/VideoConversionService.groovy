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
		amazonS3Client = amazonS3Client == null ? new AmazonS3Client() : amazonS3Client
		Map uploadedS3FileInfo = amazonS3Client.upload(videoFile)
		
		zencoderClient = zencoderClient == null ? new ZencodeClient() : zencoderClient
		HttpResponse<JsonNode> encodedVideoFileInfo = zencoderClient.encodeToWeb(uploadedS3FileInfo)
		
		jsonSlurper = jsonSlurper == null ? new JsonSlurper() : jsonSlurper
		Map jsonResult = jsonSlurper.parseText(encodedVideoFileInfo.getBody().toString())
		return jsonResult
    }
}
