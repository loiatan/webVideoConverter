package com.agileoperations.webvideoconverter

import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartFile

@Transactional
class VideoConversionService {
	
	AmazonS3Service amazonS3Service

    void convertToWebFormat(MultipartFile videoFile) {
		def uploadedS3FileInfo = amazonS3Service.upload(videoFile)
    }
}
