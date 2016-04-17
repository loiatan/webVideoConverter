package com.agileoperations.webvideoconverter

import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartFile

@Transactional
class VideoConversionService {
	
	AmazonS3Client amazonS3Client
	
    void convertToWebFormat(MultipartFile videoFile) {
		amazonS3Client = amazonS3Client == null ? new AmazonS3Client() : amazonS3Client
		Map uploadedS3FileInfo = amazonS3Client.upload(videoFile)
    }
}
