package com.imooc.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.face")
@PropertySource("classpath:file-upload-dev.properties")
public class FileUpload {

    private String userFaceLocation;
    private String userFaceServerUrl;

    public String getUserFaceLocation() {
        return userFaceLocation;
    }

    public void setUserFaceLocation(String userFaceLocation) {
        this.userFaceLocation = userFaceLocation;
    }

    public String getUserFaceServerUrl() {
        return userFaceServerUrl;
    }

    public void setUserFaceServerUrl(String userFaceServerUrl) {
        this.userFaceServerUrl = userFaceServerUrl;
    }
}
