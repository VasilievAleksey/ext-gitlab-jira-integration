package com.vasilievaleksey.plugin.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PluginExceptionMapper implements ExceptionMapper<PluginRestException> {

    @Override
    public Response toResponse(PluginRestException exception) {
        return Response.status(exception.getHttpStatusCode()).entity(exception.getMessage()).build();
    }
}