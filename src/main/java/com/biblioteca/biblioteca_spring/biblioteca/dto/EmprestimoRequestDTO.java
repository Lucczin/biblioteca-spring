package com.biblioteca.biblioteca_spring.biblioteca.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoRequestDTO {

    private Long livroId;
    private Long usuarioId;
    private LocalDate dataDevolucaoPrevista;
}
