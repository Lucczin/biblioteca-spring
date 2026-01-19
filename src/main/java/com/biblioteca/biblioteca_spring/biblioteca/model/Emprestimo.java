package com.biblioteca.biblioteca_spring.biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao_real")
    private LocalDate dataDevolucaoReal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status;

    @Column(name = "multa")
    private Double multa;

    @CreationTimestamp
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;


    public Integer getDiasAtraso() {
        if (dataDevolucaoReal != null && dataDevolucaoReal.isAfter(dataDevolucaoPrevista)) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(
                    dataDevolucaoPrevista, dataDevolucaoReal
            );
        } else if (status == StatusEmprestimo.ATRASADO) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(
                    dataDevolucaoPrevista, LocalDate.now()
            );
        }
        return 0;
    }

    // Método para calcular multa automaticamente
    public Double calcularMultaAutomatica() {
        if (multa != null) {
            return multa;
        }

        Integer diasAtraso = getDiasAtraso();
        if (diasAtraso > 0) {
            return diasAtraso * 2.0;
        }
        return 0.0;
    }

    // Método para verificar se está atrasado
    public boolean isAtrasado() {
        if (status == StatusEmprestimo.DEVOLVIDO || status == StatusEmprestimo.PERDIDO) {
            return false;
        }

        LocalDate hoje = LocalDate.now();
        return hoje.isAfter(dataDevolucaoPrevista);
    }

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
        dataAtualizacao = LocalDateTime.now();

        if (dataEmprestimo == null) {
            dataEmprestimo = LocalDate.now();
        }

        if (dataDevolucaoPrevista == null) {
            dataDevolucaoPrevista = dataEmprestimo.plusDays(14);
        }

        if (status == null) {
            status = StatusEmprestimo.ATIVO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();


        if (status == StatusEmprestimo.ATIVO && isAtrasado()) {
            status = StatusEmprestimo.ATRASADO;
        }
    }

    public enum StatusEmprestimo {
        ATIVO("Ativo"),
        DEVOLVIDO("Devolvido"),
        ATRASADO("Atrasado"),
        PERDIDO("Perdido");

        private final String descricao;

        StatusEmprestimo(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

}