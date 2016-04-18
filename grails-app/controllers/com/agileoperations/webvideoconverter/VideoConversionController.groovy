package com.agileoperations.webvideoconverter

import org.springframework.web.multipart.MultipartFile

class VideoConversionController {
	
	VideoConversionService videoConversionService

	def index(){}
	
    def convert() {
		MultipartFile file = request.getFile("videoFile")
		Map encodedVideoInfo = videoConversionService.convertToWebFormat(file)
		Map jobStatus = videoConversionService.getJobStatus(encodedVideoInfo.outputs[0].id.toString())
		while(jobStatus.state == "progress"){
			jobStatus = videoConversionService.getJobStatus(encodedVideoInfo.outputs[0].id)
			log.info "Job: " + encodedVideoInfo.outputs[0].id + ", Status: " + jobStatus.state
			Thread.sleep(5000)
		}
		render(view: 'show', model: jobStatus)
	}
}
