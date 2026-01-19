package com.biblioteca.biblioteca_spring.biblioteca.dto;

import com.biblioteca.biblioteca_spring.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca_spring.biblioteca.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoResponseDTO {

    private Long id;
    private LivroDTO livro;
    private UsuarioDTO usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private String status;
    private Double multa;
    private Integer diasAtraso;

    // Método para converter de Entity para DTO
    public static EmprestimoResponseDTO fromEntity(Emprestimo emprestimo) {
        EmprestimoResponseDTO dto = new EmprestimoResponseDTO();
        dto.setId(emprestimo.getId());
        dto.setLivro(LivroDTO.fromEntity(emprestimo.getLivro()));
        dto.setUsuario(UsuarioDTO.fromEntity(emprestimo.getUsuario()));
        dto.setDataEmprestimo(emprestimo.getDataEmprestimo());
        dto.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
        dto.setDataDevolucaoReal(emprestimo.getDataDevolucaoReal());
        dto.setStatus(emprestimo.getStatus().name());
        dto.setMulta(emprestimo.getMulta());
        dto.setDiasAtraso(emprestimo.getDiasAtraso());

        return dto;
    }
}