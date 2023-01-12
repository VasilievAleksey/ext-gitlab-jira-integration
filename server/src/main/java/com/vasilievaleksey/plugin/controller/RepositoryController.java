package com.vasilievaleksey.plugin.controller;

import com.vasilievaleksey.plugin.dto.RepositoryDto;
import com.vasilievaleksey.plugin.dto.RepositoryInfoDto;
import com.vasilievaleksey.plugin.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repository")
public class RepositoryController {
    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping
    public RepositoryInfoDto getInfo(@RequestBody RepositoryDto repositoryDto) {
        return repositoryService.getInfo(repositoryDto);
    }
}
