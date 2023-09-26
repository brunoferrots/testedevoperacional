package services;

import models.Cliente;

import java.util.List;

public class ClienteService {

    public List<Cliente> listarClients() {
        Cliente cliente = new Cliente("07221134049", "Allan da Silva", "cliente", 20);
        Cliente cliente2 = new Cliente("72840700050", "Samuel da Silva", "cliente2", 24);
        return List.of(cliente, cliente2);
    }
}
