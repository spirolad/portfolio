package fr.spirolad.infrastructure.rest.exception;

import fr.spirolad.domain.exception.EducationInvalideException;
import fr.spirolad.domain.exception.EducationNotFoundException;
import fr.spirolad.domain.exception.ExperienceInvalideException;
import fr.spirolad.domain.exception.ExperienceNotFoundException;
import fr.spirolad.domain.exception.ProjectNotFoundException;
import fr.spirolad.domain.exception.SkillInvalideException;
import fr.spirolad.domain.exception.SkillNotFoundException;
import fr.spirolad.domain.exception.CategoryNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Map<Class<?>, Response.Status> NOT_FOUND_EXCEPTIONS = Map.ofEntries(
            Map.entry(EducationNotFoundException.class, Response.Status.NOT_FOUND),
            Map.entry(ExperienceNotFoundException.class, Response.Status.NOT_FOUND),
                Map.entry(ProjectNotFoundException.class, Response.Status.NOT_FOUND),
            Map.entry(SkillNotFoundException.class, Response.Status.NOT_FOUND),
            Map.entry(CategoryNotFoundException.class, Response.Status.NOT_FOUND)
    );

    private static final Map<Class<?>, Response.Status> BAD_REQUEST_EXCEPTIONS = Map.ofEntries(
            Map.entry(EducationInvalideException.class, Response.Status.BAD_REQUEST),
            Map.entry(ExperienceInvalideException.class, Response.Status.BAD_REQUEST),
            Map.entry(SkillInvalideException.class, Response.Status.BAD_REQUEST),
            Map.entry(IllegalArgumentException.class, Response.Status.BAD_REQUEST)
    );

    @Override
    public Response toResponse(RuntimeException exception) {
        Response.Status status = resolveStatus(exception);
        return buildErrorResponse(status, exception);
    }

    private Response.Status resolveStatus(RuntimeException exception) {
        Class<?> exceptionClass = exception.getClass();

        if (NOT_FOUND_EXCEPTIONS.containsKey(exceptionClass)) {
            return Response.Status.NOT_FOUND;
        }

        if (BAD_REQUEST_EXCEPTIONS.containsKey(exceptionClass)) {
            return Response.Status.BAD_REQUEST;
        }

        return Response.Status.INTERNAL_SERVER_ERROR;
    }

    private Response buildErrorResponse(Response.Status status, RuntimeException exception) {
        String message = exception.getMessage() != null ? exception.getMessage() : "Unexpected error";
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("error", message))
                .build();
    }
}