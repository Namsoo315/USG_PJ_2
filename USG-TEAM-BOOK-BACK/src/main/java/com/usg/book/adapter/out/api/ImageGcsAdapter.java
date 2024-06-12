package com.usg.book.adapter.out.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.usg.book.application.port.out.BookImageGcsPort;
import com.usg.book.domain.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Component
public class ImageGcsAdapter implements BookImageGcsPort {

    private final String keyFilename;
    private final String bucketName;
    private final String projectId;

    public ImageGcsAdapter(@Value("${spring.cloud.gcp.storage.credentials.location}") String keyFilename,
                           @Value("${spring.cloud.gcp.storage.bucket}") String bucketName,
                           @Value("${spring.cloud.gcp.storage.project-id}") String projectId) {

        this.keyFilename = keyFilename;
        this.bucketName = bucketName;
        this.projectId = projectId;
    }

    @Override
    public String uploadImage(Image image) {

        try {
            InputStream keyFile = ResourceUtils.getURL(keyFilename).openStream();
            
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(keyFile))
                    .build()
                    .getService();

            MultipartFile multipartFile = image.getImage();
            String ext = multipartFile.getContentType();
            String storeFilename = image.getStoreFilename();
        
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, storeFilename)
                    .setContentType(ext)
                    .build();

            Blob blob = storage.create(blobInfo, multipartFile.getInputStream());

            return "https://storage.googleapis.com/" + bucketName + "/" + storeFilename;
        } catch (Exception e) {
            log.info("error : {}", e.getMessage());
            throw new IllegalArgumentException("Image Error");
        }
            
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            InputStream keyFile = ResourceUtils.getURL(keyFilename).openStream();

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(keyFile))
                    .build()
                    .getService();

            String[] urlParts = imageUrl.split("/");
            String bucketName = urlParts[3];
            String objectName = urlParts[urlParts.length - 1];

            storage.delete(BlobId.of(bucketName, objectName));
        } catch (Exception e) {
            log.error("Error deleting image from GCS: {}", e.getMessage());
            throw new IllegalArgumentException("Image Deletion Error");
        }
    }
}
