package com.gpads.moringa.entities;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.format.DateTimeFormatter;

@Document("DadosEstacao")
public class DadosEstacao {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @Field("Data")
    private String data;

    @Field("Hora")
    private String hora;
    @Field("Temperatura")
    private Double temperatura;
    @Field("Umidade")
    private Double umidade;
    @Field("Pressão")
    private Double pressao;
    @Field("Vento")
    private Double vento;
    @Field("Volt")
    private Double volt;
    @Field("Luz")
    private Double luz;
    @Field("Rpm")
    private Double rpm;
    @Field("Gás")
    private Double gas;
    @Field("Ar")
    private Double ar;

    DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    public DadosEstacao() {
    }

    public DadosEstacao(String data, String hora, Double temperatura, Double umidade, Double pressao, Double vento,
            Double volt, Double luz, Double rpm, Double gas, Double ar) {
        this.data = LocalDateTime.now().toLocalDate().format(formatoData);
        this.hora =LocalDateTime.now().toLocalTime().format(formatoHora);
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.pressao = pressao;
        this.vento = vento;
        this.volt = volt;
        this.luz = luz;
        this.rpm = rpm;
        this.gas = gas;
        this.ar = ar;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getUmidade() {
        return umidade;
    }

    public void setUmidade(Double umidade) {
        this.umidade = umidade;
    }

    public Double getPressao() {
        return pressao;
    }

    public void setPressao(Double pressao) {
        this.pressao = pressao;
    }

    public Double getVento() {
        return vento;
    }

    public void setVento(Double vento) {
        this.vento = vento;
    }

    public Double getVolt() {
        return volt;
    }

    public void setVolt(Double volt) {
        this.volt = volt;
    }

    public Double getLuz() {
        return luz;
    }

    public void setLuz(Double luz) {
        this.luz = luz;
    }

    public Double getRpm() {
        return rpm;
    }

    public void setRpm(Double rpm) {
        this.rpm = rpm;
    }

    public Double getGas() {
        return gas;
    }

    public void setGas(Double gas) {
        this.gas = gas;
    }

    public Double getAr() {
        return ar;
    }

    public void setAr(Double ar) {
        this.ar = ar;
    }
}
