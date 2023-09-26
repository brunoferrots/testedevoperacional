package services;

import models.*;

import java.util.List;

public record DadosExcutar(List<Usuario> usuarios, List<Cliente> clientes, List<Empresa> empresas, List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
}
