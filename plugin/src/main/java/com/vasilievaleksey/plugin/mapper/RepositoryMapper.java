package com.vasilievaleksey.plugin.mapper;

import com.vasilievaleksey.plugin.dto.RepositoryDTO;
import com.vasilievaleksey.plugin.model.Repository;
import com.vasilievaleksey.plugin.model.RepositoryStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RepositoryMapper {

    public RepositoryDTO.Response.Repository convertToDTO(Repository repository) {
        return new RepositoryDTO.Response.Repository(
                repository.getID(),
                repository.getName(),
                repository.getDescription(),
                repository.getUrl(),
                repository.getAccessToken(),
                repository.getAccessToken());
    }

    public Repository mapDtoToEntity(RepositoryDTO.Request.Credential credential, RepositoryDTO.Response.RepositoryInfo repositoryInfo, Repository repository) {
        repository.setUrl(credential.getUrl());
        repository.setName(repositoryInfo.getName());
        repository.setStatus(RepositoryStatus.NEW);
        repository.setLastUpdateTime(new Date());
        repository.setAccessToken(credential.getAccessToken());
        repository.setDescription(repositoryInfo.getDescription());
        repository.setGitId(repositoryInfo.getId());

        return repository;
    }
}
