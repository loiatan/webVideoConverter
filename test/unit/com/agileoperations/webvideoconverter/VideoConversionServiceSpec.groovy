package com.agileoperations.webvideoconverter

import grails.test.mixin.TestFor
import groovy.json.JsonSlurper

import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile

import spock.lang.Specification

import com.mashape.unirest.http.HttpResponse

/**
 * Unit test for VideoConversionService
 */
@TestFor(VideoConversionService)
class VideoConversionServiceSpec extends Specification {

    def setup() {
    }

	def "should convert input video to web format"() {
		given:
		GrailsMockMultipartFile videoFile = Mock()

		service.s3Client = Mock(S3Client)
		service.zencoderClient = Mock(ZencodeClient)
		service.jsonSlurper = Mock(JsonSlurper)
		
		HttpResponse httpResponse = Mock()
		
		when:
		Map jsonResult = service.convertToWebFormat(videoFile)
		
		then:
		1 * service.s3Client.upload(videoFile) >> [uploadedFileLocation: "s3://agileoperations.com.br/webvideoconverter/input/timestamp/sample.dv"]
		
		and:
		1 * service.zencoderClient.encodeToWeb([uploadedFileLocation: "s3://agileoperations.com.br/webvideoconverter/input/timestamp/sample.dv"]) >> httpResponse
		1 * httpResponse.getBody() >> "url: ..."
		1 * service.jsonSlurper.parseText("url: ...") >> [id: 100, test: true, outputs: [[id: 234341, url: "https://zencoder-temp-storage-us-east-1.s3.amazonaws.com"]]]
		
		and:
		1 * service.zencoderClient.getJobStatus("100") >> httpResponse
		1 * httpResponse.getBody() >> "url: ..."
		1 * service.jsonSlurper.parseText("url: ...") >> [state: "finished", outputs: [[state: "finished", id: 234341]]]
		
		and:
		jsonResult.encodedVideoFileInfoMap.test == true
		jsonResult.encodedVideoFileInfoMap.outputs[0].id == 234341
		jsonResult.encodedVideoFileInfoMap.outputs[0].url.contains("https://zencoder-temp-storage-us-east-1.s3.amazonaws.com")
	}
	
    def cleanup() {
    }
}
