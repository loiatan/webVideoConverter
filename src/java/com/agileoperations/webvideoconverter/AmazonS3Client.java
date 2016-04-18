package com.agileoperations.webvideoconverter;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class AmazonS3Client {
	
	// Default SDK credentials provider 
	DefaultAWSCredentialsProviderChain credentialProviderChain;
	
	// TransferManager from AWS SDK to manager file transfers to S3
	TransferManager transferManager;
	
	// Factory to manage file creation
	FileFactory fileFactory;
	
	// Injecting log4j
	Logger log = Logger.getLogger(getClass());
	
	public AmazonS3Client(){
		credentialProviderChain = new DefaultAWSCredentialsProviderChain();
		transferManager = new TransferManager(credentialProviderChain.getCredentials());
		fileFactory = new FileFactory();
	}

    public Map<String, Object> upload(MultipartFile multipartFile) throws IllegalStateException, IOException, AmazonServiceException, AmazonClientException, InterruptedException {
		
		Map<String, Object> uploadedS3FileInfo = new HashMap<String, Object>();
		
		// Get timestamp for uploaded file
		uploadedS3FileInfo.put("timestamp",new Date().getTime());
		
		// Get filename
		uploadedS3FileInfo.put("multipartFilename", multipartFile.getOriginalFilename());
		log.info("fileName: " + uploadedS3FileInfo.get("multipartFilename"));
		
		// Create file with data on tmp dir
		File videoFile = fileFactory.getTransferedFile(uploadedS3FileInfo.get("multipartFilename").toString());
		log.info("videoFile: " + videoFile.getAbsolutePath());
		
		// Transfer file to tmp dir
		multipartFile.transferTo(videoFile);
				
		// Set bucket and objectKey to upload to S3
		uploadedS3FileInfo.put("bucketName", "agileoperations.com.br");
		uploadedS3FileInfo.put("objectKey", "webvideoconverter/input/" + uploadedS3FileInfo.get("timestamp") + "/" + uploadedS3FileInfo.get("multipartFilename"));
		
		// Set S3 Object Request
		PutObjectRequest putObjectRequest = new PutObjectRequest(
				(String) uploadedS3FileInfo.get("bucketName"), 
				(String) uploadedS3FileInfo.get("objectKey"), 
				(File) videoFile
		).withCannedAcl(CannedAccessControlList.Private);

		// Instantiate Upload process
		Upload upload = transferManager.upload(putObjectRequest);

		// Get listner to monitor process		
		ProgressListener myProgressListener = getProgressListener(upload, videoFile.getAbsolutePath());
		upload.addProgressListener(myProgressListener);
		
		// Wait process to finish
		upload.waitForCompletion();
		
		// Set uploaded location of the file into uploadedS3FileInfo Map		
		uploadedS3FileInfo.put("uploadedFileLocation", "s3://" + uploadedS3FileInfo.get("bucketName") + "/" + uploadedS3FileInfo.get("objectKey"));
		
		return uploadedS3FileInfo;
	}
	
	private ProgressListener getProgressListener(final Upload upload, final String absoluteFilePath){
		ProgressListener progressListener = new ProgressListener() {
			// This method is called periodically as your transfer progresses
			public void progressChanged(ProgressEvent progressEvent) {
				double percentageCompleted = upload.getProgress().getPercentTransferred();
				log.info("Transfer: " + absoluteFilePath);
				log.info("  - " + upload.getDescription());
				log.info("  - State: " + upload.getState());
				log.info("  - Progress: " + percentageCompleted + "%");
				
				if (progressEvent.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT) {
					log.info("Upload complete!");
				}
			}
		};
		return progressListener;
	}
}
