package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.service.MarathonService;
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
@RequestMapping("/member")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Member and Marathons")
@SecurityRequirement(name = "bearerAuth")
public class MarathonResource {
        private MarathonService marathonService;

        public MarathonResource(MarathonService marathonService) {
                this.marathonService = marathonService;
        }

        @GetMapping
        @Operation(description = "Get all members and their marathons", responses = {
                        @ApiResponse(description = "Success", responseCode = "200"),
                        @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                        @ApiResponse(description = "Unknown error", responseCode = "400"),
        })
        public ResponseEntity<List<MemberDTO>> getAllMembers() {
                return ResponseEntity.ok(marathonService.getAll());
        }

        @GetMapping("/{id}")
        @Operation(description = "Get a member and their marathons by member ID", responses = {
                        @ApiResponse(description = "Success", responseCode = "200"),
                        @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                        @ApiResponse(description = "Unknown error", responseCode = "400"),
        })
        public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
                return ResponseEntity.ok(marathonService.findById(id));
        }

        @PutMapping("/{id}/marathons")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(description = "Update all marathons for a member", responses = {
                        @ApiResponse(description = "Success", responseCode = "200"),
                        @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                        @ApiResponse(description = "Unknown error", responseCode = "400"),
        })
        public ResponseEntity<MemberDTO> updateMarathons(@PathVariable Long id, @RequestBody List<MarathonDTO> marathons) {
                return ResponseEntity.ok(marathonService.updateMarathons(id, marathons));
        }

        @PostMapping("/{id}/marathons")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(description = "Add new marathons to a member", responses = {
                        @ApiResponse(description = "Success", responseCode = "201"),
                        @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                        @ApiResponse(description = "Unknown error", responseCode = "400"),
        })
        public ResponseEntity<MemberDTO> addMarathons(@PathVariable Long id, @RequestBody List<MarathonDTO> marathons) {
                return ResponseEntity.status(HttpStatusCode.valueOf(201))
                                .body(marathonService.addMarathons(id, marathons));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(description = "Delete a member and all their marathons", responses = {
                        @ApiResponse(description = "Success", responseCode = "204"),
                        @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                        @ApiResponse(description = "Unknown error", responseCode = "400"),
        })
        public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
                marathonService.delete(id);
                return ResponseEntity.noContent().build();
        }
}