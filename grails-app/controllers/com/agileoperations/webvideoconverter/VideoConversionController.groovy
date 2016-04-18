package com.agileoperations.webvideoconverter

import org.springframework.web.multipart.MultipartFile

class VideoConversionController {
	
	VideoConversionService videoConversionService

	def index(){}
	
    def convert() {
		MultipartFile file = request.getFile("videoFile")
		Map encodedVideoInfo = videoConversionService.convertToWebFormat(file)
		log.info "Encoded Video Info: " + encodedVideoInfo
		Map jobStatus = videoConversionService.getJobStatus(encodedVideoInfo.id.toString())
		log.info "Job: " + jobStatus
		log.info "Job: " + encodedVideoInfo.id + ", Status: " + jobStatus.state
		while(jobStatus.state == "pending" || jobStatus.state == "waiting" || jobStatus.state == "processing"){
			Thread.sleep(5000)
			jobStatus = videoConversionService.getJobStatus(encodedVideoInfo.id.toString())
			log.info "Encoding job: " + encodedVideoInfo.id + ", Job status: " + jobStatus.state
			if(jobStatus.state == "finished"){
				log.info "Encoding job finished!"
				log.info "Job: " + jobStatus
				break;
			}
		}
		render(view: 'show', model: [encodedVideoInfo: encodedVideoInfo, jobStatus: jobStatus])
	}
}
