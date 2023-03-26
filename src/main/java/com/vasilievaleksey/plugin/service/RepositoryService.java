package com.vasilievaleksey.plugin.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.vasilievaleksey.plugin.client.GitlabApiClient;
import com.vasilievaleksey.plugin.dto.RepositoryDTO;
import com.vasilievaleksey.plugin.mapper.RepositoryMapper;
import com.vasilievaleksey.plugin.model.Repository;
import com.vasilievaleksey.plugin.model.RepositoryStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.java.ao.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RepositoryService {
    @ComponentImport
    private final ActiveObjects activeObjects;
    private final RepositoryMapper repositoryMapper;
    private final GitlabApiClient gitlabApiClient;

    public List<RepositoryDTO.Response.Repository> getAll() {
        return Arrays.stream(activeObjects.find(Repository.class))
                .map(repositoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public RepositoryDTO.Response.Repository create(RepositoryDTO.Request.RepositoryInfo repositoryInfoRequest) {
        RepositoryDTO.Response.RepositoryInfo repositoryInfoResponse = gitlabApiClient.getRepositoryInfo(repositoryInfoRequest);

        Repository repository = repositoryMapper.mapDtoToEntity(repositoryInfoResponse, repositoryInfoRequest.getUrl(), repositoryInfoRequest.getAccessToken(), activeObjects.create(Repository.class));

        repository.save();

        return repositoryMapper.convertToDTO(repository);
    }

    public RepositoryDTO.Response.Repository update(int id, RepositoryDTO.Request.RepositoryInfo repositoryInfoRequest) {
        Repository repository = activeObjects.get(Repository.class, id);

        Optional.ofNullable(repositoryInfoRequest.getUrl()).ifPresent(repository::setUrl);
        Optional.ofNullable(repositoryInfoRequest.getAccessToken()).ifPresent(repository::setAccessToken);

        repository.save();
        return repositoryMapper.convertToDTO(repository);
    }

    public void delete(int id) {
        activeObjects.delete(activeObjects.get(Repository.class, id));
    }

    public void cloneNewRepositories() {
        Repository[] repositories = activeObjects.find(Repository.class, Query.select().where("STATUS = ?", RepositoryStatus.NEW));

        for (Repository repository : repositories) {
            repository.setLastUpdateTime(new Date());
            repository.setStatus(RepositoryStatus.SYNCHRONIZED);
            repository.save();
        }
    }
}
