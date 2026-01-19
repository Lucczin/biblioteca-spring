package com.biblioteca.biblioteca_spring.biblioteca.controller;

import org.springframework.ui.Model;
import com.biblioteca.biblioteca_spring.biblioteca.service.LivroService;
import com.biblioteca.biblioteca_spring.biblioteca.service.UsuarioService;
import com.biblioteca.biblioteca_spring.biblioteca.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final EmprestimoService emprestimoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");

        try {
            model.addAttribute("totalLivros", livroService.countTotal());
            model.addAttribute("livrosDisponiveis", livroService.countDisponiveis());
            model.addAttribute("totalUsuarios", usuarioService.countTotal());
            model.addAttribute("emprestimosAtivos", emprestimoService.countAtivos());
            model.addAttribute("emprestimosAtrasados", emprestimoService.countAtrasados());

            // Últimos empréstimos
            model.addAttribute("ultimosEmprestimos", emprestimoService.listarUltimosEmprestimos(5));

            // Livros com baixo estoque
            model.addAttribute("livrosBaixoEstoque", livroService.findBaixoEstoque(3));

            // Próximas devoluções
            model.addAttribute("proximasDevolucoes", emprestimoService.buscarPrestesAVencer(3));

        } catch (Exception e) {
            // Valores padrão se algum service falhar
            model.addAttribute("totalLivros", 0);
            model.addAttribute("livrosDisponiveis", 0);
            model.addAttribute("totalUsuarios", 0);
            model.addAttribute("emprestimosAtivos", 0);
            model.addAttribute("emprestimosAtrasados", 0);
            model.addAttribute("ultimosEmprestimos", List.of());
            model.addAttribute("livrosBaixoEstoque", List.of());
            model.addAttribute("proximasDevolucoes", List.of());
            model.addAttribute("errorMessage", "Algumas estatísticas não estão disponíveis: " + e.getMessage());
        }

        return "dashboard";
    }
}