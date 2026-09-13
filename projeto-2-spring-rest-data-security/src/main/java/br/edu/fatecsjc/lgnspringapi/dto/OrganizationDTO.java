package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDTO {
    @Schema(hidden = true)
    private Long id;
    private String nome;
    private String instituicaoEnsino;
    private String paisSede;
    private AddressDTO endereco;
    private List<GroupDTO> grupos;
    private List<Long> gruposId;
}