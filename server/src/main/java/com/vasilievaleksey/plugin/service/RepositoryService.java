package com.vasilievaleksey.plugin.service;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.vasilievaleksey.plugin.dto.RepositoryDto;
import com.vasilievaleksey.plugin.dto.RepositoryInfoDto;
import lombok.extern.log4j.Log4j2;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Log4j2
public class RepositoryService {

    public RepositoryInfoDto getInfo(RepositoryDto repositoryDto) {
        final String repositoryName = parseRepositoryName(repositoryDto.getUrl());
        final String hostUrl = parseHostUrl(repositoryDto.getUrl());

        GitLabApi gitLabApi = new GitLabApi(hostUrl, repositoryDto.getAccessToken());

        try {
            return gitLabApi.getProjectApi()
                    .getProjects(repositoryName).stream().findFirst()
                    .map(this::createRepositoryInfoDto)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repository not found"));
        } catch (GitLabApiException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getHttpStatus()), e.getReason());
        }
    }

    private RepositoryInfoDto createRepositoryInfoDto(Project project) {
        return RepositoryInfoDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .build();
    }

    private String parseRepositoryName(String repositoryUrl) {
        String[] urlParts = repositoryUrl.split("/");

        return urlParts[urlParts.length - 1].replace(".git", "");
    }

    private String parseHostUrl(String repositoryUrl) {
        UrlDetector parser = new UrlDetector(repositoryUrl, UrlDetectorOptions.Default);
        Optional<Url> urlOptional = parser.detect().stream().findFirst();

        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            return url.getScheme() + "://" + url.getHost();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect repository URL: " + repositoryUrl);
    }
}
