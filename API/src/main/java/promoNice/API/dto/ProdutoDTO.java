package promoNice.API.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import promoNice.API.model.ProdutoModel;

import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String urlProduto;
    @JsonIgnoreProperties("produto")
    private List<PromocaoDTO> promocoes;
    private boolean favorito;


    public static ProdutoDTO fromModel(ProdutoModel produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setUrlProduto(produto.getUrlProduto());
        dto.setFavorito(false);

        if (produto.getPromocoes() != null) {
            dto.setPromocoes(
                    produto.getPromocoes()
                            .stream()
                            .map(PromocaoDTO::fromModel)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    // Getters e Setters
    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

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
