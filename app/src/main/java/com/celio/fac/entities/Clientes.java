package com.celio.fac.entities;

public class Clientes {
    private String idcliente;
    private String nomeCliente;
    private String segmento;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String telefone;
    private String celular;
    private String email;
    private String nomeContato;
    private String usaOutraFarinha;
    private String marcaFarinha;
    private Vendedor vendedor;


    public String getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(String idcliente) {
        this.idcliente = idcliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getUsaOutraFarinha() {
        return usaOutraFarinha;
    }

    public void setUsaOutraFarinha(String usaOutraFarinha) {
        this.usaOutraFarinha = usaOutraFarinha;
    }

    public String getMarcaFarinha() {
        return marcaFarinha;
    }

    public void setMarcaFarinha(String marcaFarinha) {
        this.marcaFarinha = marcaFarinha;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

}
