package promoNice.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import promoNice.API.dto.LoginDTO;
import promoNice.API.service.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody LoginDTO loginDTO) {
        boolean autenticado = usuarioService.verificarCredenciais(loginDTO.getEmail(), loginDTO.getSenha());

        if (autenticado) {
            return ResponseEntity.ok(Map.of("message", "Login efetuado com sucesso!"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login ou senha inválidos."));
        }
    }

}
