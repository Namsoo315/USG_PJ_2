//package com.usg.book.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.ResourceUtils;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//public class GoogleCloudStorageConfig {
//
//    private final String keyFilename;
//
//    public GoogleCloudStorageConfig(@Value("${spring.cloud.gcp.storage.credentials.location}") String keyFilename) {
//        this.keyFilename = keyFilename;
//    }
//
//    @Bean
//    public Storage storage() throws IOException {
//        InputStream keyFile = ResourceUtils.getURL(keyFilename).openStream();
//        return StorageOptions.newBuilder()
//                .setCredentials(GoogleCredentials.fromStream(keyFile))
//                .build()
//                .getService();
//    }
//}
