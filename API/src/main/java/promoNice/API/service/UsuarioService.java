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

    public UsuarioModel buscarPorEmailESenha(String email, String senha) {
        // Verificar o usuário no banco de dados
        return usuarioRepository.findByEmailAndSenha(email, senha);
    }
}
