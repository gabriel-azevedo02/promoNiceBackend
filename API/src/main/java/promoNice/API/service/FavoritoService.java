package promoNice.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import promoNice.API.dto.ProdutoDTO;
import promoNice.API.model.FavoritoModel;
import promoNice.API.model.ProdutoModel;
import promoNice.API.model.UsuarioModel;
import promoNice.API.repository.FavoritoRepository;
import promoNice.API.repository.ProdutoRepository;
import promoNice.API.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void favoritar(Long usuarioId, Long produtoId) {
        boolean jaExiste = favoritoRepository.existsByUsuarioIdAndProdutoId(usuarioId, produtoId);
        if (jaExiste) return; // já favoritado, então não insere de novo

        UsuarioModel usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        ProdutoModel produto = produtoRepository.findById(produtoId).orElseThrow();

        FavoritoModel favorito = new FavoritoModel();
        favorito.setUsuario(usuario);
        favorito.setProduto(produto);
        favorito.setData_favoritado(LocalDateTime.now());

        favoritoRepository.save(favorito);
    }

    public void desfavoritar(Long usuarioId, Long produtoId) {
        favoritoRepository.deleteByUsuarioIdAndProdutoId(usuarioId, produtoId);
    }

    public List<ProdutoDTO> listarFavoritos(Long usuarioId) {
        List<FavoritoModel> favoritos = favoritoRepository.findByUsuarioId(usuarioId);

        return favoritos.stream()
                .map(fav -> {
                    ProdutoDTO dto = ProdutoDTO.fromModel(fav.getProduto());
                    dto.setFavorito(true);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ProdutoDTO> listarTodosComFavoritos(Long usuarioId) {
        List<ProdutoModel> todosProdutos = StreamSupport
                .stream(produtoRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        List<FavoritoModel> favoritos = favoritoRepository.findByUsuarioId(usuarioId);

        Set<Long> idsFavoritos = favoritos.stream()
                .map(f -> f.getProduto().getId())
                .collect(Collectors.toSet());

        return todosProdutos.stream()
                .map(prod -> {
                    ProdutoDTO dto = ProdutoDTO.fromModel(prod);
                    dto.setFavorito(idsFavoritos.contains(prod.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
