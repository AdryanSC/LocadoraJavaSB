package br.com.unisales.locadora;

import br.com.unisales.locadora.model.Usuario;
import br.com.unisales.locadora.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LocadoraGamesVulneravelApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    // Teste padrão
    @Test
    void contextLoads() {
    }

    @Test
    void testLoginNaoPermiteSqlInjection() throws Exception {
        mockMvc.perform(post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"' OR '1'='1\", \"password\": \"' OR '1'='1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário ou senha incorretos."));
    }

    @Test
    void testSenhaEArmazenadaComHash() {
        String senhaOriginal = "senha123";
        String senhaHash = passwordEncoder.encode(senhaOriginal);

        assertNotEquals(senhaOriginal, senhaHash);
        assertTrue(senhaHash.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(senhaOriginal, senhaHash));
    }

    @Test
    void testRotaProtegidaRetorna401SemAutenticacao() throws Exception {
        mockMvc.perform(get("/jogos"))
                .andExpect(status().isUnauthorized());
    }
}