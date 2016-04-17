package com.agileoperations.webvideoconverter

import org.springframework.web.multipart.MultipartFile

class VideoConversionController {
	
	VideoConversionService videoConversionService

    def convert() {
		MultipartFile file = request.getFile("videoFile")
		Map encodedVideoInfo = videoConversionService.convertToWebFormat(file)
		render(view: 'show', model: encodedVideoInfo)
	}
	
}
