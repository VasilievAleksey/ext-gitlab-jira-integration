package com.vasilievaleksey.plugin.mapper;

import com.vasilievaleksey.plugin.dto.RepositoryDto;
import com.vasilievaleksey.plugin.model.Repository;
import com.vasilievaleksey.plugin.model.RepositoryStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RepositoryMapper {

    public RepositoryDto convertToDTO(Repository repository) {
        return RepositoryDto.builder()
                .id(repository.getID())
                .name(repository.getName())
                .url(repository.getUrl())
                .accessToken(repository.getAccessToken())
                .status(repository.getStatus().name())
                .build();
    }

    public Repository mapDtoToEntity(RepositoryDto repositoryDto, Repository repository) {
        repository.setUrl(repositoryDto.getUrl());
        repository.setName(parseRepositoryName(repositoryDto.getUrl()));
        repository.setStatus(RepositoryStatus.NEW);
        repository.setLastUpdateTime(new Date());
        repository.setAccessToken(repositoryDto.getAccessToken());

        return repository;
    }

    private String parseRepositoryName(String repositoryUrl) {
        String[] urlParts = repositoryUrl.split("/");

        return urlParts[urlParts.length-1].replace("\\.git", "");
    }
}
