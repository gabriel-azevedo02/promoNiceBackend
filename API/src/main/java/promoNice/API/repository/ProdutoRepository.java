package promoNice.API.repository;

import org.springframework.data.repository.CrudRepository;
import promoNice.API.model.ProdutoModel;

public interface ProdutoRepository extends CrudRepository<ProdutoModel, Long> {
}
