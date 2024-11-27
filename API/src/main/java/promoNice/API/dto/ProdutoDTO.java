package promoNice.API.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class ProdutoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String urlProduto;
    @JsonIgnoreProperties("produto")
    private List<PromocaoDTO> promocoes;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUrlProduto() {
        return urlProduto;
    }

    public void setUrlProduto(String urlProduto) {
        this.urlProduto = urlProduto;
    }

    public List<PromocaoDTO> getPromocoes() {
        return promocoes;
    }

    public void setPromocoes(List<PromocaoDTO> promocoes) {
        this.promocoes = promocoes;
    }
}
