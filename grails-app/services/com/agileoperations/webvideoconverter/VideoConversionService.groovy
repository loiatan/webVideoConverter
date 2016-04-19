package com.agileoperations.webvideoconverter

import grails.transaction.Transactional
import groovy.json.JsonSlurper

import org.springframework.web.multipart.MultipartFile

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.JsonNode

@Transactional
class VideoConversionService {
	
	S3Client s3Client
	ZencodeClient zencoderClient
	JsonSlurper jsonSlurper
	
	public VideoConversionService(){
		s3Client = new S3Client()
		zencoderClient = new ZencodeClient()
		jsonSlurper = new JsonSlurper()
	}
	
    public Map convertToWebFormat(MultipartFile videoFile) {
		
		Map uploadedS3FileInfo = s3Client.upload(videoFile)
		
		HttpResponse<JsonNode> encodedVideoFileInfo = zencoderClient.encodeToWeb(uploadedS3FileInfo)
		
		String encodedVideoFileInfoJson = encodedVideoFileInfo.getBody().toString()
		
		Map encodedVideoFileInfoMap = jsonSlurper.parseText(encodedVideoFileInfoJson)
		return encodedVideoFileInfoMap
    }
	
	public Map getJobStatus(String jobId){
		HttpResponse<JsonNode> jobStatus = zencoderClient.getJobStatus(jobId)

		String jobStatusJson = jobStatus.getBody().toString()
		
		Map jobStatusMap = jsonSlurper.parseText(jobStatusJson)
		return jobStatusMap
	}
}
