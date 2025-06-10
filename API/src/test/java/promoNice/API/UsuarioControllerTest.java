package promoNice.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import promoNice.API.controller.UsuarioController;
import promoNice.API.model.UsuarioModel;
import promoNice.API.repository.UsuarioRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarPorId_deveRetornarUsuario_quandoExistir() throws Exception {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1);
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("123");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuario/listar-por/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("João")))
                .andExpect(jsonPath("$.email", is("joao@email.com")));
    }

    @Test
    void listarPorId_deveRetornarNotFound_quandoNaoExistir() throws Exception {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuario/listar-por/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarTodos_deveRetornarListaDeUsuarios() throws Exception {
        UsuarioModel u1 = new UsuarioModel();
        u1.setId(1);
        u1.setNome("João");
        u1.setEmail("joao@email.com");
        u1.setSenha("123");

        UsuarioModel u2 = new UsuarioModel();
        u2.setId(2);
        u2.setNome("Maria");
        u2.setEmail("maria@email.com");
        u2.setSenha("abc");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        mockMvc.perform(get("/api/usuario/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("João")))
                .andExpect(jsonPath("$[1].nome", is("Maria")));
    }

    @Test
    void salvar_deveCriarUsuario() throws Exception {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome("Ana");
        usuario.setEmail("ana@email.com");
        usuario.setSenha("senha");

        UsuarioModel usuarioSalvo = new UsuarioModel();
        usuarioSalvo.setId(10);
        usuarioSalvo.setNome("Ana");
        usuarioSalvo.setEmail("ana@email.com");
        usuarioSalvo.setSenha("senha");

        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuarioSalvo);

        // Capturador do argumento passado para o save
        ArgumentCaptor<UsuarioModel> captor = ArgumentCaptor.forClass(UsuarioModel.class);

        mockMvc.perform(post("/api/usuario/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.nome", is("Ana")))
                .andExpect(jsonPath("$.email", is("ana@email.com")));

        // Verifica que o save foi chamado e captura o argumento
        verify(usuarioRepository).save(captor.capture());

        // Pega o objeto salvo e printa (pode ser no console, log, etc)
        UsuarioModel usuarioSalvoNoMock = captor.getValue();
        System.out.println("Usuário que foi passado para save no mock: " + usuarioSalvoNoMock.getNome() + ", " + usuarioSalvoNoMock.getEmail());
    }

    @Test
    void alterar_deveAtualizarUsuario_quandoExistir() throws Exception {
        UsuarioModel usuarioExistente = new UsuarioModel();
        usuarioExistente.setId(1);
        usuarioExistente.setNome("Velho Nome");
        usuarioExistente.setEmail("velho@email.com");
        usuarioExistente.setSenha("123");

        UsuarioModel usuarioAtualizado = new UsuarioModel();
        usuarioAtualizado.setNome("Novo Nome");
        usuarioAtualizado.setEmail("novo@email.com");
        usuarioAtualizado.setSenha("abc");

        UsuarioModel usuarioSalvo = new UsuarioModel();
        usuarioSalvo.setId(1);
        usuarioSalvo.setNome("Novo Nome");
        usuarioSalvo.setEmail("novo@email.com");
        usuarioSalvo.setSenha("abc");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuarioSalvo);

        ArgumentCaptor<UsuarioModel> captor = ArgumentCaptor.forClass(UsuarioModel.class);

        mockMvc.perform(put("/api/usuario/alterar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Novo Nome")))
                .andExpect(jsonPath("$.email", is("novo@email.com")));

        verify(usuarioRepository).save(captor.capture());

        UsuarioModel capturado = captor.getValue();
        System.out.println("[ALTERAR] Objeto passado para save: " + capturado.getNome() + ", " + capturado.getEmail());
    }


    @Test
    void alterar_deveRetornarNotFound_quandoUsuarioNaoExistir() throws Exception {
        UsuarioModel usuarioAtualizado = new UsuarioModel();
        usuarioAtualizado.setNome("Novo Nome");
        usuarioAtualizado.setEmail("novo@email.com");
        usuarioAtualizado.setSenha("abc");

        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuario/alterar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioAtualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRemoverUsuario_quandoExistir() throws Exception {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1);
        usuario.setNome("Usuário");
        usuario.setEmail("usuario@email.com");
        usuario.setSenha("123");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(any(UsuarioModel.class));

        ArgumentCaptor<UsuarioModel> captor = ArgumentCaptor.forClass(UsuarioModel.class);

        mockMvc.perform(delete("/api/usuario/deletar/1"))
                .andExpect(status().isNoContent());

        verify(usuarioRepository).delete(captor.capture());

        UsuarioModel capturado = captor.getValue();
        System.out.println("[DELETAR] Objeto passado para delete: " + capturado.getNome() + ", " + capturado.getEmail());
    }

    @Test
    void deletar_deveRetornarNotFound_quandoNaoExistir() throws Exception {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/usuario/deletar/1"))
                .andExpect(status().isNotFound());

        verify(usuarioRepository, never()).delete(any());
    }
}
