package com.biblioteca.biblioteca_spring.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDevolucaoDTO {

    private LocalDate dataDevolucaoReal;
    private String observacoes;
}
