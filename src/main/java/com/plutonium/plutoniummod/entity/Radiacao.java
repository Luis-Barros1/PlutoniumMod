package com.plutonium.plutoniummod.entity;

public class Radiacao {
    private double velocidade;
    private int intensidade;

    public Radiacao(double velocidade, int intensidade) {
        this.velocidade = velocidade;
        this.intensidade = intensidade;
    }

    public Radiacao(double velocidade) {
        this.velocidade = velocidade;
        this.intensidade = 1;
    }

    public Radiacao() {
        this.intensidade = 1;
        this.velocidade = 1.0;
    }

    public int getIntensidade() {
        return intensidade;
    }

    public void setIntensidade(int intensidade) {
        this.intensidade = intensidade;
    }

    public double getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(double velocidade) {
        this.velocidade = velocidade;
    }
}
