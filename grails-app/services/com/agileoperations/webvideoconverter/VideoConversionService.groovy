package com.agileoperations.webvideoconverter

import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartFile

@Transactional
class VideoConversionService {
	
    void convertToWebFormat(MultipartFile videoFile) {
		def uploadedS3FileInfo = new AmazonS3Client().upload(videoFile)
    }
}
