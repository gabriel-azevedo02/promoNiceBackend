package promoNice.API.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import promoNice.API.model.ProdutoModel;

import java.time.LocalDate;

public class FavoritoDTO {
    private Long id;
    @JsonIgnoreProperties("promocoes")
    private ProdutoDTO produto;
    private UsuarioDTO usuario;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

}
