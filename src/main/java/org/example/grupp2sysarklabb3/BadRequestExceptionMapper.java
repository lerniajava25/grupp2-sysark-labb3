package org.example.grupp2sysarklabb3;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", getLastNodeName(exception.getMessage())))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private static String getLastNodeName(String path) {
        return path.substring(path.lastIndexOf('.') + 1);
    }
}
