package com.vasilievaleksey.plugin.controller;

import com.vasilievaleksey.plugin.dto.RepositoryDto;
import com.vasilievaleksey.plugin.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Named
@Path("/repository")
public class RepositoryController {
    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void add(RepositoryDto repositoryDto) {
        repositoryService.add(repositoryDto);
    }
}
