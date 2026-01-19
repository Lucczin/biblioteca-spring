package com.biblioteca.biblioteca_spring.biblioteca.dto;

import com.biblioteca.biblioteca_spring.biblioteca.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "CPF deve estar no formato 000.000.000-00")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}",
            message = "Telefone deve estar no formato (00) 00000-0000")
    private String telefone;

    @NotBlank(message = "Tipo de usuário é obrigatório")
    @Pattern(regexp = "COMUM|FUNCIONARIO|ADMIN",
            message = "Tipo deve ser COMUM, FUNCIONARIO ou ADMIN")
    private String tipo;

    // Método para converter de Entity para DTO
    public static UsuarioDTO fromEntity(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                formatarCPF(usuario.getCpf()),
                formatarTelefone(usuario.getTelefone()),
                usuario.getTipo().name()
        );
    }

    // Método para converter de DTO para Entity
    public Usuario toEntity() {
        return Usuario.builder()
                .id(this.id)
                .nome(this.nome)
                .email(this.email)
                .cpf(removerFormatacaoCPF(this.cpf))
                .telefone(removerFormatacaoTelefone(this.telefone))
                .tipo(Usuario.TipoUsuario.valueOf(this.tipo))
                .build();
    }

    private static String formatarCPF(String cpf) {
        if (cpf == null) return null;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11) return cpf;
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private static String formatarTelefone(String telefone) {
        if (telefone == null) return null;
        telefone = telefone.replaceAll("\\D", "");
        if (telefone.length() == 11) {
            return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        } else if (telefone.length() == 10) {
            return telefone.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return telefone;
    }

    private static String removerFormatacaoCPF(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("\\D", "");
    }

    private static String removerFormatacaoTelefone(String telefone) {
        if (telefone == null) return null;
        return telefone.replaceAll("\\D", "");
    }
}