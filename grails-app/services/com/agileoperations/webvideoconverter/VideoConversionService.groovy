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

		Map encodedVideoFileInfoMap = getMapFromJsonString(encodedVideoFileInfo)
		
		HttpResponse<JsonNode> jobStatus = zencoderClient.getJobStatus(encodedVideoFileInfoMap.id.toString())
		
		Map jobStatusMap = getMapFromJsonString(jobStatus)
		
		log.info "Job: " + jobStatusMap
		log.info "Job: " + encodedVideoFileInfoMap.id + ", Status: " + jobStatusMap.state
		while(jobStatusMap.state == "pending" || jobStatusMap.state == "waiting" || jobStatusMap.state == "processing"){
			Thread.sleep(5000)
			jobStatusMap = getMapFromJsonString(zencoderClient.getJobStatus(encodedVideoFileInfoMap.id.toString()))
			log.info "Encoding job: " + encodedVideoFileInfoMap.id + ", Job status: " + jobStatusMap.state
			if(jobStatusMap.state == "finished"){
				log.info "Encoding job finished!"
				log.info "Job: " + jobStatusMap
				break;
			}
		}

		return [encodedVideoFileInfoMap: encodedVideoFileInfoMap, jobStatus: jobStatusMap]
    }
	
	public Map getJobStatus(String jobId){
		HttpResponse<JsonNode> jobStatus = zencoderClient.getJobStatus(jobId)

		Map jobStatusMap = getMapFromJsonString(jobStatus)
		
		return jobStatusMap
	}
	
	private Map getMapFromJsonString(HttpResponse<JsonNode> jsonObject){
		String jsonString = jsonObject.getBody().toString()
		Map map = jsonSlurper.parseText(jsonString)
		return map
	}
}
