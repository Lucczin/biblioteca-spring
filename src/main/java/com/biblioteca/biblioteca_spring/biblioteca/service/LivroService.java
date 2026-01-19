package com.biblioteca.biblioteca_spring.biblioteca.service;

import com.biblioteca.biblioteca_spring.biblioteca.dto.LivroDTO;
import com.biblioteca.biblioteca_spring.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.biblioteca_spring.biblioteca.model.Livro;
import com.biblioteca.biblioteca_spring.biblioteca.repository.LivroRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    // ================== CREATE ==================

    @Transactional
    public LivroDTO criarLivro(LivroDTO livroDTO) {

        if (livroRepository.existsByIsbn(livroDTO.getIsbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado: " + livroDTO.getIsbn());
        }

        Livro livro = Livro.builder()
                .titulo(livroDTO.getTitulo())
                .autor(livroDTO.getAutor())
                .isbn(livroDTO.getIsbn())
                .anoPublicacao(livroDTO.getAnoPublicacao())
                .editora(livroDTO.getEditora())
                .quantidadeTotal(livroDTO.getQuantidadeTotal())
                .quantidadeDisponivel(livroDTO.getQuantidadeTotal())
                .build();

        return convertToDTO(livroRepository.save(livro));
    }

    // ================== READ ==================

    @Transactional(readOnly = true)
    public List<LivroDTO> listarTodos() {
        return livroRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public LivroDTO buscarPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Livro não encontrado com ID: " + id));

        return convertToDTO(livro);
    }

    @Transactional(readOnly = true)
    public List<LivroDTO> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ================== UPDATE ==================

    @Transactional
    public LivroDTO atualizarLivro(Long id, LivroDTO livroDTO) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Livro não encontrado com ID: " + id));

        livro.setTitulo(livroDTO.getTitulo());
        livro.setAutor(livroDTO.getAutor());
        livro.setIsbn(livroDTO.getIsbn());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setEditora(livroDTO.getEditora());

        int diferenca = livroDTO.getQuantidadeTotal() - livro.getQuantidadeTotal();
        livro.setQuantidadeTotal(livroDTO.getQuantidadeTotal());
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + diferenca);

        return convertToDTO(livroRepository.save(livro));
    }

    // ================== DELETE ==================

    @Transactional
    public void deletarLivro(Long id) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Livro não encontrado com ID: " + id));

        if (livro.getQuantidadeDisponivel() < livro.getQuantidadeTotal()) {
            throw new IllegalStateException(
                    "Não é possível excluir livro com exemplares emprestados");
        }

        livroRepository.delete(livro);
    }

    // ================== CONVERSÃO ==================

    private LivroDTO convertToDTO(Livro livro) {
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
    @Transactional(readOnly = true)
    public List<LivroDTO> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<LivroDTO> listarDisponiveis() {
        return livroRepository.findDisponiveis()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<LivroDTO> findBaixoEstoque(int limite) {
        return livroRepository.findBaixoEstoque(limite)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public Long countTotal() {
        return livroRepository.count();
    }
    @Transactional(readOnly = true)
    public Long countDisponiveis() {
        Long total = livroRepository.sumQuantidadeDisponivel();
        return total != null ? total : 0L;
    }
}
