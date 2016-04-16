package com.agileoperations.webvideoconverter

import grails.test.mixin.TestFor

import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile

import spock.lang.Specification

/**
 * Integration tests for AmazonS3Service
 */
@TestFor(AmazonS3Service)
class AmazonS3ServiceIntegrationSpec extends Specification {
	
    def setup() {
    }

	void "should upload a file"() {
		given:
		GrailsMockMultipartFile multipartFile = Mock()
		File file = new File("\\tmp\\sample.dv").withWriter('UTF-8') { writer ->
			writer.write('some content')
		}
		
		when:
		Map uploadedS3FileInfo = service.upload(multipartFile)
		
		then:
		1 * multipartFile.getOriginalFilename() >> "sample.dv"
		//1 * service.fileFactory.getTransferedFile("sample.dv") >> file
		//1 * multipartFile.transferTo(file)
		//1 * multipartFile.getBytes("\\tmp\\sample.dv") >> "some content".bytes
		
		uploadedS3FileInfo.uploadedFileLocation.contains("s3://agileoperations.com.br/webvideoconverter/input/")
	}
	
    def cleanup() {
		//amazonWebService.s3.deleteObject('uploadedS3FileInfo.bucketName', 'uploadedS3FileInfo.objectKey')
    }

}
