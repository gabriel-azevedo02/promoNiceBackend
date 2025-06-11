package promoNice.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import promoNice.API.model.UsuarioModel;
import promoNice.API.repository.UsuarioRepository;

import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método GET listar por id

    @GetMapping(path = "/api/usuario/listar-por/{idUsuarios}")
    public ResponseEntity consultar(@PathVariable("idUsuarios") Long idUsuarios) {
        return usuarioRepository.findById(idUsuarios)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

//Método GET listar todos

    @GetMapping(path = "/api/usuario/listar")
    public ResponseEntity<List<UsuarioModel>> listarTodos() {
        List<UsuarioModel> usuarios = (List<UsuarioModel>) usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios); // Encapsula a lista dentro de um ResponseEntity.
    }
//Método POST salvar

    @PostMapping(path = "/api/usuario/salvar")
    public UsuarioModel salvar(@RequestBody UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    //Método PUT alterar

    @PutMapping("/api/usuario/alterar/{id}")
    public ResponseEntity<UsuarioModel> alterar(@PathVariable Long id, @RequestBody UsuarioModel usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    // Atualizar os campos
                    usuarioExistente.setNome(usuarioAtualizado.getNome());
                    usuarioExistente.setEmail(usuarioAtualizado.getEmail());
                    usuarioExistente.setSenha(usuarioAtualizado.getSenha());
                    UsuarioModel usuarioSalvo = usuarioRepository.save(usuarioExistente);
                    return ResponseEntity.ok(usuarioSalvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Método deletar

    @DeleteMapping("/api/usuario/deletar/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
