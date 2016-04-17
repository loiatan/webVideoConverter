package com.agileoperations.webvideoconverter

import grails.transaction.Transactional

import org.springframework.web.multipart.MultipartFile

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.event.ProgressEvent
import com.amazonaws.event.ProgressListener
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.*

@Transactional
class AmazonS3Service {
	
	DefaultAWSCredentialsProviderChain credentialProviderChain
	TransferManager transferManager
	FileFactory fileFactory
	
	public AmazonS3Service(){
		credentialProviderChain = new DefaultAWSCredentialsProviderChain()
		transferManager = new TransferManager(credentialProviderChain.getCredentials())
		fileFactory = new FileFactory()
	}

    public Map upload(MultipartFile multipartFile) {
		
		Map uploadedS3FileInfo = [:]
		
		// Get timestamp for uploaded file
		uploadedS3FileInfo.timestamp = new Date().time
		
		// Get filename
		uploadedS3FileInfo.multipartFilename = multipartFile.getOriginalFilename()
		log.info "fileName: " + uploadedS3FileInfo.multipartFilename
		
		// Create file with data on tmp dir
		File videoFile = fileFactory.getTransferedFile(uploadedS3FileInfo.multipartFilename)	
		log.info "videoFile: " + videoFile.absolutePath
		
		// Transfer file to tmp dir
		multipartFile.transferTo(videoFile)
				
		// Set bucket and objectKey to upload to S3
		uploadedS3FileInfo.bucketName = "agileoperations.com.br"
		uploadedS3FileInfo.objectKey = "webvideoconverter/input/${uploadedS3FileInfo.timestamp}/" + uploadedS3FileInfo.multipartFilename
		
		// Set S3 Object Request
		PutObjectRequest putObjectRequest = new PutObjectRequest(uploadedS3FileInfo.bucketName, uploadedS3FileInfo.objectKey, videoFile).withCannedAcl(CannedAccessControlList.Private)

		// Instantiate Upload process
		def upload = transferManager.upload(putObjectRequest)

		// Get liestener to monitor process		
		ProgressListener myProgressListener = getProgressListener(upload)
		upload.addProgressListener(myProgressListener)
		
		// Wait process to finish
		upload.waitForCompletion()
		
		// Shutdown transfer manager
		transferManager.shutdownNow()

		// Set uploaded location of the file into uploadedS3FileInfo Map		
		uploadedS3FileInfo.uploadedFileLocation = "s3://" + uploadedS3FileInfo.bucketName + "/" + uploadedS3FileInfo.objectKey
		
		return uploadedS3FileInfo
	}
	
	public ProgressListener getProgressListener(upload){
		ProgressListener progressListener = new ProgressListener() {
			// This method is called periodically as your transfer progresses
			public void progressChanged(ProgressEvent progressEvent) {
				double percentageCompleted = upload.getProgress().getPercentTransferred()
				log.info "Transfer: $upload.description"
				log.info "  - State: $upload.state"
				log.info "  - Progress: $percentageCompleted" + "%"
		 
				if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
					log.info  "Upload complete!"
				}
				Thread.sleep(2000)
			}
		}
		return progressListener
	}
}
