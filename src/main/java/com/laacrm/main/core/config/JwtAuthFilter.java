package com.laacrm.main.core.config;

import com.laacrm.main.core.migration.MigratorService;
import com.laacrm.main.framework.AuthThreadLocal;
import com.laacrm.main.framework.entities.Users;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final MigratorService migratorService;

    /**
     * JWT Authentication Filter
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                AuthThreadLocal.setCurrentUser((Users) userDetails);
                AuthThreadLocal.setCurrentTenant(((Users) userDetails).getTenant());
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    if(!migratorService.isTaskScheduled("autoDataPopulate")){
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(() -> {
                            AuthThreadLocal.setCurrentTenant(((Users) userDetails).getTenant());
                            AuthThreadLocal.setCurrentTenant(((Users) userDetails).getTenant());
                            migratorService.migrate();
                        });
                        executor.shutdown();
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UsernameNotFoundException exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            JSONObject details = new JSONObject();
            details.put("code", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
            details.put("api_name", "jwt");
            details.put("message", "User Not Authenticated");
            details.put("key", "JWT Failure");
            response.getWriter().write(details.toString());
        }
    }
}
