package promoNice.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import promoNice.API.model.ProdutoModel;
import promoNice.API.repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository  produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<ProdutoModel> buscarPorNome(String filtro) {
        return produtoRepository.findByNomeContainingIgnoreCase(filtro);
    }
}
