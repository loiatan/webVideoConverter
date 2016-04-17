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

	def "should convert input video to web format"() {
		given:
		GrailsMockMultipartFile videoFile = Mock()
		File file = new File("\\tmp\\sample.dv").withWriter('UTF-8') { writer ->
			writer.write('some content')
		}
		service.amazonS3Client = Mock(AmazonS3Client)
		//service.zencoderClient = new ZencoderClient()
		
		when:
		service.convertToWebFormat(videoFile)
		
		then:
		1 * service.amazonS3Client.upload(videoFile) >> [sourceVideoFile: "s3://agileoperations.com.br/webvideoconverter/input/timestamp/sample.dv"]
		//1 * service.zencoderClient.encodeToWeb([sourceVideoFile: "s3://webVideoConverter/input/timestamp/sample.dv"]) >> [encodedVideoFile: "s3://webVideoConverter/output/timestamp/sample.mpg"]
	}
	
    def cleanup() {
    }
}
