package com.fleetos.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class BootstrapCredentialConfiguration implements ApplicationRunner {
 private final OrganizationRepository organizations; private final ApiCredentialRepository credentials;
 @Value("${fleetos.bootstrap.organization-slug:}") private String slug;
 @Value("${fleetos.bootstrap.organization-name:}") private String organizationName;
 @Value("${fleetos.bootstrap.credential-name:bootstrap-admin}") private String credentialName;
 @Value("${fleetos.bootstrap.api-key:}") private String apiKey;
 public BootstrapCredentialConfiguration(OrganizationRepository organizations,ApiCredentialRepository credentials){this.organizations=organizations;this.credentials=credentials;}
 @Override @Transactional public void run(ApplicationArguments args){
  if(slug.isBlank()||apiKey.isBlank()) return;
  Organization organization=organizations.findBySlug(slug).orElseGet(()->organizations.save(new Organization(slug,organizationName.isBlank()?slug:organizationName)));
  String hash=ApiKeyHasher.hash(apiKey);
  if(credentials.findByKeyHashAndActiveTrue(hash).isEmpty()) credentials.save(new ApiCredential(organization,credentialName,hash,FleetRole.ADMIN));
 }
}
