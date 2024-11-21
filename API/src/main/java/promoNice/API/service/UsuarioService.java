package promoNice.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import promoNice.API.model.UsuarioModel;
import promoNice.API.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean verificarCredenciais(String email, String senha) {
        Optional<UsuarioModel> usuario = usuarioRepository.findByEmailAndSenha(email, senha);
        return usuario.isPresent();
    }
}
