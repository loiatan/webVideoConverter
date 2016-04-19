package com.agileoperations.webvideoconverter

import org.springframework.web.multipart.MultipartFile

class VideoConversionController {
	
	VideoConversionService videoConversionService

	def index(){}
	
    def convert() {
		MultipartFile file = request.getFile("videoFile")
		Map encodedVideoInfo = videoConversionService.convertToWebFormat(file)
		log.info "Encoded Video Info: " + encodedVideoInfo
		render(view: 'show', model: encodedVideoInfo)
	}
}
