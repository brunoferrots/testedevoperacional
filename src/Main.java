import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import models.Cliente;
import models.Empresa;
import models.Produto;
import models.Usuario;
import models.Venda;
import services.*;

public class Main {
    /*
     *                                ## ALTERÇÕES NO SISTEMA ##
     * 1# Criei uma classe service para cada entidade, para melhorar a legibilidade
     * 2# Mudei todos os "x" nos forEach para melhorar o entendimento
     * 3# Criei um metodo para que o admin consiga visualizar as Empresas e Produtos com mais detalhe
     * 4# Melhorei o padrão de if inicial para if excluisivo, trantando o possivel erro de Usuário e Senha
     * 5# Criei um try catch resources para fechar o Scanner
     * 6# Criei um Record para facilitar a passagem de estados entre os metodos
     * 7# Restringi o número de casas decimais para o getComissaoSistema
     * 8# Retirei de todos os modelos a chamada para o contrutor Pai
     * 9# Criei as pastas para melhora a organização das classes
     */
    private static DadosExcutar dadosExcutar;
    public static void main(String[] args) throws Exception {

        // SIMULANDO BANCO DE DADOS

        List<Produto> carrinho = new ArrayList<Produto>();
        List<Venda> vendas = new ArrayList<Venda>();

        List<Usuario> usuarios = new UsuarioService().listarUsuarios();
        List<Cliente> clientes = new ClienteService().listarClients();
        List<Empresa> empresas = new EmpresaService().listarEmpresas();
        List<Produto> produtos = new ProdutoService().listarProdutos();

        dadosExcutar = new DadosExcutar(usuarios, clientes, empresas, produtos, carrinho, vendas);
        executar(dadosExcutar);
    }

    public static void executar(DadosExcutar dadosExcutar) throws Exception {
        var usuarios = dadosExcutar.usuarios();
        var clientes = dadosExcutar.clientes();
        var empresas = dadosExcutar.empresas();
        var produtos = dadosExcutar.produtos();
        var carrinho = dadosExcutar.carrinho();
        var vendas = dadosExcutar.vendas();

        try (var sc = new Scanner(System.in)) {
            System.out.println("Entre com seu usuário e senha:");
            System.out.print("Usuário: ");
            String username = sc.next();
            System.out.print("Senha: ");
            String senha = sc.next();

            List<Usuario> usuariosSearch = usuarios.stream()
                    .filter(usuario -> usuario.getUsername().equals(username))
                    .toList();

            if (usuariosSearch.isEmpty()) {
                System.out.println("Usuário não encontrado");
                executar(dadosExcutar);
            }

            Usuario usuarioLogado = usuariosSearch.get(0);
            if (!usuarioLogado.getSenha().equals(senha)) {
                System.out.println("Senha incorreta");
                executar(dadosExcutar);
            }

            System.out.println("Escolha uma opção para iniciar");

            if (usuarioLogado.IsAdmin()) {
                listarVendasAdmin(sc, dadosExcutar);
            }

            if (usuarioLogado.IsEmpresa()) {
                System.out.println("1 - Listar vendas");
                System.out.println("2 - Ver produtos");
                System.out.println("0 - Deslogar");

                Integer escolha = sc.nextInt();
                switch (escolha) {
                    case 1: {
                        System.out.println();
                        System.out.println("************************************************************");
                        System.out.println("VENDAS EFETUADAS");

                        vendas.forEach(venda -> {
                            if (venda.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
                                System.out.println("************************************************************");
                                System.out.println("Venda de código: "
                                        + venda.getCodigo() + " no CPF "
                                        + venda.getCliente().getCpf() + ": ");

                                venda.getItens().forEach(item -> {
                                    System.out.println(item.getId() + " - " + item.getNome() + "    R$" + item.getPreco());
                                });
                                System.out.println("Total Venda: R$" + venda.getValor());
                                System.out.printf("Total Taxa a ser paga: R$ %.2f", venda.getComissaoSistema());
                                System.out.println("Total Líquido  para empresa: "
                                        + (venda.getValor() - venda.getComissaoSistema()));
                                System.out.println("************************************************************");
                            }

                        });
                        System.out.println("Saldo Empresa: " + usuarioLogado.getEmpresa().getSaldo());
                        System.out.println("************************************************************");

                        executar(dadosExcutar);
                    }
                    case 2: {
                        System.out.println();
                        System.out.println("************************************************************");
                        System.out.println("MEUS PRODUTOS");
                        produtos.forEach(produto -> {
                            if (produto.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
                                System.out.println("************************************************************");
                                System.out.println("Código: " + produto.getId());
                                System.out.println("Produto: " + produto.getNome());
                                System.out.println("Quantidade em estoque: " + produto.getQuantidade());
                                System.out.println("Valor: R$" + produto.getPreco());
                                System.out.println("************************************************************");
                            }

                        });
                        System.out.println("Saldo Empresa: " + usuarioLogado.getEmpresa().getSaldo());
                        System.out.println("************************************************************");

                        executar(dadosExcutar);
                    }
                    case 0: {
                        executar(dadosExcutar);

                    }
                }

            } else {
                System.out.println("1 - Relizar Compras");
                System.out.println("2 - Ver Compras");
                System.out.println("0 - Deslogar");
                Integer escolha = sc.nextInt();
                switch (escolha) {
                    case 1: {
                        System.out.println("Para realizar uma compra, escolha a empresa onde deseja comprar: ");
                        empresas.forEach(empresa -> {
                            System.out.println(empresa.getId() + " - " + empresa.getNome());
                        });
                        Integer escolhaEmpresa = sc.nextInt();
                        Integer escolhaProduto = -1;
                        do {
                            System.out.println("Escolha os seus produtos: ");
                            produtos.forEach(produto -> {
                                if (produto.getEmpresa().getId().equals(escolhaEmpresa)) {
                                    System.out.println(produto.getId() + " - " + produto.getNome());
                                }
                            });
                            System.out.println("0 - Finalizar compra");

                            escolhaProduto = sc.nextInt();
                            for (Produto produtoSearch : produtos) {
                                if (produtoSearch.getId().equals(escolhaProduto))
                                    carrinho.add(produtoSearch);
                            }
                        } while (escolhaProduto != 0);

                        System.out.println("************************************************************");
                        System.out.println("Resumo da compra: ");
                        carrinho.forEach(produto -> {
                            if (produto.getEmpresa().getId().equals(escolhaEmpresa)) {
                                System.out.println(produto.getId() + " - " + produto.getNome() + "    R$" + produto.getPreco());
                            }
                        });

                        Empresa empresaEscolhida = empresas.stream().filter(empresa -> empresa.getId().equals(escolhaEmpresa))
                                .toList()
                                .get(0);

                        Cliente clienteLogado = clientes.stream()
                                .filter(item -> item.getUsername().equals(usuarioLogado.getUsername()))
                                .toList().get(0);

                        Venda venda = VendaService.criarVenda(carrinho, empresaEscolhida, clienteLogado, vendas);
                        System.out.println("Total: R$" + venda.getValor());
                        System.out.println("************************************************************");
                        carrinho.clear();
                        executar(dadosExcutar);
                    }
                    case 2: {
                        System.out.println();
                        System.out.println("************************************************************");
                        System.out.println("COMPRAS EFETUADAS");
                        vendas.forEach(venda -> {
                            if (venda.getCliente().getUsername().equals(usuarioLogado.getUsername())) {
                                System.out.println("************************************************************");
                                System.out.println("Compra de código: " + venda.getCodigo() + " na empresa "
                                        + venda.getEmpresa().getNome() + ": ");
                                venda.getItens().forEach(item -> {
                                    System.out.println(item.getId() + " - " + item.getNome() + "    R$" + item.getPreco());
                                });
                                System.out.println("Total: R$" + venda.getValor());
                                System.out.println("************************************************************");
                            }

                        });

                        executar(dadosExcutar);
                    }
                    case 0: {
                        executar(dadosExcutar);

                    }
                }
            }

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void listarVendasAdmin(Scanner sc, DadosExcutar dadosExcutar) throws Exception {
        var produtos = dadosExcutar.produtos();
        var vendas = dadosExcutar.vendas();
        var empresas = dadosExcutar.empresas();

        System.out.println("1 - Listar vendas");
        System.out.println("2 - Ver produtos");
        System.out.println("0 - Deslogar");

        Integer escolha = sc.nextInt();
        switch (escolha) {
            case 1: {
                System.out.println();
                System.out.println("************************************************************");
                System.out.println("VENDAS EFETUADAS");

                vendas.forEach(venda -> {
                    System.out.println("************************************************************");
                    System.out.println("Venda de código: "
                            + venda.getCodigo() + " no CPF "
                            + venda.getCliente().getCpf() + ": ");

                    venda.getItens().forEach(item -> {
                        System.out.println(item.getId() + " - " + item.getNome() + "    R$" + item.getPreco());
                    });
                    System.out.println("Total Venda: R$" + venda.getValor());
                    System.out.println("Total Taxa a ser paga: R$" + venda.getComissaoSistema());
                    System.out.println("Total Líquido  para empresa: "
                            + (venda.getValor() - venda.getComissaoSistema()));
                    System.out.println("************************************************************");
                });

                empresas.forEach(empresa -> {
                    System.out.println("Saldo Empresa: " + empresa.getSaldo());
                    System.out.println("************************************************************");
                });

                executar(dadosExcutar);
            }
            case 2: {
                System.out.println();
                System.out.println("************************************************************");
                System.out.println("MEUS PRODUTOS");

                empresas.forEach(empresa -> {
                    System.out.println("************************************************************");
                    System.out.println("Empresa: " + empresa.getNome());

                    produtos.forEach(produto -> {
                        if (produto.getEmpresa().getId().equals(empresa.getId())) {
                            System.out.println("************************************************************");
                            System.out.println("Código: " + produto.getId());
                            System.out.println("Produto: " + produto.getNome());
                            System.out.println("Quantidade em estoque: " + produto.getQuantidade());
                            System.out.println("Valor: R$" + produto.getPreco());
                            System.out.println("************************************************************");
                        }
                    });
                });

                executar(dadosExcutar);
            }
            case 0: {
                executar(dadosExcutar);
            }
        }
    }
}
