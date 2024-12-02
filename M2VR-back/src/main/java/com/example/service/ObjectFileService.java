package com.example.service;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.example.entity.FileResult;
import org.springframework.web.multipart.MultipartFile;


public interface ObjectFileService {
	
	public InputStream getInputStream(String bucketname, String fileName, long offset) throws Exception;

	public InputStream getObj(String bucketname, String objName) throws Exception;

	public String getImageWidthHeight(String relativeMinioUrl);

	public BufferedImage getBufferedImage(String relativeMinioUrl);

	public InputStream getImageInputStream(String relativeMinioUrl);

	public void deleteFileFromMinio(String bucketname,String objectName,String fileBucketName);

	public void deleteFileFromMinio(String bucketname,String objectName);

	public void deleteFileFromMinio(String relatetiveUrl);

	public boolean isExistMinioFile(String bucketname,String objectName);
	
	public boolean isExistMinioFileAndDeleteNotComplete(String bucketname,String objectName);

	public long getMinioObjectLength(String bucketname,String objectName);

	public void removeBucketName(String fileBucketName);

	public void uploadPictureFile(List<File> fileList, String bucketName);

	public FileResult uploadVideoFile(File file) ;

	public List<FileResult> uploadFile(MultipartFile zipFile,String dataSetType) ;

	public String downLoadFileFromMinio(String bucketName, String objectName,String destPath) throws Exception ;

	public String downLoadFileFromMinioAndSetPictureName(String bucketName, String objectName,String pictureName) throws Exception;
	
	public List<String> listAllFile(String obsPath);

}