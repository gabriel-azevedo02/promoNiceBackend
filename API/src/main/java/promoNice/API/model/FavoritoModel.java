package promoNice.API.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos")
public class FavoritoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;

    @ManyToOne
    @JoinColumn(name = "promocao_id")
    private PromocaoModel promocao;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoModel produto;

    private LocalDateTime data_favoritado;

    // Getters e Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public UsuarioModel getUsuario() { return usuario; }

    public void setUsuario(UsuarioModel usuario) { this.usuario = usuario; }

    public PromocaoModel getPromocoes() { return promocao; }

    public void setPromocoes(PromocaoModel promocoes) { this.promocao = promocoes; }

    public ProdutoModel getProduto() { return produto; }

    public void setProduto(ProdutoModel produtos) { this.produto = produtos; }

    public LocalDateTime getData_favoritado() { return data_favoritado; }

    public void setData_favoritado(LocalDateTime data_favoritado) {
        this.data_favoritado = data_favoritado;
    }
}