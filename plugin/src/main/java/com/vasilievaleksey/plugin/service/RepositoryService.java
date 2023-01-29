package com.vasilievaleksey.plugin.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.vasilievaleksey.plugin.dto.RepositoryDTO;
import com.vasilievaleksey.plugin.integration.PluginServerClient;
import com.vasilievaleksey.plugin.mapper.RepositoryMapper;
import com.vasilievaleksey.plugin.model.Repository;
import com.vasilievaleksey.plugin.model.RepositoryStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.java.ao.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    private final PluginServerClient pluginServerClient;

    public List<RepositoryDTO.Response.Repository> getAll() {
        return Arrays.stream(activeObjects.find(Repository.class))
                .map(repositoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public RepositoryDTO.Response.Repository create(RepositoryDTO.Request.Credential credential) {
        RepositoryDTO.Response.RepositoryInfo repositoryInfo = pluginServerClient.getRepositoryInfo(credential);
        Repository repository = repositoryMapper.mapDtoToEntity(credential, repositoryInfo, activeObjects.create(Repository.class));

        repository.save();

        return repositoryMapper.convertToDTO(repository);
    }

    public RepositoryDTO.Response.Repository update(int id, RepositoryDTO.Request.Credential credential) {
        Repository repository = activeObjects.get(Repository.class, id);

        Optional.ofNullable(credential.getUrl()).ifPresent(repository::setUrl);
        Optional.ofNullable(credential.getAccessToken()).ifPresent(repository::setAccessToken);

        repository.save();
        return repositoryMapper.convertToDTO(repository);
    }

    public void delete(int id) {
        activeObjects.delete(activeObjects.get(Repository.class, id));
    }

    public void cloneNewRepositories() {
        Repository[] repositories = activeObjects.find(Repository.class, Query.select().where("STATUS = ?", RepositoryStatus.NEW));

        for (Repository repository : repositories) {
            pluginServerClient.cloneNewRepository(new RepositoryDTO.Request.Credential(repository.getUrl(), repository.getAccessToken()));
            repository.setStatus(RepositoryStatus.SYNCHRONIZED);
        }
    }
}
