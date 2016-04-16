package com.agileoperations.webvideoconverter

import grails.test.mixin.TestFor

import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile

import spock.lang.Specification

/**
 * Unit test for VideoConversionService
 */
@TestFor(VideoConversionService)
class VideoConversionServiceSpec extends Specification {

    def setup() {
    }

	@Ignore
	void "should convert input video to web format"() {
		given:
		GrailsMockMultipartFile videoFile = new GrailsMockMultipartFile('videoFile', 'some file contents'.bytes)
		
		when:
		service.convertToWebFormat(videoFile)
		
		then:
		1 * service.amazonS3Service.upload(videoFile) >> [sourceVideoFile: "s3://webVideoConverter/input/timestamp/sample.dv"]
		1 * service.zencoderClient.encodeToWeb([sourceVideoFile: "s3://webVideoConverter/input/timestamp/sample.dv"]) >> [encodedVideoFile: "s3://webVideoConverter/output/timestamp/sample.mpg"]
	}
	
    def cleanup() {
    }
}
