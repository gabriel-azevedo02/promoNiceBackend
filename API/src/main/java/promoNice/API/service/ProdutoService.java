package promoNice.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import promoNice.API.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository  produtoRepository;

}
