package com.vasilievaleksey.plugin.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.vasilievaleksey.plugin.dto.RepositoryDto;
import com.vasilievaleksey.plugin.dto.RepositoryInfoDto;
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

    public List<RepositoryDto> getAll() {
        return Arrays.stream(activeObjects.find(Repository.class))
                .map(repositoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public RepositoryDto create(RepositoryDto repositoryDto) {
        RepositoryInfoDto repositoryInfoDto = pluginServerClient.getRepositoryInfo(repositoryDto);
        Repository repository = repositoryMapper.mapDtoToEntity(repositoryDto, repositoryInfoDto, activeObjects.create(Repository.class));

        repository.save();

        return repositoryMapper.convertToDTO(repository);
    }

    public RepositoryDto update(int id, RepositoryDto repositoryDto) {
        Repository repository = activeObjects.get(Repository.class, id);

        Optional.ofNullable(repositoryDto.getUrl()).ifPresent(repository::setUrl);
        Optional.ofNullable(repositoryDto.getAccessToken()).ifPresent(repository::setAccessToken);

        repository.save();
        return repositoryMapper.convertToDTO(repository);
    }

    public void delete(int id) {
        activeObjects.delete(activeObjects.get(Repository.class, id));
    }

    public void cloneNewRepositories() {
        Repository[] repositories = activeObjects.find(Repository.class, Query.select().where("STATUS = ?", RepositoryStatus.NEW));

        for (Repository repository : repositories) {
            pluginServerClient.cloneNewRepository(repositoryMapper.convertToDTO(repository));
            repository.setStatus(RepositoryStatus.SYNCHRONIZED);
        }

    }
}
