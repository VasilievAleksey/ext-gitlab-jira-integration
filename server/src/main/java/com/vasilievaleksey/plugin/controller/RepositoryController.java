package com.vasilievaleksey.plugin.controller;

import com.vasilievaleksey.plugin.dto.RepositoryDTO;
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
    public RepositoryDTO.Response.RepositoryInfo getInfo(@RequestBody RepositoryDTO.Request.Credential credential) {
        return repositoryService.getInfo(credential);
    }

    @PostMapping
    @RequestMapping("/clone")
    public void cloneNewRepository(@RequestBody RepositoryDTO.Request.Credential credential) {
        repositoryService.clone(credential);
    }
}
