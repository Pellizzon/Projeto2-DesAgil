package br.edu.insper.al.matheusp1.projeto2.ui.login;

public class Dados {

    private String nome;
    private String valor;

    public Dados(String nome, String valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public String getMais() {
        return null;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
