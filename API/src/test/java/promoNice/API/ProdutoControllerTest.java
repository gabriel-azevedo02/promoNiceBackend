package promoNice.API;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import promoNice.API.controller.ProdutoController;
import promoNice.API.dto.ProdutoDTO;
import promoNice.API.model.ProdutoModel;
import promoNice.API.model.UsuarioModel;
import promoNice.API.repository.ProdutoRepository;
import promoNice.API.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoRepository produtoRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private ModelMapper modelMapper;


    @Test
    void RetornoListaDeProdutos() throws Exception {
        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(1L);
        produtoModel.setNome("Produto Teste");
        produtoModel.setDescricao("Descrição");
        produtoModel.setUrlProduto("http://imagem.com");

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        produtoDTO.setNome("Produto Teste");
        produtoDTO.setDescricao("Descrição");
        produtoDTO.setUrlProduto("http://imagem.com");

        when(produtoRepository.findAll()).thenReturn(List.of(produtoModel));
        when(modelMapper.map(produtoModel, ProdutoDTO.class)).thenReturn(produtoDTO);

        mockMvc.perform(get("/api/produtos/listar-todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Produto Teste"))
                .andExpect(jsonPath("$[0].descricao").value("Descrição"))
                .andExpect(jsonPath("$[0].urlProduto").value("http://imagem.com"));
    }

    @Test
    void buscarPorId_deveRetornarProduto_quandoIdValido() throws Exception {
        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(1L);
        produtoModel.setNome("Produto Teste");

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        produtoDTO.setNome("Produto Teste");

        when(produtoRepository.findById(1L)).thenReturn(java.util.Optional.of(produtoModel));
        when(modelMapper.map(produtoModel, ProdutoDTO.class)).thenReturn(produtoDTO);

        mockMvc.perform(get("/api/produtos/listar-por/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoIdInvalido() throws Exception {
        // Simula que nenhum produto foi encontrado com o ID fornecido
        when(produtoRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produtos/listar-por/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cadastrar_deveRetornarProduto_quandoSucesso() throws Exception {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Produto Novo");
        produtoDTO.setDescricao("Descrição");

        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setNome("Produto Novo");
        produtoModel.setDescricao("Descrição");

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);

        ProdutoModel salvo = new ProdutoModel();
        salvo.setId(10L);
        salvo.setNome("Produto Novo");

        ProdutoDTO salvoDTO = new ProdutoDTO();
        salvoDTO.setId(10L);
        salvoDTO.setNome("Produto Novo");

        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(usuario));
        when(modelMapper.map(any(ProdutoDTO.class), eq(ProdutoModel.class))).thenReturn(produtoModel);
        when(produtoRepository.save(any(ProdutoModel.class))).thenReturn(salvo);
        when(modelMapper.map(any(ProdutoModel.class), eq(ProdutoDTO.class))).thenReturn(salvoDTO);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/api/produtos/cadastrar/1")
                        .contentType("application/json")
                        .content("{\"nome\":\"Produto Novo\", \"descricao\":\"Descrição\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Produto Novo"));

        // Captura o produto que foi salvo
        ArgumentCaptor<ProdutoModel> captor = ArgumentCaptor.forClass(ProdutoModel.class);
        verify(produtoRepository).save(captor.capture());

        ProdutoModel produtoCapturado = captor.getValue();

        // Faz os asserts
        assertEquals("Produto Novo", produtoCapturado.getNome());
        assertEquals("Descrição", produtoCapturado.getDescricao());

        // Faz o print no console
        System.out.println("Produto salvo no mock: " + produtoCapturado.getNome() + ", " + produtoCapturado.getDescricao());
    }

    @Test
    void alterar_deveAtualizarProduto_quandoIdValido() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Atualizado");

        ProdutoModel existente = new ProdutoModel();
        existente.setId(1L);
        existente.setNome("Antigo");

        ProdutoModel salvo = new ProdutoModel();
        salvo.setId(1L);
        salvo.setNome("Atualizado");

        ProdutoDTO salvoDTO = new ProdutoDTO();
        salvoDTO.setId(1L);
        salvoDTO.setNome("Atualizado");

        when(produtoRepository.findById(1L)).thenReturn(java.util.Optional.of(existente));
        when(produtoRepository.save(existente)).thenReturn(salvo);
        when(modelMapper.map(salvo, ProdutoDTO.class)).thenReturn(salvoDTO);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/api/produtos/alterar/1")
                        .contentType("application/json")
                        .content("{\"nome\":\"Atualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
        ArgumentCaptor<ProdutoModel> captor = ArgumentCaptor.forClass(ProdutoModel.class);
        verify(produtoRepository).save(captor.capture());

        ProdutoModel produtoAlterado = captor.getValue();

// Print no console com nome e ID
        System.out.println("Produto alterado no mock: ID=" + produtoAlterado.getId() + ", Nome=" + produtoAlterado.getNome());
    }

    @Test
    void deletar_deveRetornarNoContent_quandoIdValido() throws Exception {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete("/api/produtos/deletar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_deveRetornarNotFound_quandoIdInvalido() throws Exception {
        when(produtoRepository.existsById(999L)).thenReturn(false);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete("/api/produtos/deletar/999"))
                .andExpect(status().isNotFound());
    }

}
