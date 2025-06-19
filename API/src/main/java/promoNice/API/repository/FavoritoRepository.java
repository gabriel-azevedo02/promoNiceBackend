package promoNice.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import promoNice.API.model.FavoritoModel;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<FavoritoModel, Long> {
    List<FavoritoModel> findByUsuarioId(Long usuarioId);

    Optional<FavoritoModel> findByUsuarioIdAndProdutoId(Long usuarioId, Long produtoId);

    void deleteByUsuarioIdAndProdutoId(Long usuarioId, Long produtoId);

    boolean existsByUsuarioIdAndProdutoId(Long usuarioId, Long produtoId);
}