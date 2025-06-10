package promoNice.API.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import promoNice.API.dto.LoginDTO;
import promoNice.API.model.UsuarioModel;
import promoNice.API.service.UsuarioService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void autenticar_deveRetornarUsuario_quandoCredenciaisValidas() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("ana@email.com");
        loginDTO.setSenha("senha123");

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1);
        usuario.setNome("Ana");
        usuario.setEmail("ana@email.com");

        Mockito.when(usuarioService.buscarPorEmailESenha(anyString(), anyString())).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@email.com"));
        // Print do resultado simulado
        System.out.println("Login realizado com sucesso: " + usuario.getNome() + " - " + usuario.getEmail());
    }

    @Test
    void autenticar_deveRetornarUnauthorized_quandoCredenciaisInvalidas() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("erro@emailcom");
        loginDTO.setSenha("invalida");

        Mockito.when(usuarioService.buscarPorEmailESenha(anyString(), anyString())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Login ou senha inválidos."));
        System.out.println("Tentativa de login falhou para: " + loginDTO.getEmail());
    }
}
