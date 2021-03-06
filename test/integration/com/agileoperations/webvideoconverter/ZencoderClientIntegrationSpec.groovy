package com.agileoperations.webvideoconverter

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import groovy.json.JsonSlurper
import spock.lang.*

/**
 * ZencoderClient Integration tests
 */
class ZencoderClientIntegrationSpec extends Specification {

    def setup() {
    }

	def "should request zencoder API to encode video in s3"(){
		given:
		Map uploadedS3FileInfo = [
			uploadedFileLocation: "s3://agileoperations.com.br/webvideoconverter/integration-tests/input/sample.dv"
			, timestamp: "integration-test-timestamp", multipartFilename: "sample-encoded-during-test.dv"
			]
		ZencodeClient client = new ZencodeClient()
		
		when:
		HttpResponse<JsonNode> result = client.encodeToWeb(uploadedS3FileInfo)
		
		then:
		JsonSlurper jsonSlurper = new JsonSlurper()
		Map jsonResult = jsonSlurper.parseText(result.getBody().toString())
		
		jsonResult.test == true
		jsonResult.outputs[0].url.contains("http://agileoperations.com.br.s3.amazonaws.com/webvideoconverter/output/")
		jsonResult.outputs[0].label == "mp4 low"
		
	}
	
	def "should query job status using zencoder API"(){
		given:
		String jobId = "254419025"
		ZencodeClient client = new ZencodeClient()
		
		when:
		HttpResponse<JsonNode> result = client.getJobStatus(jobId)
		
		then:
		JsonSlurper jsonSlurper = new JsonSlurper()
		Map jsonResult = jsonSlurper.parseText(result.getBody().toString())
		
		jsonResult.state == "finished"
	}
	
	
    def cleanup() {
    }
}
