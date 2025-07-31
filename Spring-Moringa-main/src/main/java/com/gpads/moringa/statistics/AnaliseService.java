package com.gpads.moringa.statistics;

import com.gpads.moringa.entities.*;
import com.gpads.moringa.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Map<String, Object>> unificarDados() {
        List<SensorDeSolo> dadosSolo = sensorDeSoloRepository.findAll();
        List<SensorDePh> dadosPh = sensorDePhRepository.findAll();
        List<Pluviometro> dadosPluvio = pluviometroRepository.findAll();
        DadosEstacao dadoEstacaoMaisRecente = buscarUltimoDadoEstacao();

        Map<String, Map<String, Object>> mapa = new HashMap<>();

        for (SensorDeSolo solo : dadosSolo) {
            String chave = solo.getData() + " " + solo.getHora();
            Map<String, Object> dado = mapa.getOrDefault(chave, new HashMap<>());

            dado.put("data", solo.getData());
            dado.put("hora", solo.getHora());

            dado.put("temperatura", parseDouble(solo.getTemperatura()));
            dado.put("umidade", parseDouble(solo.getUmidade()));
            dado.put("ph", parseDouble(solo.getPh()));
            dado.put("condutividade", parseDouble(solo.getCondutividade()));

            mapa.put(chave, dado);
        }

        for (SensorDePh ph : dadosPh) {
            String chave = ph.getData() + " " + ph.getHora();
            Map<String, Object> dado = mapa.getOrDefault(chave, new HashMap<>());

            dado.put("data", ph.getData());
            dado.put("hora", ph.getHora());
            dado.put("ph", parseDouble(ph.getPh()));

            mapa.put(chave, dado);
        }

        for (Pluviometro pluvio : dadosPluvio) {
            String chave = pluvio.getData() + " " + pluvio.getHora();
            Map<String, Object> dado = mapa.getOrDefault(chave, new HashMap<>());

            dado.put("data", pluvio.getData());
            dado.put("hora", pluvio.getHora());
            dado.put("pluviometria", parseDouble(pluvio.getMedidaDeChuvaCalculado()));

            mapa.put(chave, dado);
        }

        for (Map<String, Object> dado : mapa.values()) {
            String chave = "";
            if (dadoEstacaoMaisRecente != null) {
                chave = dadoEstacaoMaisRecente.getData() + " " + dadoEstacaoMaisRecente.getHora();
                dado.put("data", dadoEstacaoMaisRecente.getData());
                dado.put("hora", dadoEstacaoMaisRecente.getHora());
                dado.put("temperaturaEstacao", dadoEstacaoMaisRecente.getTemperatura());
                dado.put("umidadeEstacao", dadoEstacaoMaisRecente.getUmidade());
                dado.put("pressao", dadoEstacaoMaisRecente.getPressao());
                dado.put("luminosidade", dadoEstacaoMaisRecente.getLuz());
                dado.put("co2", dadoEstacaoMaisRecente.getGas());
                dado.put("qualidadeAr", dadoEstacaoMaisRecente.getAr());
                dado.put("velocidadeVento", dadoEstacaoMaisRecente.getVento());
                dado.put("voltagem", dadoEstacaoMaisRecente.getVolt());
                dado.put("rpm", dadoEstacaoMaisRecente.getRpm());

            }
            mapa.put(chave, dado);

            // Preencher valores padrão para evitar null
            /*
             * dado.putIfAbsent("pluviometria", 0.0);
             * dado.putIfAbsent("rpm", 0.0);
             * dado.putIfAbsent("velocidadeVento", 0.0);
             */
        }

        return new ArrayList<>(mapa.values());
    }

    private DadosEstacao buscarUltimoDadoEstacao() {
        List<DadosEstacao> todos = dadosEstacaoRepository.findAll();
        if (todos.isEmpty())
            return null;
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