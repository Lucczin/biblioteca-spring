package com.biblioteca.biblioteca_spring.biblioteca.service;

import com.biblioteca.biblioteca_spring.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.biblioteca_spring.biblioteca.dto.EmprestimoRequestDTO;
import com.biblioteca.biblioteca_spring.biblioteca.dto.EmprestimoResponseDTO;
import com.biblioteca.biblioteca_spring.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.biblioteca_spring.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca_spring.biblioteca.model.Livro;
import com.biblioteca.biblioteca_spring.biblioteca.model.Usuario;
import com.biblioteca.biblioteca_spring.biblioteca.repository.LivroRepository;
import com.biblioteca.biblioteca_spring.biblioteca.repository.UsuarioRepository;
import com.biblioteca.biblioteca_spring.biblioteca.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    // Método básico para listar todos os empréstimos
    @Transactional(readOnly = true)
    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    // Método para listar empréstimos ativos
    @Transactional(readOnly = true)
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.findByStatus(Emprestimo.StatusEmprestimo.ATIVO);
    }

    // Método para listar empréstimos atrasados
    @Transactional(readOnly = true)
    public List<Emprestimo> listarEmprestimosAtrasados() {
        LocalDate hoje = LocalDate.now();
        return emprestimoRepository.findAtrasados(hoje);
    }

    // Método para listar últimos empréstimos
    @Transactional(readOnly = true)
    public List<Emprestimo> listarUltimosEmprestimos(int limite) {
        return emprestimoRepository.findAll().stream()
                .sorted((e1, e2) -> e2.getDataEmprestimo().compareTo(e1.getDataEmprestimo()))
                .limit(limite)
                .toList();
    }

    // Método SIMPLIFICADO que substitui listarTodosDetalhados
    @Transactional(readOnly = true)
    public List<Emprestimo> listarEmprestimosComDetalhes() {
        return emprestimoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado com ID: " + id));
    }

    @Transactional
    public Emprestimo salvarEmprestimo(Emprestimo emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public void devolverLivro(Long id) {
        Emprestimo emprestimo = buscarPorId(id);
        emprestimo.setDataDevolucaoReal(LocalDate.now());
        emprestimo.setStatus(Emprestimo.StatusEmprestimo.DEVOLVIDO);
        emprestimoRepository.save(emprestimo);
    }

    @Transactional(readOnly = true)
    public Long countAtivos() {
        return emprestimoRepository.countByStatus(Emprestimo.StatusEmprestimo.ATIVO);
    }

    @Transactional(readOnly = true)
    public Long countAtrasados() {
        return emprestimoRepository.countByStatus(Emprestimo.StatusEmprestimo.ATRASADO);
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> buscarPrestesAVencer(int dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(dias);
        return emprestimoRepository.findPrestesAVencer(hoje, limite);
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> listarEmprestimosPorUsuario(Long usuarioId) {
        return emprestimoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> listarEmprestimosPorLivro(Long livroId) {
        return emprestimoRepository.findByLivroId(livroId);
    }
    @Transactional
    public Emprestimo criarEmprestimo(EmprestimoRequestDTO dto) {

        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new IllegalStateException("Livro sem exemplares disponíveis");
        }

        Emprestimo emprestimo = Emprestimo.builder()
                .livro(livro)
                .usuario(usuario)
                .dataEmprestimo(LocalDate.now())
                .dataDevolucaoPrevista(
                        dto.getDataDevolucaoPrevista() != null
                                ? dto.getDataDevolucaoPrevista()
                                : LocalDate.now().plusDays(14)
                )
                .status(Emprestimo.StatusEmprestimo.ATIVO)
                .build();

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);

        return emprestimoRepository.save(emprestimo);
    }

}