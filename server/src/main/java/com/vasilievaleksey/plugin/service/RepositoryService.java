package com.vasilievaleksey.plugin.service;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.vasilievaleksey.plugin.client.GitlabApiClient;
import com.vasilievaleksey.plugin.dto.RepositoryDTO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
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

    public RepositoryDTO.Response.RepositoryInfo getInfo(RepositoryDTO.Request.Credential credential) {
        return createRepositoryInfoDto(gitlabApiClient.findProjectByName(credential.hostUrl(), credential.accessToken(), credential.repositoryName()));
    }

    private RepositoryDTO.Response.RepositoryInfo createRepositoryInfoDto(Project project) {
        return new RepositoryDTO.Response.RepositoryInfo(project.getId(), project.getName(), project.getDescription());
    }

    @SneakyThrows
    public void clone(RepositoryDTO.Request.Credential credential) {
        try (Git git = Git.cloneRepository()
                .setURI(credential.url())
                .setBare(true)
                .setCloneAllBranches(true)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("gitlab-ci-token", credential.accessToken()))
                .setDirectory(Paths.get(getRepositoryPath(credential.repositoryName())).toFile())
                .call()) {

            saveConfig(git);
            System.out.println("Having repository: " + git.getRepository().getDirectory());
        }
    }

    private String getRepositoryPath(String repositoryName) {
        return "./data/repository/" + repositoryName;
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
