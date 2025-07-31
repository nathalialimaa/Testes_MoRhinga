package com.gpads.moringa.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

//@Entity
@Document("placaOutPutMoringa")
public class PlacaOutPut {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @SuppressWarnings("unused")
    private ObjectId id;
    private Date dataHora;
    private float temperatura;
    private float umidade;
    private float pressao;
    private float luminosidade;
    private float cO2;
    private float qualidadeDoAr;
    private float velocidadeDoVento;
    private float voltagem;
    private float rpm;
    private float ph;
    private float pluviometria;

    
    private Placa placa;

    public PlacaOutPut(){}

    
    
    public PlacaOutPut(Date dataHora, float temperatura, float umidade) {
        this.dataHora = dataHora;
        this.temperatura = temperatura;
        this.umidade = umidade;
    }

    public PlacaOutPut(Date dataHora, float temperatura, float umidade, float pressao, float luminosidade, float cO2,
            float qualidadeDoAr, float velocidadeDoVento, float voltagem, float rpm, Placa placa, float ph, float pluviometria) {
        this.dataHora = dataHora;
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.pressao = pressao;
        this.luminosidade = luminosidade;
        this.cO2 = cO2;
        this.qualidadeDoAr = qualidadeDoAr;
        this.velocidadeDoVento = velocidadeDoVento;
        this.voltagem = voltagem;
        this.rpm = rpm;
        this.placa = placa;
        this.ph = ph;
        this.pluviometria = pluviometria;
    }

    public PlacaOutPut(float temperatura) {
        this.temperatura = temperatura;
    }

    
    public Date getDataHora() {
        return dataHora;
    }
    public String getDataHoraString(){
        return new SimpleDateFormat("HH:mm dd/MM/yyyy").format(this.dataHora);
    }
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }
    public float getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }
    public float getUmidade() {
        return umidade;
    }
    public void setUmidade(float umidade) {
        this.umidade = umidade;
    }
    public float getPressao() {
        return pressao;
    }
    public void setPressao(float pressao) {
        this.pressao = pressao;
    }
    public float getLuminosidade() {
        return luminosidade;
    }
    public void setLuminosidade(float luminosidade) {
        this.luminosidade = luminosidade;
    }
    public float getcO2() {
        return cO2;
    }
    public void setcO2(float cO2) {
        this.cO2 = cO2;
    }
    public float getQualidadeDoAr() {
        return qualidadeDoAr;
    }
    public void setQualidadeDoAr(float qualidadeDoAr) {
        this.qualidadeDoAr = qualidadeDoAr;
    }
    public float getVelocidadeDoVento() {
        return velocidadeDoVento;
    }
    public void setVelocidadeDoVento(float velocidadeDoVento) {
        this.velocidadeDoVento = velocidadeDoVento;
    }
    public float getVoltagem() {
        return voltagem;
    }
    public void setVoltagem(float voltagem) {
        this.voltagem = voltagem;
    }
    public float getRpm() {
        return rpm;
    }
    public void setRpm(float rpm) {
        this.rpm = rpm;
    }
    public Placa getPlaca() {
        return placa;
    }
    public void setPlaca(Placa placa) {
        this.placa = placa;
    }
    public float getPh() {
        return ph;
    }
    public void setPh(float ph) {
        this.ph = ph;
    }
    public float getPluviometria() {
        return pluviometria;
    }
    public void setPluviometria(float pluviometria) {
        this.pluviometria = pluviometria;
    }
    
}
