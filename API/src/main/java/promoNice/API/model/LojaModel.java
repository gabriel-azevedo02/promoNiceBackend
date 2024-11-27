package promoNice.API.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "lojas")
public class LojaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "url_site")
    private String urlSite;

    @Column(name = "descricao")
    private String descricao;

    // Relacionamento com produtos_lojas
    @OneToMany(mappedBy = "lojas", cascade = CascadeType.ALL)
    private List<ProdutoLojaModel> produtosLojas;
}
