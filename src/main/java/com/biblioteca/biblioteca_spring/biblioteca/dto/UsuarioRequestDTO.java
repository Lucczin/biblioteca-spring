package com.biblioteca.biblioteca_spring.biblioteca.dto;


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
public class UsuarioRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2,max = 100,message = "Nome deve ter entre 2 a 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\.\\d{2}",
    message = "CPF deve estar na formato: 000.000.000.00")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "(\\d{2}\\) \\d{4,5}-\\d{4}",
    message = "Telefone deve estar nesse formato: (00) 0000-0000")
    private String telefone;

    @NotBlank(message = "Tipo de usuario é obrigatório")
    @Pattern(regexp = "COMUM|FUNCIONARIO|ADMIN",
    message = "Tipo deve ser Comum, Funcionario ou Admin")
    private String tipo;
}
