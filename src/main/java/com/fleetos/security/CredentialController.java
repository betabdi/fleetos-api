package com.fleetos.security;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.fleetos.common.ApiException;
@RestController @RequestMapping("/api/v1/admin/credentials")
public class CredentialController {
 private final ApiCredentialRepository credentials; private final SecureRandom random=new SecureRandom();
 public CredentialController(ApiCredentialRepository credentials){this.credentials=credentials;}
 @GetMapping public List<CredentialView> list(){return credentials.findAllByOrganizationIdOrderByIdAsc(organizationId()).stream().map(CredentialView::from).toList();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public CreatedCredential create(@Valid @RequestBody CreateCredential request){
  byte[] bytes=new byte[32];random.nextBytes(bytes);String rawKey=HexFormat.of().formatHex(bytes);
  ApiCredential saved=credentials.save(new ApiCredential(TenantContext.organization(),request.name().trim(),ApiKeyHasher.hash(rawKey),request.role()));
  return new CreatedCredential(saved.getId(),saved.getName(),saved.getRole(),rawKey);
 }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void revoke(@PathVariable Long id){
  ApiCredential credential=credentials.findByIdAndOrganizationId(id,organizationId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Credential not found"));
  credential.revoke();credentials.save(credential);
 }
 private Long organizationId(){return TenantContext.organization().getId();}
 public record CreateCredential(@NotBlank @Size(max=120) String name,@NotNull FleetRole role){}
 public record CreatedCredential(Long id,String name,FleetRole role,String apiKey){}
 public record CredentialView(Long id,String name,FleetRole role,boolean active){static CredentialView from(ApiCredential value){return new CredentialView(value.getId(),value.getName(),value.getRole(),value.isActive());}}
}
