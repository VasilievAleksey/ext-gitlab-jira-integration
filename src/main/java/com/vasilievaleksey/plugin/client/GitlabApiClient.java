package com.vasilievaleksey.plugin.client;

import com.vasilievaleksey.plugin.dto.RepositoryDTO;
import com.vasilievaleksey.plugin.exception.PluginRestException;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GitlabApiClient {
    
    public RepositoryDTO.Response.RepositoryInfo getRepositoryInfo(RepositoryDTO.Request.RepositoryInfo repositoryInfoRequest) {
        GitLabApi gitLabApi = new GitLabApi(repositoryInfoRequest.hostUrl(), repositoryInfoRequest.getAccessToken());

        try {
            return gitLabApi.getProjectApi()
                    .getProjects(repositoryInfoRequest.repositoryName()).stream().findFirst()
                    .map(RepositoryDTO.Response.RepositoryInfo::new)
                    .orElseThrow(() -> new PluginRestException(HttpStatus.NOT_FOUND, "Repository not found"));
        } catch (GitLabApiException e) {
            throw new PluginRestException(e.getHttpStatus() == 0 ? HttpStatus.NOT_FOUND : HttpStatus.valueOf(e.getHttpStatus()), e.getReason());
        }
    }
}
