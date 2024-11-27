package promoNice.API.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

public class PromocaoDTO {

    private Long id;
    @JsonIgnoreProperties("promocoes")
    private ProdutoDTO produto;
    private UsuarioDTO usuario;
    private Double preco;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String urlPromocao;

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
