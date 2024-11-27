package promoNice.API.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import promoNice.API.dto.ProdutoDTO;
import promoNice.API.model.ProdutoModel;
import promoNice.API.model.PromocaoModel;
import promoNice.API.model.UsuarioModel;
import promoNice.API.repository.ProdutoRepository;
import promoNice.API.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("/listar-todos")
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        List<ProdutoModel> produtos = (List<ProdutoModel>) produtoRepository.findAll();
        List<ProdutoDTO> produtosDTO = produtos.stream().map(produto -> modelMapper.map(produto, ProdutoDTO.class)).toList();
        return ResponseEntity.ok(produtosDTO); // Retorna a lista de DTOs.
    }

    // Método para buscar um produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Integer id) {
        Optional<ProdutoModel> produto = produtoRepository.findById(id);
        if (produto.isPresent()) {
            ProdutoDTO produtoDTO = modelMapper.map(produto.get(), ProdutoDTO.class);
            return ResponseEntity.ok(produtoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Método para cadastrar um novo produto
    @PostMapping("/cadastrar/{id}")
    public ResponseEntity<ProdutoDTO> cadastrar(@RequestBody ProdutoDTO produtoDTO, @PathVariable Integer id) {

        // Recupera o usuário pelo ID
        UsuarioModel usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Converte o ProdutoDTO para ProdutoModel
        ModelMapper modelMapper = new ModelMapper();
        ProdutoModel produto = modelMapper.map(produtoDTO, ProdutoModel.class);

        // Define as promoções, se existirem, e estabelece a relação com o usuário
        if (produto.getPromocoes() != null) {
            for (PromocaoModel promocao : produto.getPromocoes()) {
                promocao.setProduto(produto); // Define a relação bidirecional
                promocao.setUsuario(usuario);
            }
        }

        // Salva o novo produto no banco de dados
        ProdutoModel novoProduto = produtoRepository.save(produto);

        // Converte a entidade ProdutoModel de volta para ProdutoDTO para retornar
        ProdutoDTO novoProdutoDTO = modelMapper.map(novoProduto, ProdutoDTO.class);

        // Retorna a resposta com o ProdutoDTO
        return ResponseEntity.ok(novoProdutoDTO);
    }

    // Método para atualizar um produto existente
    @PutMapping("alterar/{id}")
    public ResponseEntity<ProdutoDTO> alterar(@PathVariable Integer id, @RequestBody ProdutoDTO produtoAtualizadoDTO) {
        return produtoRepository.findById(id).map(produtoExistente -> {
            // Atualizar os campos
            produtoExistente.setNome(produtoAtualizadoDTO.getNome());
            produtoExistente.setDescricao(produtoAtualizadoDTO.getDescricao());
            produtoExistente.setUrlProduto(produtoAtualizadoDTO.getUrlProduto());

            // Atualizar promoções, se necessário (opcional, dependendo da lógica)
            if (produtoAtualizadoDTO.getPromocoes() != null) {
                produtoExistente.setPromocoes(produtoAtualizadoDTO.getPromocoes().stream().map(promocaoDTO -> modelMapper.map(promocaoDTO, PromocaoModel.class)).toList());
            }

            ProdutoModel produtoSalvo = produtoRepository.save(produtoExistente);
            ProdutoDTO produtoSalvoDTO = modelMapper.map(produtoSalvo, ProdutoDTO.class);
            return ResponseEntity.ok(produtoSalvoDTO);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Método para deletar um produto por ID
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
