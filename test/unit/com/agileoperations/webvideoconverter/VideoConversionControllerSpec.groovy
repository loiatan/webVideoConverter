package com.agileoperations.webvideoconverter

import grails.test.mixin.TestFor

import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile

import spock.lang.Specification

/**
 * Converter controller unit test
 */
@TestFor(VideoConversionController)
class VideoConversionControllerSpec extends Specification {

	def setup() {
	}

	def "should trigger video conversion"() {
		given:
		controller.videoConversionService = Mock(VideoConversionService)
		GrailsMockMultipartFile videoFile = new GrailsMockMultipartFile('videoFile', 'some file contents'.bytes)
		request.addFile(videoFile)
		
		when:
		Map encodedVideoInfo = controller.convert()
		
		then:
		request.getFile("videoFile") == videoFile
		1 * controller.videoConversionService.convertToWebFormat(videoFile) >> [id: 100, test: true, outputs: [[id: 234341, url: "https://zencoder-temp-storage-us-east-1.s3.amazonaws.com/input/sample.dv"]]]
		view == '/videoConversion/show'
	}
	
	def cleanup() {
	}
	
}
