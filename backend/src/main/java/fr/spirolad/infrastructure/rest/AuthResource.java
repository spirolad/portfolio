package fr.spirolad.infrastructure.rest;
import fr.spirolad.api.AuthApi;
import fr.spirolad.dto.LoginRequest;
import fr.spirolad.dto.LoginResponse;
import fr.spirolad.dto.UnauthorizedResponse;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
public class AuthResource implements AuthApi {
    @ConfigProperty(name = "portfolio.admin.username")
    String adminUsername;
    @ConfigProperty(name = "portfolio.admin.password")
    String adminPassword;

    @Override
    public Response login(LoginRequest loginRequest) {
        if (adminUsername.equals(loginRequest.getUsername()) && adminPassword.equals(loginRequest.getPassword())) {
            String token = Jwt.issuer("portfolio")
                    .subject(adminUsername)
                    .groups("admin")
                    .expiresIn(3600)
                    .sign();

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            return Response.ok(response).build();
        } else {
            UnauthorizedResponse unauthorized = new UnauthorizedResponse();
            unauthorized.setError("Invalid credentials");
            return Response.status(Response.Status.UNAUTHORIZED).entity(unauthorized).build();
        }
    }
}
