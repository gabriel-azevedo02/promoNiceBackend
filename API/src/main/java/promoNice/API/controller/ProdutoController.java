package promoNice.API.controller;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import promoNice.API.dto.ProdutoDTO;
import promoNice.API.dto.PromocaoDTO;
import promoNice.API.model.ProdutoModel;
import promoNice.API.model.PromocaoModel;
import promoNice.API.model.UsuarioModel;
import promoNice.API.repository.ProdutoRepository;
import promoNice.API.repository.PromocaoRepository;
import promoNice.API.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PromocaoRepository promocaoRepository;


    @GetMapping("/listar-todos")
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        // Inicializando o ModelMapper com configuração
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(PromocaoModel.class, PromocaoDTO.class)
                .addMappings(mapper -> mapper.skip(PromocaoDTO::setProduto));

        // Mapeando lista de ProdutoModel para ProdutoDTO
        List<ProdutoModel> produtos = (List<ProdutoModel>) produtoRepository.findAll();
        List<ProdutoDTO> produtosDTO = produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoDTO.class))
                .toList();

        return ResponseEntity.ok(produtosDTO);
    }

    // Método para buscar um produto por ID
    @GetMapping("/listar-por/{id}")
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
    public ResponseEntity<?> alterar(@PathVariable Integer id, @RequestBody ProdutoDTO produtoAtualizadoDTO,  @RequestHeader("usuario-id") Long usuarioId) {
        ProdutoModel produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Validação - verificar se todas as promoções pertencem ao usuário
        boolean todasSaoDoUsuario = produtoExistente.getPromocoes().stream()
                .allMatch(promocao -> promocao.getUsuario().getId().equals(usuarioId));

        if (!todasSaoDoUsuario) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para alterar este produto pois não é dono de todas as promoções.");
        }

        // Atualiza os campos básicos apenas se os valores não forem nulos
        if (produtoAtualizadoDTO.getNome() != null) {
            produtoExistente.setNome(produtoAtualizadoDTO.getNome());
        }
        if (produtoAtualizadoDTO.getDescricao() != null) {
            produtoExistente.setDescricao(produtoAtualizadoDTO.getDescricao());
        }
        if (produtoAtualizadoDTO.getUrlProduto() != null) {
            produtoExistente.setUrlProduto(produtoAtualizadoDTO.getUrlProduto());
        }

        // Atualizar promoções mantendo a referência do produto
        if (produtoAtualizadoDTO.getPromocoes() != null) {
            produtoExistente.getPromocoes().clear(); // Remove promoções antigas
            List<PromocaoModel> novasPromocoes = produtoAtualizadoDTO.getPromocoes().stream()
                    .map(promocaoDTO -> {
                        PromocaoModel promocao = modelMapper.map(promocaoDTO, PromocaoModel.class);
                        promocao.setProduto(produtoExistente); // Mantém a relação
                        return promocao;
                    })
                    .collect(Collectors.toList());

            produtoExistente.getPromocoes().addAll(novasPromocoes); // Adiciona novas promoções
        }

        ProdutoModel produtoSalvo = produtoRepository.save(produtoExistente);
        ProdutoDTO produtoSalvoDTO = modelMapper.map(produtoSalvo, ProdutoDTO.class);

        return ResponseEntity.ok(produtoSalvoDTO);
    }

    // Método para deletar um produto por ID
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<?> deletar(
            @PathVariable Integer id,
            @RequestHeader("usuario-id") Long usuarioId) {

        // Verifica se o produto existe
        Optional<ProdutoModel> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Busca a promoção associada a esse produto
        Optional<PromocaoModel> promocaoOptional = promocaoRepository.findByProdutos_Id(id.longValue());

        if (promocaoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Promoção não encontrada para este produto.");
        }

        PromocaoModel promocao = promocaoOptional.get();

        // Verifica se o usuário da promoção é o mesmo do header
        if (!promocao.getUsuario().getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para excluir esse produto.");
        }

        // Tudo certo, pode excluir o produto
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
