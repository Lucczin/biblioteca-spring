package com.biblioteca.biblioteca_spring.biblioteca.repository;

import  com.biblioteca.biblioteca_spring.biblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // Buscar empréstimos por status
    List<Emprestimo> findByStatus(Emprestimo.StatusEmprestimo status);

    // Contar empréstimos por status
    Long countByStatus(Emprestimo.StatusEmprestimo status);

    // Buscar empréstimos atrasados
    @Query("SELECT e FROM Emprestimo e WHERE e.dataDevolucaoPrevista < :hoje AND e.status = 'ATIVO'")
    List<Emprestimo> findAtrasados(@Param("hoje") LocalDate hoje);

    // Buscar empréstimos por usuário
    @Query("SELECT e FROM Emprestimo e WHERE e.usuario.id = :usuarioId")
    List<Emprestimo> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Buscar empréstimos por livro
    @Query("SELECT e FROM Emprestimo e WHERE e.livro.id = :livroId")
    List<Emprestimo> findByLivroId(@Param("livroId") Long livroId);

    // Buscar empréstimos prestes a vencer
    @Query("SELECT e FROM Emprestimo e WHERE e.dataDevolucaoPrevista BETWEEN :hoje AND :limite AND e.status = 'ATIVO'")
    List<Emprestimo> findPrestesAVencer(@Param("hoje") LocalDate hoje,
                                        @Param("limite") LocalDate limite);

    // Contar empréstimos ativos de um usuário
    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.usuario.id = :usuarioId AND e.status = 'ATIVO'")
    Long countAtivosByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Verificar se livro está emprestado
    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.livro.id = :livroId AND e.status = 'ATIVO'")
    boolean isLivroEmprestado(@Param("livroId") Long livroId);

    // Verificar se usuário já tem este livro emprestado (ALTERNATIVA SIMPLES)
    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.livro.id = :livroId AND e.usuario.id = :usuarioId AND e.status = 'ATIVO'")
    boolean existsAtivoByLivroAndUsuario(@Param("livroId") Long livroId,
                                         @Param("usuarioId") Long usuarioId);
}