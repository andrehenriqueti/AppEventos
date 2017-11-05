package com.eventos.bean;

/**
 * Created by ANDRE on 31/10/2017.
 */

public class UsuarioBean {
    private String nome;
    private String email;
    private String senha;
    private String dataNascimento;
    private char sexo;
    private String telefone;
    private double longitude;
    private double latitude;
    private char status;
    private String cidade;

    public UsuarioBean(){}

    public UsuarioBean(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public UsuarioBean(String nome, String email, String dataNascimento, char sexo, String telefone){
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.telefone = telefone;
    }
    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public char getSexo() {
        return sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getSenha() {
        return senha;
    }

    @Override
    public String toString() {
        return "UsuarioBean{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", sexo=" + sexo +
                ", telefone='" + telefone + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", status=" + status +
                ", cidade='" + cidade + '\'' +
                '}';
    }
}
