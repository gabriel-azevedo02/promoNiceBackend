package promoNice.API.model;

import jakarta.persistence.*;

@Entity
@Table(name = "produtos_lojas")
public class ProdutoLojaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoModel produto;

    @ManyToOne
    @JoinColumn(name = "loja_id")
    private LojaModel lojas;

    @Column(name = "url_produto_loja")
    private String urlProdutoLoja;

}
