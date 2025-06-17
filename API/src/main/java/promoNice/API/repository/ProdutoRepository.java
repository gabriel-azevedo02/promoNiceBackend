package promoNice.API.repository;

import org.springframework.data.repository.CrudRepository;
import promoNice.API.model.ProdutoModel;

import java.util.List;

public interface ProdutoRepository extends CrudRepository<ProdutoModel, Long> {

    List<ProdutoModel> findByNomeContainingIgnoreCase(String nome);
}
