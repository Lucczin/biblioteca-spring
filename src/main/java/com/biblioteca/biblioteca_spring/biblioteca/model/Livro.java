package com.biblioteca.biblioteca_spring.biblioteca.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.swing.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 2, max = 200, message = "Título deve ter entre 2 e 200 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    @Size(min = 2, max = 100, message = "Autor deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String autor;

    @NotBlank(message = "ISBN é obrigatório")
    @Pattern(regexp = "^(?:ISBN(?:-13)?:?\\ )?(?=[0-9]{13}$|(?=(?:[0-9]+[-\\ ]){4})[-\\ 0-9]{17}$)97[89][-\\ ]?[0-9]{1,5}[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9]$",
            message = "ISBN inválido")
    @Column(unique = true, nullable = false)
    private String isbn;

    @NotNull(message = "Ano de publicação é obrigatório")
    @Min(value = 1000, message = "Ano deve ser maior que 1000")
    @Max(value = 2024, message = "Ano não pode ser no futuro")
    @Column(name = "ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    @NotBlank(message = "Editora é obrigatória")  // ← ESTE CAMPO ESTAVA FALTANDO!
    @Size(min = 2, max = 100, message = "Editora deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String editora;

    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel;

    @Column(name = "quantidade_total", nullable = false)
    private Integer quantidadeTotal;

    @CreationTimestamp
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
        dataAtualizacao = LocalDateTime.now();

        if (quantidadeDisponivel == null) {
            quantidadeDisponivel = quantidadeTotal != null ? quantidadeTotal : 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}