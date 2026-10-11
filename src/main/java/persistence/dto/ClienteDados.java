package persistence.dto;

import model.Cliente;
import model.Cnpj;

public class ClienteDados {
    // Atributos //
    private String razaoSocial;
    private Cnpj cnpj;
    private String inscricaoEstadual;
    private String endereco;
    private String email;
    private String celular;
    
    // Métodos de acesso //

    public String getCelular() {
        return celular;
    }
    
    public Cnpj getCnpj() {
        return cnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    // Construtor //

    public ClienteDados (
        String razaoSocial,
        Cnpj cnpj,
        String inscricaoEstadual,
        String endereco,
        String email,
        String celular
    ) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.endereco = endereco;
        this.email = email;
        this.celular = celular;
    }

    public ClienteDados () {}

    public ClienteDados (Cliente cliente) {
        this (
            cliente.getRazaoSocial(),
            cliente.getCnpj(),
            cliente.getInscricaoEstadual(),
            cliente.getEndereco(),
            cliente.getEmail(),
            cliente.getCelular()
        );
    }

    // Conversão para cliente //

    public Cliente paraCliente() {
        return new Cliente(
            razaoSocial,
            cnpj.toString(),
            inscricaoEstadual,
            endereco,
            email,
            celular
        );
    }
}

