package com.eventos.bean;

import java.io.Serializable;

/**
 * Created by Victor on 05/11/2017.
 */

public class EventoBean implements Serializable{
    private long id;
    private String nome;
    private String dataHoraInicio;
    private String dataHoraFim;
    private String descricao;
    private String endereco;
    private int lotacaoMax;
    private double longitude;
    private double latitude;
    private Character status;
    private String emailCriador;
    private float valor;
    private String cidade;



    public EventoBean() {}

    public EventoBean(long id, String nome, String dataHoraInicio, String dataHoraFim, String descricao, String endereco, int lotacaoMax, float valor ){
        this.id = id;
        this.nome = nome;
        this.dataHoraFim = dataHoraFim;
        this.dataHoraInicio = dataHoraInicio;
        this.descricao = descricao;
        this.endereco = endereco;
        this.lotacaoMax = lotacaoMax;
        this.valor = valor;
    }

    public EventoBean(long id, String nome, String dataHoraInicio, String dataHoraFim, String descricao, String endereco, int lotacaoMax, double longitude, double latitude, Character status, String emailCriador, float valor, String cidade) {
        this.id = id;
        this.nome = nome;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.descricao = descricao;
        this.endereco = endereco;
        this.lotacaoMax = lotacaoMax;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.emailCriador = emailCriador;
        this.valor = valor;
        this.cidade = cidade;
    }

    public EventoBean(String nome, String dataHoraInicio, String dataHoraFim, String descricao, String endereco, int lotacaoMax, double longitude, double latitude, String emailCriador, float valor) {
        this.nome = nome;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.descricao = descricao;
        this.endereco = endereco;
        this.lotacaoMax = lotacaoMax;
        this.longitude = longitude;
        this.latitude = latitude;
        this.emailCriador = emailCriador;
        this.valor = valor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(String dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public String getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(String dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getLotacaoMax() {
        return lotacaoMax;
    }

    public void setLotacaoMax(int lotacaoMax) {
        this.lotacaoMax = lotacaoMax;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getEmailCriador() {
        return emailCriador;
    }

    public void setEmailCriador(String emailCriador) {
        this.emailCriador = emailCriador;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return "EventoBean{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataHoraInicio='" + dataHoraInicio + '\'' +
                ", dataHoraFim='" + dataHoraFim + '\'' +
                ", descricao='" + descricao + '\'' +
                ", endereco='" + endereco + '\'' +
                ", lotacaoMax=" + lotacaoMax +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", status=" + status +
                ", emailCriador='" + emailCriador + '\'' +
                ", valor=" + valor +
                '}';
    }
}
