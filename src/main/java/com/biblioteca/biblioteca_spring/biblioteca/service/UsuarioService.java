package com.biblioteca.biblioteca_spring.biblioteca.service;

import com.biblioteca.biblioteca_spring.biblioteca.dto.UsuarioDTO;
import com.biblioteca.biblioteca_spring.biblioteca.dto.UsuarioRequestDTO;
import com.biblioteca.biblioteca_spring.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.biblioteca_spring.biblioteca.model.Usuario;
import com.biblioteca.biblioteca_spring.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // ========== MÉTODOS BÁSICOS CRUD ==========

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        // Verificar se email já existe (para novos usuários)
        if (usuario.getId() == null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + usuario.getEmail());
        }

        // Verificar se CPF já existe (para novos usuários)
        if (usuario.getId() == null && usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + usuario.getCpf());
        }

        // Se tipo não foi definido, definir como COMUM
        if (usuario.getTipo() == null) {
            usuario.setTipo(Usuario.TipoUsuario.COMUM);
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }

        usuarioRepository.deleteById(id);
    }

    // ========== MÉTODOS DE BUSCA ==========

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + email));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com CPF: " + cpf));
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarPorTipo(String tipo) {
        try {
            Usuario.TipoUsuario tipoEnum = Usuario.TipoUsuario.valueOf(tipo.toUpperCase());
            return usuarioRepository.findByTipo(tipoEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de usuário inválido: " + tipo);
        }
    }

    // ========== MÉTODOS ESTATÍSTICOS ==========

    @Transactional(readOnly = true)
    public Long countTotal() {
        return usuarioRepository.count();
    }

    @Transactional(readOnly = true)
    public Long countByTipo(String tipo) {
        try {
            Usuario.TipoUsuario tipoEnum = Usuario.TipoUsuario.valueOf(tipo.toUpperCase());
            return usuarioRepository.countByTipo(tipoEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de usuário inválido: " + tipo);
        }
    }

    // ========== MÉTODOS DE VALIDAÇÃO ==========

    @Transactional(readOnly = true)
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean cpfExiste(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    @Transactional(readOnly = true)
    public boolean podeRealizarEmprestimo(Long usuarioId) {
        return true;
    }
    @Transactional
    public void salvarUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .cpf(dto.getCpf().replaceAll("\\D", ""))
                .telefone(dto.getTelefone().replaceAll("\\D", ""))
                .tipo(Usuario.TipoUsuario.valueOf(dto.getTipo()))
                .build();

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void atualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf().replaceAll("\\D", ""));
        usuario.setTelefone(dto.getTelefone().replaceAll("\\D", ""));
        usuario.setTipo(Usuario.TipoUsuario.valueOf(dto.getTipo()));

        usuarioRepository.save(usuario);
    }
}