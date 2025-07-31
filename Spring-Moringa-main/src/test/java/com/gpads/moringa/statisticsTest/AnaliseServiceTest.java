package com.gpads.moringa.statisticsTest;

import com.gpads.moringa.entities.*;
import com.gpads.moringa.repositories.*;
import com.gpads.moringa.statistics.AnaliseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnaliseServiceTest {

    private SensorDeSoloRepositoryMongoDB sensorDeSoloRepository;
    private SensorDePhRepositoryMongoDB sensorDePhRepository;
    private PluviometroRepositoryMongoDB pluviometroRepository;
    private DadosEstacaoRepositoryMongoDB dadosEstacaoRepository;

    private AnaliseService analiseService;

    @BeforeEach
    public void setup() {
        sensorDeSoloRepository = mock(SensorDeSoloRepositoryMongoDB.class);
        sensorDePhRepository = mock(SensorDePhRepositoryMongoDB.class);
        pluviometroRepository = mock(PluviometroRepositoryMongoDB.class);
        dadosEstacaoRepository = mock(DadosEstacaoRepositoryMongoDB.class);

        analiseService = new AnaliseService(
                sensorDeSoloRepository,
                sensorDePhRepository,
                pluviometroRepository,
                dadosEstacaoRepository
        );
    }

    @Test
    public void testUnificarDados() {
        // Mocks de entidades
        SensorDeSolo solo = new SensorDeSolo();
        solo.setData("01/06/2025");
        solo.setHora("12:00");
        solo.setTemperatura("25,5");
        solo.setUmidade("80");
        solo.setPh("6.2");

        SensorDePh sensorPh = new SensorDePh();
        sensorPh.setData("01/06/2025");
        sensorPh.setHora("12:00");
        sensorPh.setPh("6.3");

        Pluviometro pluvio = new Pluviometro();
        pluvio.setData("01/06/2025");
        pluvio.setHora("12:00");
        pluvio.setMedidaDeChuvaCalculado("12,5");

        DadosEstacao estacao = new DadosEstacao();
        estacao.setTemperatura(27.0);
        estacao.setUmidade(85.0);
        estacao.setPressao(1013.0);
        estacao.setLuz(300.0);
        estacao.setGas(400.0);
        estacao.setAr(90.0);
        estacao.setVento(12.0);
        estacao.setVolt(5.0);
        estacao.setRpm(1500.0);

        // Simulações
        when(sensorDeSoloRepository.findAll()).thenReturn(List.of(solo));
        when(sensorDePhRepository.findAll()).thenReturn(List.of(sensorPh));
        when(pluviometroRepository.findAll()).thenReturn(List.of(pluvio));
        when(dadosEstacaoRepository.findAll()).thenReturn(List.of(estacao));

        // Execução
        List<Map<String, Object>> resultado = analiseService.unificarDados();

        // Verificação
        assertEquals(1, resultado.size());
        Map<String, Object> dado = resultado.get(0);

        assertEquals("01/06/2025", dado.get("data"));
        assertEquals("12:00", dado.get("hora"));

        // Dados sobrescritos da estação
        assertEquals(27.0, (Double) dado.get("temperatura"));
        assertEquals(85.0, (Double) dado.get("umidade"));
        assertEquals(6.3, (Double) dado.get("ph"));
        assertEquals(12.5, (Double) dado.get("pluviometria"));
        assertEquals(1013.0, (Double) dado.get("pressao"));
        assertEquals(300.0, (Double) dado.get("luminosidade"));
        assertEquals(400.0, (Double) dado.get("co2"));
        assertEquals(90.0, (Double) dado.get("qualidadeAr"));
        assertEquals(12.0, (Double) dado.get("velocidadeVento"));
        assertEquals(5.0, (Double) dado.get("voltagem"));
        assertEquals(1500.0, (Double) dado.get("rpm"));
    }
}
