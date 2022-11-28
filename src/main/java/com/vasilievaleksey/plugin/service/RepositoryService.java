package com.vasilievaleksey.plugin.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.vasilievaleksey.plugin.dto.RepositoryDto;
import com.vasilievaleksey.plugin.model.Repository;
import com.vasilievaleksey.plugin.model.RepositoryStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class RepositoryService {
    @ComponentImport
    private final ActiveObjects ao;

    @Autowired
    public RepositoryService(ActiveObjects ao) {
        this.ao = ao;
    }

    public void add(RepositoryDto repositoryDto) {
        final Repository repository = ao.create(Repository.class);
        repository.setUrl(repositoryDto.getUrl());
        repository.setName(parseRepositoryName(repositoryDto.getUrl()));
        repository.setStatus(RepositoryStatus.NEW);
        repository.setLastUpdateTime(new Date());
        repository.setUsername(repositoryDto.getUsername());
        repository.setPassword(repositoryDto.getPassword());
        repository.setAccessToken(repositoryDto.getAccessToken());

        repository.save();
    }

    private String parseRepositoryName(String repositoryUrl) {
        String[] urlParts = repositoryUrl.split("/");

        return urlParts[urlParts.length-1].replace("\\.git", "");
    }
}
