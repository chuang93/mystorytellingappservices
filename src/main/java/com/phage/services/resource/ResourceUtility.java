package com.phage.services.resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class ResourceUtility {
    public static ResponseEntity getResponseOkJson(Object o){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(headers).body(o);
    }

}
