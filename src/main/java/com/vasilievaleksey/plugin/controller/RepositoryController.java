package com.vasilievaleksey.plugin.controller;

import com.vasilievaleksey.plugin.dto.RepositoryDTO;
import com.vasilievaleksey.plugin.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("repository")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class RepositoryController {
    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GET
    @Path("all")
    public Response getAll() {
        return Response.ok(repositoryService.getAll()).build();
    }

    @POST
    public Response create(RepositoryDTO.Request.RepositoryInfo repositoryInfoRequest) {
        return Response.ok(repositoryService.create(repositoryInfoRequest)).build();
    }

    @PUT
    @Path ("{id}")
    public Response update(@PathParam("id") int id, RepositoryDTO.Request.RepositoryInfo repositoryInfoRequest) {
        return Response.ok(repositoryService.update(id, repositoryInfoRequest)).build();
    }

    @DELETE
    @Path ("{id}")
    public Response delete(@PathParam ("id") int id) {
        repositoryService.delete(id);
        return Response.ok().build();
    }
}
