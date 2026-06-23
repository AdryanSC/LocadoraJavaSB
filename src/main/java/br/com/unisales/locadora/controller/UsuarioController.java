package br.com.unisales.locadora.controller;

import br.com.unisales.locadora.model.Usuario;
import br.com.unisales.locadora.repository.UsuarioRepository;
import br.com.unisales.locadora.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public Usuario cadastrar(@RequestBody Usuario usuario) {
        return service.cadastrar(usuario);
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario loginDados) {
        Usuario usuario = repository.findByUsername(loginDados.getUsername());
        if (usuario != null && passwordEncoder.matches(loginDados.getPassword(), usuario.getPassword())) {
            return "Login realizado! Bem-vindo, " + usuario.getUsername();
        }
        return "Usuário ou senha incorretos.";
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/alterar/{id}")
    public Usuario alterar(@PathVariable Long id, @RequestBody Usuario dadosNovos) {
        return service.alterar(id, dadosNovos);
    }
}