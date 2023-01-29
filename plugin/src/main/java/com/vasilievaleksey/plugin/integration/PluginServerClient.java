package com.vasilievaleksey.plugin.integration;

import com.google.gson.Gson;
import com.vasilievaleksey.plugin.dto.RepositoryDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class PluginServerClient {
    private RestTemplate restTemplate;
    private Gson gson;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        gson = new Gson();
    }

    public RepositoryDTO.Response.RepositoryInfo getRepositoryInfo(RepositoryDTO.Request.Credential credential) {
        return gson.fromJson(doRequest("repository", HttpMethod.POST, createHttpEntity(credential)), RepositoryDTO.Response.RepositoryInfo.class);
    }

    public void cloneNewRepository(RepositoryDTO.Request.Credential credential) {
        doRequest("repository/clone", HttpMethod.POST, createHttpEntity(credential));
    }

    private String doRequest(String urlPath, HttpMethod httpMethod, HttpEntity<String> requestEntity) {
        return restTemplate.exchange("http://localhost:8080/" + urlPath, httpMethod, requestEntity, String.class).getBody();
    }

    private <T> HttpEntity<String> createHttpEntity(T object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return new HttpEntity<>(gson.toJson(object), headers);
    }
}
