package services;

import java.util.List;
import java.util.Set;

import models.Empresa;

public class EmpresaService {
    public List<Empresa> listarEmpresas() {
        Empresa empresa = new Empresa(2, "SafeWay Padaria", "30021423000159", 0.15, 0.0);
		Empresa empresa2 = new Empresa(1, "Level Varejo", "53239160000154", 0.05, 0.0);
		Empresa empresa3 = new Empresa(3, "SafeWay Restaurante", "41361511000116", 0.20, 0.0);

        return List.of(empresa, empresa2, empresa3);
  }









}
