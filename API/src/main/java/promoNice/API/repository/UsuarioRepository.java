package promoNice.API.repository;

import org.springframework.data.repository.CrudRepository;
import promoNice.API.model.UsuarioModel;

public interface UsuarioRepository extends CrudRepository<UsuarioModel, Integer> {
}
