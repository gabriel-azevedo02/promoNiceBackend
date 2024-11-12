package promoNice.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import promoNice.API.model.UsuarioModel;
import promoNice.API.repository.UsuarioRepository;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(path = "/api/usuario/{idUsuarios}")
    public ResponseEntity consultar(@PathVariable("idUsuarios") Integer idUsuarios){
        return usuarioRepository.findById(idUsuarios)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

   @PostMapping(path = "/api/usuario/salvar")
    public UsuarioModel salvar(@RequestBody UsuarioModel usuario){
        return usuarioRepository.save(usuario);
    }
}
