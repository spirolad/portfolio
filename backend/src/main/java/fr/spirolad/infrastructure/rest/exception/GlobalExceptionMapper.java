package fr.spirolad.infrastructure.rest.exception;

import fr.spirolad.domain.exception.EducationInvalideException;
import fr.spirolad.domain.exception.EducationNotFoundException;
import fr.spirolad.domain.exception.ExperienceInvalideException;
import fr.spirolad.domain.exception.ExperienceNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        if (exception instanceof EducationNotFoundException || exception instanceof ExperienceNotFoundException) {
            return error(Response.Status.NOT_FOUND, exception);
        }

        if (exception instanceof EducationInvalideException || exception instanceof ExperienceInvalideException || exception instanceof IllegalArgumentException) {
            return error(Response.Status.BAD_REQUEST, exception);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("error", exception.getMessage() == null ? "Unexpected error" : exception.getMessage()))
                .build();
    }

    private Response error(Response.Status status, RuntimeException exception) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("error", exception.getMessage()))
                .build();
    }
}