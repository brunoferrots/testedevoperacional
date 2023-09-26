package services;

import models.Produto;

import java.util.List;

public class ProdutoService {
    private EmpresaService empresaService = new EmpresaService();

    public List<Produto> listarProdutos() {
        var empresas = empresaService.listarEmpresas();
        var empresa = empresas.get(0);
        var empresa2 = empresas.get(1);
        var empresa3 = empresas.get(2);

        Produto produto = new Produto(1, "Pão Frances", 5, 3.50, empresa);
        Produto produto2 = new Produto(2, "Coturno", 10, 400.0, empresa2);
        Produto produto3 = new Produto(3, "Jaqueta Jeans", 15, 150.0, empresa2);
        Produto produto4 = new Produto(4, "Calça Sarja", 15, 150.0, empresa2);
        Produto produto5 = new Produto(5, "Prato feito - Frango", 10, 25.0, empresa3);
        Produto produto6 = new Produto(6, "Prato feito - Carne", 10, 25.0, empresa3);
        Produto produto7 = new Produto(7, "Suco Natural", 30, 10.0, empresa3);
        Produto produto8 = new Produto(8, "Sonho", 5, 8.50, empresa);
        Produto produto9 = new Produto(9, "Croissant", 7, 6.50, empresa);
        Produto produto10 = new Produto(10, "Ché Gelado", 4, 5.50, empresa);

        return List.of(produto, produto2, produto3, produto4, produto5, produto6, produto7, produto8, produto9, produto10);
    }

}
