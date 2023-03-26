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

    public Repository mapDtoToEntity(RepositoryDTO.Response.RepositoryInfo repositoryInfoResponse, String url, String accessToken, Repository repository) {
        repository.setUrl(url);
        repository.setName(repositoryInfoResponse.getName());
        repository.setStatus(RepositoryStatus.NEW);
        repository.setLastUpdateTime(new Date());
        repository.setAccessToken(accessToken);
        repository.setDescription(repositoryInfoResponse.getDescription());
        repository.setGitId(repositoryInfoResponse.getId());

        return repository;
    }
}
