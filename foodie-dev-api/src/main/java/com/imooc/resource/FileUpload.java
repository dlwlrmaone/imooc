package com.imooc.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.face")
@PropertySource("classpath:file-upload-dev.properties")
public class FileUpload {

    private String faceUploadLocation;

    public String getFaceUploadLocation() {
        return faceUploadLocation;
    }

    public void setFaceUploadLocation(String faceUploadLocation) {
        this.faceUploadLocation = faceUploadLocation;
    }
}
