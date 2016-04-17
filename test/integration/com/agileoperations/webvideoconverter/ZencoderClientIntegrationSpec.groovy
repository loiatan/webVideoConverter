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
		Map uploadedS3FileInfo = [uploadedFileLocation: "s3://agileoperations.com.br/webvideoconverter/integration-tests/input/sample.dv"]
		ZencodeClient client = new ZencodeClient()
		
		when:
		HttpResponse<JsonNode> result = client.encodeToWeb(uploadedS3FileInfo)
		JsonSlurper jsonSlurper = new JsonSlurper()
		Map jsonResult = jsonSlurper.parseText(result.getBody().toString())
		
		then:
		//{"id":254369387,"test":true,"outputs":[{"id":823287249,"label":null,"url":"https://zencoder-temp-storage-us-east-1.s3.amazonaws.com/o/20160417/3bca66113c9084df19f749bdbc91945e/e2ea957b0596449ae64bd4ac4cf81d03.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAI456JQ76GBU7FECA%2F20160417%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20160417T193159Z&X-Amz-Expires=86399&X-Amz-SignedHeaders=host&X-Amz-Signature=440a09ecfddb99fd974daeaa65283023d12c0502d9fb2e392023aa1c9aee00f8"}]}
		jsonResult.test == true
		jsonResult.outputs[0].url.contains("https://zencoder-temp-storage-us-east-1.s3.amazonaws.com")
//		jsonResult.outputs.label == "mp4 low"
//		jsonResult.outputs.url.contains("http://agileoperations.com.br.s3.amazonaws.com/webvideoconverter/output/")
	}
	/* Example
	 """
	 {
	 "id": 254232685,
	 "outputs": [    
	   {
		 "id": 822833653,
		 "label": "mp4 low",
		 "url": "http://agileoperations.com.br.s3.amazonaws.com/webvideoconverter/output/timestamp/sample-mobile.mp4"
	   }
	 ],
	 "test": true
	 }"""
   */
	
    def cleanup() {
    }
}
