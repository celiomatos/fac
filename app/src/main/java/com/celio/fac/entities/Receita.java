package com.celio.fac.entities;

public class Receita {

    private String idreceita;
    private Massa massa;
    private Ingrediente ingrediente;
    private String porcentagem;

    public String getIdreceita() {
        return idreceita;
    }

    public void setIdreceita(String idreceita) {
        this.idreceita = idreceita;
    }

    public Massa getMassa() {
        return massa;
    }

    public void setMassa(Massa massa) {
        this.massa = massa;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(String porcentagem) {
        this.porcentagem = porcentagem;
    }
}
