package com.biblioteca.biblioteca_spring.biblioteca.repository;

import com.biblioteca.biblioteca_spring.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar por email
    Optional<Usuario> findByEmail(String email);

    // Buscar por CPF
    Optional<Usuario> findByCpf(String cpf);

    // Verificar se email existe
    boolean existsByEmail(String email);

    // Verificar se CPF existe
    boolean existsByCpf(String cpf);

    // Buscar por nome
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    // Buscar por tipo
    List<Usuario> findByTipo(Usuario.TipoUsuario tipo);

    // Contar por tipo
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipo = :tipo")
    Long countByTipo(@Param("tipo") Usuario.TipoUsuario tipo);
}
