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
		controller.convert()
		
		then:
		request.getFile("videoFile") == videoFile
		1 * controller.videoConversionService.convertToWebFormat(videoFile)
		response.redirectedUrl == '/videoConversion/list'
	}
	
	def cleanup() {
	}
	
}
