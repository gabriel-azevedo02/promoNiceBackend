package promoNice.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import promoNice.API.dto.FavoritoDTO;
import promoNice.API.dto.ProdutoDTO;
import promoNice.API.service.FavoritoService;

import java.util.List;

// Métodos para favoritos
@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    public ResponseEntity<?> favoritar(@RequestBody FavoritoDTO favoritoDTO) {
        favoritoService.favoritar(
                favoritoDTO.getUsuario().getId(),
                favoritoDTO.getProduto().getId()
        );
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/{usuarioId}/{produtoId}")
    public ResponseEntity<?> desfavoritar(@PathVariable Long usuarioId, @PathVariable Long produtoId) {
        favoritoService.desfavoritar(usuarioId, produtoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<ProdutoDTO>> listarFavoritos(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(favoritoService.listarFavoritos(usuarioId));
    }

    @GetMapping("/todos-com-favoritos/{usuarioId}")
    public ResponseEntity<List<ProdutoDTO>> listarTodosComFavoritos(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(favoritoService.listarTodosComFavoritos(usuarioId));
    }
}

