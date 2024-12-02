package promoNice.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import promoNice.API.dto.LoginDTO;
import promoNice.API.model.UsuarioModel;
import promoNice.API.service.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody LoginDTO loginDTO) {
        // Buscar o usuário pelo email e senha
        UsuarioModel usuario = usuarioService.buscarPorEmailESenha(loginDTO.getEmail(), loginDTO.getSenha());

        if (usuario != null) {
            // Retornar informações adicionais do usuário (como ID e nome)
            return ResponseEntity.ok(Map.of(
                    "id", usuario.getId(),
                    "nome", usuario.getNome(),
                    "email", usuario.getEmail()

            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login ou senha inválidos."));
        }
    }

}
