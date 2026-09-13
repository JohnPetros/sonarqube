package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Organization, Address and Groups")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationResource {
    private OrganizationService organizationService;

    public OrganizationResource(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    @Operation(description = "Get all organizations and groups", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
            @ApiResponse(description = "Unknown error", responseCode = "400"),
    })
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(description = "Get a organization and groups by organization ID", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
            @ApiResponse(description = "Unknown error", responseCode = "400"),
    })
    public ResponseEntity<OrganizationDTO> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Update a group and members by group ID", responses = {
            @ApiResponse(description = "Success", responseCode = "201"),
            @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
            @ApiResponse(description = "Unknown error", responseCode = "400"),
    })
    public ResponseEntity<OrganizationDTO> update(@PathVariable Long id, @RequestBody OrganizationDTO body) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(organizationService.save(id, body));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Register a new organization and groups", responses = {
            @ApiResponse(description = "Success", responseCode = "201"),
            @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
            @ApiResponse(description = "Unknown error", responseCode = "400"),
    })
    public ResponseEntity<OrganizationDTO> register(@RequestBody OrganizationDTO body) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(organizationService.save(body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Delete a group and members by group ID", responses = {
            @ApiResponse(description = "Success", responseCode = "204"),
            @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
            @ApiResponse(description = "Unknown error", responseCode = "400"),
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}