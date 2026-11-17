package com.fleetos.security;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ApiCredentialRepository extends JpaRepository<ApiCredential,Long>{
 Optional<ApiCredential> findByKeyHashAndActiveTrue(String keyHash);
 Optional<ApiCredential> findByIdAndOrganizationId(Long id,Long organizationId);
 List<ApiCredential> findAllByOrganizationIdOrderByIdAsc(Long organizationId);
}
