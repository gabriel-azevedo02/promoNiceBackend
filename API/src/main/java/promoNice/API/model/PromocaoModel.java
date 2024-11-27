package promoNice.API.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "promocoes")
public class PromocaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoModel produtos;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;

    @Column(name = "preco")
    private Double preco;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "url_promocao")
    private String urlPromocao;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdutoModel getProduto() {
        return produtos;
    }

    public void setProduto(ProdutoModel produto) {
        this.produtos = produto;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getUrlPromocao() {
        return urlPromocao;
    }

    public void setUrlPromocao(String urlPromocao) {
        this.urlPromocao = urlPromocao;
    }
}

