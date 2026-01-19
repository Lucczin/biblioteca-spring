package com.biblioteca.biblioteca_spring.biblioteca.dto;

import com.biblioteca.biblioteca_spring.biblioteca.model.Livro;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LivroDTO {

    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 2, max = 200)
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    @Size(min = 2, max = 100)
    private String autor;

    @NotBlank(message = "ISBN é obrigatório")
    private String isbn;

    @NotNull(message = "Ano de publicação é obrigatório")
    @Min(1000)
    @Max(2024)
    private Integer anoPublicacao;

    @NotBlank(message = "Editora é obrigatória")
    @Size(min = 2, max = 100)
    private String editora;

    @NotNull(message = "Quantidade total é obrigatória")
    @Min(value = 1, message = "Deve haver pelo menos 1 livro")
    private Integer quantidadeTotal;

    // Método para converter de Entity para DTO
    public static LivroDTO fromEntity(Livro livro) {
        return new LivroDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getEditora(),
                livro.getQuantidadeTotal()
        );
    }


}
