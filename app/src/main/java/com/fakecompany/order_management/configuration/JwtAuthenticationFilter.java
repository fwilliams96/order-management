package com.fakecompany.order_management.configuration;

import com.fakecompany.order_management.auth.application.check_expiration_date.JwtExpirationDateChecker;
import com.fakecompany.order_management.auth.application.extract_user_id.JwtUserIdExtractor;
import com.fakecompany.order_management.auth.application.find.UserFinder;
import com.fakecompany.order_management.auth.domain.JWToken;
import com.fakecompany.order_management.auth.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUserIdExtractor jwtUserIdExtractor;
    private final UserFinder userFinder;
    private final JwtExpirationDateChecker jwtExpirationDateChecker;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or malformed");
            return; // There is no token, stop flow
        }

        String accessToken = authHeader.substring(7); // Extract <token> from "Bearer <token>"

        JWToken jwToken = JWToken.builder()
                .accessToken(accessToken)
                .build();

        UUID userId = null;
        try {
            userId = jwtUserIdExtractor.extract(jwToken);
        } catch (Exception e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + e.getMessage());
            return;
        }

        Optional<User> userOpt = userFinder.find(userId);
        if (userOpt.isEmpty()) {
            log.warn("User with ID {} not found", userId);
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found for the given token");
            return; // No user, stop flow
        }

        User user = userOpt.get();
        boolean expired = jwtExpirationDateChecker.isExpired(jwToken);
        if (expired) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
            return; // Token expired, stop flow
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        request.setAttribute("userId", userId);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("swagger-ui.html") ||
                path.startsWith("/v3/api-docs/") ||
                path.startsWith("/swagger-resources/") ||
                path.startsWith("/webjars/") ||
                path.startsWith("/error");
    }

}
