package services;

import models.Usuario;

import java.util.List;

public class UsuarioService {
    private EmpresaService empresaService = new EmpresaService();
    private ClienteService clienteService = new ClienteService();

    public List<Usuario> listarUsuarios() {
        var empresas = empresaService.listarEmpresas();
        var empresa = empresas.get(0);
        var empresa2 = empresas.get(1);
        var empresa3 = empresas.get(2);

        var clientes = clienteService.listarClients();
        var cliente = clientes.get(0);
        var cliente2 = clientes.get(1);

        Usuario usuario1 = new Usuario("admin", "1234", null, null);
        Usuario usuario2 = new Usuario("empresa", "1234", null, empresa);
        Usuario usuario3 = new Usuario("cliente", "1234", cliente, null);
        Usuario usuario4 = new Usuario("cliente2", "1234", cliente2, null);
        Usuario usuario5 = new Usuario("empresa2", "1234", null, empresa2);
        Usuario usuario6 = new Usuario("empresa3", "1234", null, empresa3);

        return List.of(usuario1, usuario2, usuario3, usuario4, usuario5, usuario6);
    }

}
