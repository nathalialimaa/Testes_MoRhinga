/**
 *
 * Esta classe é um serviço Spring (`@Service`) responsável por realizar a unificação e análise estatística
 * dos dados provenientes de diferentes sensores da aplicação: solo, pH, pluviômetro e estação meteorológica.
 *
 * Funcionalidades principais:
 *
 * 1. Unificação de Dados (método `unificarDados`):
 *   - Coleta todos os registros dos sensores (via repositórios MongoDB).
 *   - Agrupa e une os dados com base em data + hora, combinando informações de sensores diferentes em um único objeto DTO (`DadoSensorUnificado`).
 *   - Preenche os campos do DTO com os valores mais recentes da estação meteorológica.
 *   - Substitui valores nulos por `0.0` quando apropriado para evitar falhas ou inconsistências na análise.
 *   - Retorna uma lista consolidada de medições para consumo por APIs ou análise.
 *
 * 2. Estatísticas básicas:
 *   - `media(List<Float>)`: calcula a média de uma lista de valores float.
 *   - `moda(List<Double>)`: retorna os valores mais frequentes (moda) de uma lista.
 *   - `q1(List<Double>)` e `q3(List<Double>)`: retornam o primeiro e terceiro quartis.
 *
 * Detalhes técnicos:
 * - Os dados são buscados dos repositórios MongoDB específicos para cada sensor.
 * - A estrutura `Map<String, DadoSensorUnificado>` é usada para agrupar os dados por timestamp, onde a chave é uma combinação de `data + hora`.
 * - O método `buscarUltimoDadoEstacao()` retorna o dado mais recente da estação, utilizado para preencher os campos climáticos (temperatura, umidade, etc).
 * - Conversões de string para número são tratadas com o método utilitário `parseDouble()`, que também lida com vírgulas.
 *
 * Público-alvo:
 * - Esta classe é usada principalmente pelo `MainController` (endpoint `/api/analise-completa`) para fornecer uma visualização integrada dos dados.
 */
package com.gpads.moringa.statistics;

import com.gpads.moringa.dto.DadoSensorUnificado;
import com.gpads.moringa.entities.*;
import com.gpads.moringa.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@Service
public class AnaliseService {

    @Autowired
    private SensorDeSoloRepositoryMongoDB sensorDeSoloRepository;

    @Autowired
    private SensorDePhRepositoryMongoDB sensorDePhRepository;

    @Autowired
    private PluviometroRepositoryMongoDB pluviometroRepository;

    @Autowired
    private DadosEstacaoRepositoryMongoDB dadosEstacaoRepository;

    public AnaliseService(
            SensorDeSoloRepositoryMongoDB sensorDeSoloRepository,
            SensorDePhRepositoryMongoDB sensorDePhRepository,
            PluviometroRepositoryMongoDB pluviometroRepository,
            DadosEstacaoRepositoryMongoDB dadosEstacaoRepository) {
        this.sensorDeSoloRepository = sensorDeSoloRepository;
        this.sensorDePhRepository = sensorDePhRepository;
        this.pluviometroRepository = pluviometroRepository;
        this.dadosEstacaoRepository = dadosEstacaoRepository;
    }

    public List<DadoSensorUnificado> unificarDados() {
        List<SensorDeSolo> dadosSolo = sensorDeSoloRepository.findAll();
        List<SensorDePh> dadosPh = sensorDePhRepository.findAll();
        List<Pluviometro> dadosPluvio = pluviometroRepository.findAll();
        DadosEstacao dadoEstacaoMaisRecente = buscarUltimoDadoEstacao();
        Map<String, DadoSensorUnificado> mapa = new HashMap<>();

        // *** Debug para checar os dados do Pluviometro ***
        System.out.println("### Dados de Pluviometro carregados do banco ###");
        for (Pluviometro p : dadosPluvio) {
            System.out.println("Data: " + p.getData()
                    + ", Hora: " + p.getHora()
                    + ", Medida de chuva (calculado): " + p.getMedidaDeChuvaCalculado());
        }
        System.out.println("### Fim dos dados de Pluviometro ###");

        // Unir SensorDeSolo
        for (SensorDeSolo solo : dadosSolo) {
            String chave = solo.getData() + " " + solo.getHora();
            DadoSensorUnificado dto = mapa.getOrDefault(chave, new DadoSensorUnificado());

            dto.setData(solo.getData());
            dto.setHora(solo.getHora());

            try {
                dto.setTemperatura(parseDouble(solo.getTemperatura()));
                dto.setUmidade(parseDouble(solo.getUmidade()));
                dto.setPh(parseDouble(solo.getPh()));

                // dto.setCondutividade(parseDouble(solo.getCondutividade()));
            } catch (Exception e) {
                // log ou tratamento de erro
            }

            mapa.put(chave, dto);
        }

        // Unir SensorDePh
        for (SensorDePh ph : dadosPh) {
            String chave = ph.getData() + " " + ph.getHora();
            DadoSensorUnificado dto = mapa.getOrDefault(chave, new DadoSensorUnificado());

            dto.setData(ph.getData());
            dto.setHora(ph.getHora());

            try {
                dto.setPh(parseDouble(ph.getPh()));
            } catch (Exception e) {
                // log ou tratamento de erro
            }

            mapa.put(chave, dto);
        }

        // Unir Pluviômetro
        for (Pluviometro pluvio : dadosPluvio) {
            String chave = pluvio.getData() + " " + pluvio.getHora();
            DadoSensorUnificado dto = mapa.getOrDefault(chave, new DadoSensorUnificado());

            dto.setData(pluvio.getData());
            dto.setHora(pluvio.getHora());

            dto.setPluviometriaFromString(pluvio.getMedidaDeChuvaCalculado());

            mapa.put(chave, dto);
        }

        // Preencher os campos da DadosEstacao no DTO
        for (DadoSensorUnificado dto : mapa.values()) {
            if (dadoEstacaoMaisRecente != null) {
                dto.setTemperatura(dadoEstacaoMaisRecente.getTemperatura()); // ainda veio
                dto.setUmidade(dadoEstacaoMaisRecente.getUmidade()); // ainda veio
                dto.setPressao(dadoEstacaoMaisRecente.getPressao());
                dto.setLuminosidade(dadoEstacaoMaisRecente.getLuz());
                dto.setCo2(dadoEstacaoMaisRecente.getGas());
                dto.setQualidadeAr(dadoEstacaoMaisRecente.getAr());
                dto.setVelocidadeVento(dadoEstacaoMaisRecente.getVento());
                dto.setVoltagem(dadoEstacaoMaisRecente.getVolt());
                dto.setRpm(dadoEstacaoMaisRecente.getRpm());
            }

            if (dto.getPluviometria() == null) {
                dto.setPluviometria(0.0);
            }
            if(dto.getRpm() == null){
                dto.setRpm(0.0);
            }
            if(dto.getVelocidadeVento() == null){
                dto.setVelocidadeVento(0.0);
            }
        }

        return new ArrayList<DadoSensorUnificado>(mapa.values());

    }

    private DadosEstacao buscarUltimoDadoEstacao() {
        List<DadosEstacao> todos = dadosEstacaoRepository.findAll();
        if (todos.isEmpty())
            return null;

        // Pode ordenar se quiser garantir o mais recente
        // Aqui pegamos o último

        return todos.get(todos.size() - 1);
    }

    private Double parseDouble(String valor) {
        if (valor == null || valor.trim().isEmpty())
            return null;
        return Double.parseDouble(valor.replace(",", "."));
    }

    public Double media(List<Float> d) {
        if (d == null || d.isEmpty()) {
            return null;
        }
        float soma = d.stream().reduce(0.0f, Float::sum);
        return (double) soma / d.size();
    }

    public List<Double> moda(List<Double> d) {
        if (d == null || d.isEmpty()) {
            return null;
        }

        Map<Double, Integer> frequencia = new HashMap<>();
        int maxFrequencia = 0;

        for (Double valor : d) {
            int count = frequencia.getOrDefault(valor, 0) + 1;
            frequencia.put(valor, count);
            maxFrequencia = Math.max(maxFrequencia, count);
        }

        List<Double> modas = new ArrayList<>();
        for (Map.Entry<Double, Integer> entry : frequencia.entrySet()) {
            if (entry.getValue() == maxFrequencia) {
                modas.add(entry.getKey());
            }
        }

        return modas;
    }

    public Double q1(List<Double> d) {
        if (d == null || d.isEmpty()) {
            return null;
        }

        List<Double> ordenada = new ArrayList<>(d);
        Collections.sort(ordenada);
        double posicao = 0.25 * (ordenada.size() + 1);

        if (posicao <= 1)
            return ordenada.get(0);
        if (posicao >= ordenada.size())
            return ordenada.get(ordenada.size() - 1);

        int indexInferior = (int) Math.floor(posicao) - 1;
        int indexSuperior = (int) Math.ceil(posicao) - 1;

        if (indexInferior == indexSuperior) {
            return ordenada.get(indexInferior);
        }

        double valorInferior = ordenada.get(indexInferior);
        double valorSuperior = ordenada.get(indexSuperior);
        return (valorInferior + valorSuperior) / 2;
    }

    public Double q3(List<Double> d) {
        if (d == null || d.isEmpty()) {
            return null;
        }

        List<Double> ordenada = new ArrayList<>(d);
        Collections.sort(ordenada);
        double posicao = 0.75 * (ordenada.size() + 1);

        if (posicao <= 1)
            return ordenada.get(0);
        if (posicao >= ordenada.size())
            return ordenada.get(ordenada.size() - 1);

        int indexInferior = (int) Math.floor(posicao) - 1;
        int indexSuperior = (int) Math.ceil(posicao) - 1;

        if (indexInferior == indexSuperior) {
            return ordenada.get(indexInferior);
        }

        double valorInferior = ordenada.get(indexInferior);
        double valorSuperior = ordenada.get(indexSuperior);
        return (valorInferior + valorSuperior) / 2;
    }

}