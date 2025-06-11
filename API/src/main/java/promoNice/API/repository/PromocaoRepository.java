package promoNice.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import promoNice.API.model.PromocaoModel;

import java.util.Optional;

@Repository
public interface PromocaoRepository extends CrudRepository<PromocaoModel, Long> {
    Optional<PromocaoModel> findByProdutos_Id(Long produtoId); // produtos é o nome do campo no model
}
