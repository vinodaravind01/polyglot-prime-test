package org.techbd.service.http;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GitHubUserAuthorizationFilter extends OncePerRequestFilter {
  private static final String AUTH_USER_SESSION_ATTR_NAME = "authenticatedUser";
  private static final String supportEmail = "NYeC QCS Support <qcs-help@qualifiedentity.org>";

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record AuthenticatedUser(OAuth2User principal, GitHubUsersService.AuthorizedUser ghUser) implements Serializable {
  }

  public static final Optional<AuthenticatedUser> getAuthenticatedUser(
      final @NonNull HttpServletRequest request) {
    final var sessionUser = (AuthenticatedUser) request.getSession(true)
        .getAttribute(AUTH_USER_SESSION_ATTR_NAME);
    return Optional.ofNullable(sessionUser);
  }

  protected static final void setAuthenticatedUser(final @NonNull HttpServletRequest request,
      final @NonNull AuthenticatedUser authUser) {
    request.getSession(true).setAttribute(AUTH_USER_SESSION_ATTR_NAME, authUser);
  }

  private final GitHubUsersService gitHubUsers;

  public GitHubUserAuthorizationFilter(final GitHubUsersService gitHubUsers) {
    this.gitHubUsers = gitHubUsers;
  }

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {
    final var sessionUser = getAuthenticatedUser(request);
    if (sessionUser.isEmpty()) {
      final var authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated()
          && !"anonymousUser".equals(authentication.getPrincipal().toString())) {
        final var gitHubPrincipal = (DefaultOAuth2User) authentication.getPrincipal();
        final var gitHubLoginId = Optional.ofNullable(gitHubPrincipal.getAttribute("login")).orElseThrow();
        final var gitHubAuthnUser = gitHubUsers.isAuthorizedUser(gitHubLoginId.toString());
        if (!gitHubAuthnUser.isPresent()) {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          response.getWriter()
              .write("GitHub ID " + gitHubLoginId
                  + " does not have permission to access this resource. Please send an email to "
                  + supportEmail + " from your manager to have this GitHub ID added to the list of authorized users.");
          return;
        }
        setAuthenticatedUser(request, new AuthenticatedUser(gitHubPrincipal, gitHubAuthnUser.orElseThrow()));
      }
    }

    if (request.getRequestURI().startsWith("/actuator")) {
      Optional<AuthenticatedUser> userOptional = getAuthenticatedUser(request);
      if (userOptional.isPresent()) {
        if (!userOptional.get().ghUser.isUserHasActuatorAccess()) {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          response.setContentType("text/plain"); // Ensure content type is set to text/plain
          response.getWriter().write("GitHub ID " + userOptional.get().ghUser.gitHubId()
              + " does not have roles to access actuator. Please send an email to "
              + supportEmail
              + " from your manager, to add roles to access actuator from this GitHub ID.");
          response.flushBuffer();
          return; // Stop further processing of the request
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
