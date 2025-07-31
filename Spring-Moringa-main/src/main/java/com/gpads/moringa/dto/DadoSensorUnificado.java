/**
 * Classe DadoSensorUnificado
 *
 * Esta classe representa um Data Transfer Object (DTO) utilizado para agrupar e transportar
 * de forma unificada os dados coletados por diferentes sensores em uma estação de monitoramento
 * (como temperatura, umidade, pressão, luminosidade, etc).
 *
 * Ela facilita a transferência de dados entre camadas da aplicação (por exemplo, entre a camada
 * de serviço e a camada de API) e também pode ser usada para serialização/deserialização JSON.
 *
 * A anotação @JsonInclude(JsonInclude.Include.NON_NULL) garante que, ao converter o objeto para JSON,
 * apenas os campos com valores diferentes de null serão incluídos na resposta — o que deixa o payload
 * mais limpo e reduz o tráfego de dados.
 *
 * A classe possui:
 * - Atributos que representam medições de diversos sensores.
 * - Construtor padrão e um construtor completo.
 * - Métodos getters e setters para todos os atributos.
 * - Um método utilitário para converter uma string em Double especificamente para o campo "pluviometria".
 *
 * Exemplos de sensores representados:
 * - Estação climática (temperatura, umidade, vento, gás, etc.)
 * - Sensor de solo ou pH
 * - Pluviômetro
 */

package com.gpads.moringa.dto; // Ou um pacote mais apropriado para DTOs/modelos

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DadoSensorUnificado {

    private String data;
    private String hora;

    private Double temperatura; // DadosEstacao
    private Double umidade; // DadosEstacao
    private Double pressao; // DadosEstacao
    private Double luminosidade; // DadosEstacao (luz)
    private Double co2; // DadosEstacao (gas)
    private Double qualidadeAr; // DadosEstacao (ar)
    private Double velocidadeVento; // DadosEstacao (vento)
    private Double voltagem; // DadosEstacao (volt)
    private Double rpm; // DadosEstacao (rpm)

    private Double ph; // SensorDePh ou SensorDeSolo
    private Double pluviometria; // Pluviometro

    // Construtor padrão
    public DadoSensorUnificado() {
    }

    // Construtor com todos os atributos
    public DadoSensorUnificado(String data, String hora, Double temperatura, Double umidade, Double pressao,
            Double luminosidade, Double co2, Double qualidadeAr, Double velocidadeVento,
            Double voltagem, Double rpm, Double ph, Double pluviometria) {
        this.data = data;
        this.hora = hora;
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.pressao = pressao;
        this.luminosidade = luminosidade;
        this.co2 = co2;
        this.qualidadeAr = qualidadeAr;
        this.velocidadeVento = velocidadeVento;
        this.voltagem = voltagem;
        this.rpm = rpm;
        this.ph = ph;
        this.pluviometria = pluviometria;
    }

    // Getters e Setters

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
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

    public Double getLuminosidade() {
        return luminosidade;
    }

    public void setLuminosidade(Double luminosidade) {
        this.luminosidade = luminosidade;
    }

    public Double getCo2() {
        return co2;
    }

    public void setCo2(Double co2) {
        this.co2 = co2;
    }

    public Double getQualidadeAr() {
        return qualidadeAr;
    }

    public void setQualidadeAr(Double qualidadeAr) {
        this.qualidadeAr = qualidadeAr;
    }

    public Double getVelocidadeVento() {
        return velocidadeVento;
    }

    public void setVelocidadeVento(Double velocidadeVento) {
        this.velocidadeVento = velocidadeVento;
    }

    public Double getVoltagem() {
        return voltagem;
    }

    public void setVoltagem(Double voltagem) {
        this.voltagem = voltagem;
    }

    public Double getRpm() {
        return rpm;
    }

    public void setRpm(Double rpm) {
        this.rpm = rpm;
    }

    public Double getPh() {
        return ph;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public Double getPluviometria() {
        return pluviometria;
    }

    public void setPluviometria(Double pluviometria) {
        this.pluviometria = pluviometria;
    }

    public void setPluviometriaFromString(String valor) {
        try {
            if (valor != null) {
                this.pluviometria = Double.parseDouble(valor.replace(",", "."));
            } else {
                this.pluviometria = null;
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter pluviometria: " + valor);
            this.pluviometria = null;
        }
    }
}