package com.harlyn.domain.problems;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wannabe on 20.11.15.
 */
public class SubmitData {
    private String queryParam;
    private MultipartFile fileParam;

    public SubmitData(String queryParam, MultipartFile fileParam) {
        this.queryParam = queryParam;
        this.fileParam = fileParam;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public SubmitData setQueryParam(String queryParam) {
        this.queryParam = queryParam;
        return this;
    }

    public MultipartFile getFileParam() {
        return fileParam;
    }

    public SubmitData setFileParam(MultipartFile fileParam) {
        this.fileParam = fileParam;
        return this;
    }
}
