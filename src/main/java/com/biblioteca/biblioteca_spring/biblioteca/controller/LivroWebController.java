package com.biblioteca.biblioteca_spring.biblioteca.controller;

import com.biblioteca.biblioteca_spring.biblioteca.dto.LivroDTO;
import com.biblioteca.biblioteca_spring.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroWebController {

    private final LivroService livroService;

    // =============================
    // LISTAR LIVROS
    // =============================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pageTitle", "Livros");
        model.addAttribute("livros", livroService.listarTodos());
        return "livros/lista";
    }

    // =============================
    // FORMULÁRIO NOVO LIVRO
    // =============================
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("pageTitle", "Novo Livro");
        model.addAttribute("livroDTO", new LivroDTO());
        model.addAttribute("isEdit", false);
        return "livros/form";
    }

    // =============================
    // FORMULÁRIO EDITAR LIVRO
    // =============================
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Editar Livro");
        model.addAttribute("livroDTO", livroService.buscarPorId(id));
        model.addAttribute("isEdit", true);
        return "livros/form";
    }

    // =============================
    // SALVAR (CRIAR OU ATUALIZAR)
    // =============================
    @PostMapping
    public String salvar(@ModelAttribute LivroDTO livroDTO) {

        if (livroDTO.getId() == null) {
            livroService.criarLivro(livroDTO);
        } else {
            livroService.atualizarLivro(livroDTO.getId(), livroDTO);
        }

        return "redirect:/livros";
    }


    // =============================
    // EXCLUIR LIVRO
    // =============================
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            livroService.deletarLivro(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Livro excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", e.getMessage());
        }
        return "redirect:/livros";
    }

}