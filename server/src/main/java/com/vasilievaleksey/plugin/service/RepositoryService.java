package com.vasilievaleksey.plugin.service;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.vasilievaleksey.plugin.client.GitlabApiClient;
import com.vasilievaleksey.plugin.dto.RepositoryDto;
import com.vasilievaleksey.plugin.dto.RepositoryInfoDto;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class RepositoryService {
    private GitlabApiClient gitlabApiClient;

    public RepositoryInfoDto getInfo(RepositoryDto repositoryDto) {
        final String hostUrl = parseHostUrl(repositoryDto.getUrl());
        final String repositoryName = parseRepositoryName(repositoryDto.getUrl());
        
        return createRepositoryInfoDto(gitlabApiClient.findProjectByName(hostUrl,repositoryDto.getAccessToken(), repositoryName));
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

    @SneakyThrows
    public void clone(RepositoryDto repositoryDto) {
        try (Git git = Git.cloneRepository()
                .setURI(repositoryDto.getUrl())
                .setBare(true)
                .setCloneAllBranches(true)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("gitlab-ci-token", repositoryDto.getAccessToken()))
                .setDirectory(Paths.get(getRepositoryPath(repositoryDto)).toFile())
                .call()) {

            saveConfig(git);
            System.out.println("Having repository: " + git.getRepository().getDirectory());
        }
    }

    private String getRepositoryPath(RepositoryDto repositoryDto) {
        return "./data/repository/" + parseRepositoryName(repositoryDto.getUrl());
    }

    private void saveConfig(Git git) {
        try {
            StoredConfig config = git.getRepository().getConfig();
            config.setBoolean("http", null, "sslVerify", false);
            config.save();
        } catch (IOException ex) {
            log.error("Failed to save config: " + ex.getLocalizedMessage());
        }
    }
}
