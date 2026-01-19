package com.biblioteca.biblioteca_spring.biblioteca.controller;

import com.biblioteca.biblioteca_spring.biblioteca.dto.EmprestimoRequestDTO;
import com.biblioteca.biblioteca_spring.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca_spring.biblioteca.service.EmprestimoService;
import com.biblioteca.biblioteca_spring.biblioteca.service.UsuarioService;
import com.biblioteca.biblioteca_spring.biblioteca.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final UsuarioService usuarioService;
    private final LivroService livroService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        return "emprestimos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("livros", livroService.listarTodos());
        return "emprestimos/form";
    }

    @PostMapping
    public String salvar(@RequestParam Long usuarioId,
                         @RequestParam Long livroId) {

        EmprestimoRequestDTO dto = new EmprestimoRequestDTO();
        dto.setUsuarioId(usuarioId);
        dto.setLivroId(livroId);

        emprestimoService.criarEmprestimo(dto);

        return "redirect:/emprestimos";
    }

    @PostMapping("/{id}/devolver")
    public String devolver(@PathVariable Long id) {
        emprestimoService.devolverLivro(id);
        return "redirect:/emprestimos";
    }
}

