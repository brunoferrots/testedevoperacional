package services;

import models.Cliente;
import models.Empresa;
import models.Produto;
import models.Venda;

import java.util.List;

public class VendaService {

    public static Venda criarVenda(List<Produto> carrinho, Empresa empresa, Cliente cliente, List<Venda> vendas) {
        Double total = carrinho.stream().mapToDouble(Produto::getPreco).sum();
        Double comissaoSistema = total * empresa.getTaxa();
        int idVenda = vendas.isEmpty() ? 1 : vendas.get(vendas.size() - 1).getCodigo() + 1;
        Venda venda = new Venda(idVenda, carrinho.stream().toList(), total, comissaoSistema, empresa, cliente);
        empresa.setSaldo(empresa.getSaldo() + total);
        vendas.add(venda);
        return venda;
    }
}
