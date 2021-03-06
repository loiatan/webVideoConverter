package com.agileoperations.webvideoconverter

import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification

/**
 * Integration tests for AmazonS3Client
 */
class S3ClientIntegrationSpec extends Specification {
	
    def setup() {
    }

	void "should upload a file"() {
		given:
		S3Client client = new S3Client()
		GrailsMockMultipartFile multipartFile = Mock()
		File file = new File("\\tmp\\sample.dv").withWriter('UTF-8') { writer ->
			writer.write('some content')
		}
		
		when:
		Map uploadedS3FileInfo = client.upload(multipartFile)
		
		then:
		1 * multipartFile.getOriginalFilename() >> "sample.dv"
		
		uploadedS3FileInfo.uploadedFileLocation.contains("s3://agileoperations.com.br/webvideoconverter/input/")
	}
	
    def cleanup() {
		//amazonWebService.s3.deleteObject('uploadedS3FileInfo.bucketName', 'uploadedS3FileInfo.objectKey')
    }

}
