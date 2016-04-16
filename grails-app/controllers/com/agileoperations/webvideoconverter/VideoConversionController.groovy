package com.agileoperations.webvideoconverter

import org.springframework.web.multipart.MultipartFile

class VideoConversionController {
	
	VideoConversionService videoConversionService

    def convert() {
		MultipartFile file = request.getFile("videoFile")
		videoConversionService.convertToWebFormat(file)
		redirect(action: 'list')
	}
	
}
