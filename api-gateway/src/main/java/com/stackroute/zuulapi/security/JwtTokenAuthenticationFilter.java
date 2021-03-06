//package com.stackroute.zuulapi.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.cors.CorsUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtConfig jwtConfig;
//
//    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
//        this.jwtConfig = jwtConfig;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        response.setStatus(HttpServletResponse.SC_OK);
//
//
//        if(CorsUtils.isPreFlightRequest(request)) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        }
//
//
//        System.out.println("The request:\t"+request);
//
//        System.out.println("The header from jwtConfig:\t"+jwtConfig.getHeader());
//
//        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
//        String header = request.getHeader(jwtConfig.getHeader());
//
//        System.out.println("the header:\t"+header);
//
//        // 2. validate the header and check the prefix
//        if(header == null) {
//            chain.doFilter(request, response);  		// If not valid, go to the next filter.
//            return;
//        }else if(!header.startsWith(jwtConfig.getPrefix())){
//            chain.doFilter(request, response);  		// If not valid, go to the next filter.
//            return;
//        }
//
//        // If there is no token provided and hence the user won't be authenticated.
//        // It's Ok. Maybe the user accessing a public path or asking for a token.
//
//        // All secured paths that needs a token are already defined and secured in config class.
//        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.
//
//        // 3. Get the token
//        String token = header.replace(jwtConfig.getPrefix(), "");
//        System.out.println("token: " + token);
//
//
//        // exceptions might be thrown in creating the claims if for example the token is expired
//
//            // 4. Validate the token
//            Claims claims = Jwts.parser()
//                    .setSigningKey(jwtConfig.getSecret())
//                    .parseClaimsJws(token)
//                    .getBody();
//
//
//            String username = claims.getSubject();
//            System.out.println("username:" + username );
//        try {
//
//            if(username != null) {
//                List<String> authorities = (List<String>) claims.get("authorities");
//
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//               auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                System.out.println("a");
//
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//
//        } catch (Exception e) {
//            // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
//
//            System.out.println("GODDAM EXCEPTION");
//            System.out.println(e.getMessage());
//            SecurityContextHolder.clearContext();
//        }
//
//        // go to the next filter in the filter chain
//        chain.doFilter(request, response);
//    }
//
//}
