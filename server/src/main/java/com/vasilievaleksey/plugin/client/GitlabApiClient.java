package com.vasilievaleksey.plugin.client;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GitlabApiClient {
    
    public Project findProjectByName(String hostUrl, String accessToken, String name) {
        GitLabApi gitLabApi = new GitLabApi(hostUrl, accessToken);

        try {
            return gitLabApi.getProjectApi()
                    .getProjects(name).stream().findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repository not found"));
        } catch (GitLabApiException e) {
            throw new ResponseStatusException(e.getHttpStatus() == 0 ? HttpStatus.NOT_FOUND : HttpStatus.valueOf(e.getHttpStatus()), e.getReason());
        }
    }
}
