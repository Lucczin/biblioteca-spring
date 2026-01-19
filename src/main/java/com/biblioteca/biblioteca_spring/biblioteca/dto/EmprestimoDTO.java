package com.biblioteca.biblioteca_spring.biblioteca.dto;

import com.biblioteca.biblioteca_spring.biblioteca.model.Emprestimo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoDTO {

    private Long id;
    private Long livro;
    private String tituloLivro;
    private String livroIsbn;
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioEmail;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private String status;
    private double multa;

    public static EmprestimoDTO fromEntity(Emprestimo emprestimo){
        return new EmprestimoDTO(
                emprestimo.getId(),
                emprestimo.getLivro().getId(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getLivro().getIsbn(),
                emprestimo.getUsuario().getId(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getUsuario().getEmail(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucaoPrevista(),
                emprestimo.getDataDevolucaoReal(),
                emprestimo.getStatus().name(),
                emprestimo.getMulta()
        );
    }

}
