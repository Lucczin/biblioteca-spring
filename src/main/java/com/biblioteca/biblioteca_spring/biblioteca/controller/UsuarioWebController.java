package com.biblioteca.biblioteca_spring.biblioteca.controller;

import com.biblioteca.biblioteca_spring.biblioteca.dto.UsuarioRequestDTO;
import com.biblioteca.biblioteca_spring.biblioteca.model.Usuario;
import com.biblioteca.biblioteca_spring.biblioteca.service.UsuarioService;
import com.biblioteca.biblioteca_spring.biblioteca.dto.UsuarioDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioWebController {

    private final UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        model.addAttribute("isEdit", false);
        return "usuarios/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuarioDTO",
                UsuarioDTO.fromEntity(usuarioService.buscarPorId(id)));
        model.addAttribute("isEdit", true);
        return "usuarios/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute UsuarioDTO dto) {
        usuarioService.salvarUsuario(dto.toEntity());
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return "redirect:/usuarios";
    }
}
