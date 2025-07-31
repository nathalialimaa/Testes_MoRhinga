package com.gpads.moringa.statisticsTest;

import com.gpads.moringa.dto.DadoSensorUnificado;
import com.gpads.moringa.entities.*;
import com.gpads.moringa.repositories.*;

import com.gpads.moringa.statistics.AnaliseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;


public class AnaliseServiceTest {

    @Mock
    private SensorDeSoloRepositoryMongoDB sensorDeSoloRepository;

    @Mock
    private SensorDePhRepositoryMongoDB sensorDePhRepository;

    @Mock
    private PluviometroRepositoryMongoDB pluviometroRepository;

    @Mock
    private DadosEstacaoRepositoryMongoDB dadosEstacaoRepository;

    @InjectMocks
    private AnaliseService analiseService;

    @BeforeEach
    public void setup() {
        sensorDeSoloRepository = mock(SensorDeSoloRepositoryMongoDB.class);
        sensorDePhRepository = mock(SensorDePhRepositoryMongoDB.class);
        pluviometroRepository = mock(PluviometroRepositoryMongoDB.class);
        dadosEstacaoRepository = mock(DadosEstacaoRepositoryMongoDB.class);

        analiseService = new AnaliseService(sensorDeSoloRepository, sensorDePhRepository, pluviometroRepository, dadosEstacaoRepository);
    }

    @Test
    public void testUnificarDados() {
        
        // Mocks
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

        when(sensorDeSoloRepository.findAll()).thenReturn(List.of(solo));
        when(sensorDePhRepository.findAll()).thenReturn(List.of(sensorPh));
        when(pluviometroRepository.findAll()).thenReturn(List.of(pluvio));
        when(dadosEstacaoRepository.findAll()).thenReturn(List.of(estacao));

        // Execução
        List<DadoSensorUnificado> resultado = analiseService.unificarDados();

        // Verificação
        assertEquals(1, resultado.size());
        DadoSensorUnificado dado = resultado.get(0);

        assertEquals("01/06/2025", dado.getData());
        assertEquals("12:00", dado.getHora());
        assertEquals(27.0, dado.getTemperatura()); // DadosEstacao sobrescreve
        assertEquals(85.0, dado.getUmidade());     // DadosEstacao sobrescreve
        assertEquals(6.3, dado.getPh());           // SensorDePh sobrescreve
        assertEquals(12.5, dado.getPluviometria());
        assertEquals(1013.0, dado.getPressao());
        assertEquals(300.0, dado.getLuminosidade());
        assertEquals(400.0, dado.getCo2());
        assertEquals(90.0, dado.getQualidadeAr());
        assertEquals(12.0, dado.getVelocidadeVento());
        assertEquals(5.0, dado.getVoltagem());
        assertEquals(1500.0, dado.getRpm());
    }
}
