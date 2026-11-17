package com.fleetos.security;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
 public static final String HEADER="X-FleetOS-Api-Key";
 private final ApiCredentialRepository credentials;
 public ApiKeyAuthenticationFilter(ApiCredentialRepository credentials){this.credentials=credentials;}
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
  String raw=request.getHeader(HEADER);
  if(raw!=null&&!raw.isBlank()) credentials.findByKeyHashAndActiveTrue(ApiKeyHasher.hash(raw)).ifPresent(credential->{
   TenantContext.set(credential);
   var authority=new SimpleGrantedAuthority("ROLE_"+credential.getRole().name());
   SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(credential.getName(),null,List.of(authority)));
  });
  try{chain.doFilter(request,response);}finally{TenantContext.clear();SecurityContextHolder.clearContext();}
 }
}
