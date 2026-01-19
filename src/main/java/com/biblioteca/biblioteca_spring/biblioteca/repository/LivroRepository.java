package com.biblioteca.biblioteca_spring.biblioteca.repository;

import com.biblioteca.biblioteca_spring.biblioteca.model.Livro;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    boolean existsByIsbn(String isbn);

    @Query("SELECT COUNT(l) FROM Livro l")
    Long countTotal();

    @Query("SELECT SUM(l.quantidadeDisponivel) FROM Livro l")
    Long sumQuantidadeDisponivel();


    // Buscar livros por título
    @Query("""
   SELECT l FROM Livro l
   WHERE l.quantidadeDisponivel <= :limite
""")
    List<Livro> findBaixoEstoque(@Param("limite") int limite);


    // Buscar livros por autor
    List<Livro> findByAutorContainingIgnoreCase(String autor);

    // Buscar por ISBN
    Livro findByIsbn(String isbn);

    // Verificar se ISBN existe


    // Contar livros disponíveis
    @Query("SELECT COUNT(l) FROM Livro l WHERE l.quantidadeDisponivel > 0")
    Long countByQuantidadeDisponivelGreaterThan(@Param("quantidade") int quantidade);

    // Buscar livros com baixo estoque
    @Query("SELECT l FROM Livro l WHERE l.quantidadeDisponivel <= :limite")
    List<Livro> findByQuantidadeDisponivelLessThanEqual(@Param("limite") int limite);

    // Buscar livros disponíveis


    // Buscar por editora
    List<Livro> findByEditoraContainingIgnoreCase(String editora);


    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT l FROM Livro l WHERE l.quantidadeDisponivel > 0")
    List<Livro> findDisponiveis();


}