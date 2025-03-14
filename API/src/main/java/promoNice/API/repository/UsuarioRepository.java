package promoNice.API.repository;

import org.springframework.data.repository.CrudRepository;
import promoNice.API.model.UsuarioModel;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<UsuarioModel, Integer> {
        UsuarioModel findByEmailAndSenha(String email, String senha);
    }

