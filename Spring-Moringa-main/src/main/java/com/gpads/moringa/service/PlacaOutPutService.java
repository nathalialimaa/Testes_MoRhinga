package com.gpads.moringa.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.gpads.moringa.entities.Placa;
import com.gpads.moringa.entities.PlacaOutPut;
import com.gpads.moringa.repositories.PlacaOutPutRepositoryMongoDB;

@Service
public class PlacaOutPutService {

    
    private final PlacaOutPutRepositoryMongoDB placaOutPutRepositoryMongoDB;

    
    private final PlacaService placaService;

    public PlacaOutPutService(PlacaOutPutRepositoryMongoDB placaOutPutRepositoryMongoDB, PlacaService placaService) {
        this.placaOutPutRepositoryMongoDB = placaOutPutRepositoryMongoDB;
        this.placaService = placaService;
    }

    public void save(PlacaOutPut entity){
        placaOutPutRepositoryMongoDB.save(entity);
    }

    public PlacaOutPut findLatestByPlacaId(ObjectId id) {
        return placaOutPutRepositoryMongoDB.findLatestByPlacaId(id).get(0);
    }

    public List<PlacaOutPut> findByPlacaIdAndDateRange(ObjectId id, Date startDate, Date endDate) {
        return placaOutPutRepositoryMongoDB.findByPlacaIdAndDateRange(id, startDate, endDate);
    }
    public List<PlacaOutPut> findAll(){
        return placaOutPutRepositoryMongoDB.findAll();
    }

    public void gerarLeituras(Date startDate, Date endDate) {
        List<Placa> placas = placaService.findAll(); 
        Random random = new Random();

        for (Placa placa : placas) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
                float temperatura = gerarValorAleatorio(15.0f, 35.0f, random); 
                float umidade = gerarValorAleatorio(20.0f, 80.0f, random);     
                float pressao = gerarValorAleatorio(950.0f, 1050.0f, random);  
                float luminosidade = gerarValorAleatorio(0.0f, 1000.0f, random); 
                float cO2 = gerarValorAleatorio(300.0f, 500.0f, random);        
                float qualidadeDoAr = gerarValorAleatorio(0.0f, 500.0f, random); 
                float velocidadeDoVento = gerarValorAleatorio(0.0f, 20.0f, random); 
                float voltagem = gerarValorAleatorio(0.0f, 0.1f, random);   
                float rpm = gerarValorAleatorio(0.0f, 5000.0f, random);         
                float ph = gerarValorAleatorio(0.0f, 14.0f, random);         
                float pluviometria = gerarValorAleatorio(0.0f, 100.0f, random);         

                PlacaOutPut placaOutPut = new PlacaOutPut(
                        calendar.getTime(),
                        temperatura,
                        umidade,
                        pressao,
                        luminosidade,
                        cO2,
                        qualidadeDoAr,
                        velocidadeDoVento,
                        voltagem, 
                        rpm,
                        placa,
                        ph,
                        pluviometria
                );

                placaOutPutRepositoryMongoDB.save(placaOutPut);

                calendar.add(Calendar.HOUR, 1);
            }
        }

    }
    private float gerarValorAleatorio(float min, float max, Random random) {
        return min + random.nextFloat() * (max - min);
    }


    public List<PlacaOutPut> converterTxtParaPlacaOutPut(String filePath, Placa placa) throws ParseException {
        List<PlacaOutPut> registros = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Date dataHora = null;
            float temperatura = 0;
            float  umidade = 0;
            float  pressao = 0;
            float  luminosidade = 0;
            float  co2 = 0;
            float  qualidadeDoAr = 0;
            float  velocidadeDoVento = 0;
            float  voltagem = 0;
            float  rpm;
            float  ph = 0;
            float  pluviometria = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                    dataHora = dateFormat.parse(line);
                } else if (line.startsWith("Temperatura:")) {
                    temperatura = Float.parseFloat(line.split(":")[1].replace("ºC", ""));
                } else if (line.startsWith("Umidade:")) {
                    umidade = Float.parseFloat(line.split(":")[1].replace("%", ""));
                } else if (line.startsWith("Pressão:")) {
                    pressao = Float.parseFloat(line.split(":")[1].replace("hPa", ""));
                } else if (line.startsWith("Luz:")) {
                    luminosidade = Float.parseFloat(line.split(":")[1].replace("lx", ""));
                } else if (line.startsWith("Gás:")) {
                    co2 = Float.parseFloat(line.split(":")[1].replace(",", "."));
                } else if (line.startsWith("Ar:")) {
                    qualidadeDoAr = Float.parseFloat(line.split(":")[1].replace(",", "."));
                } else if (line.startsWith("Velocidade do vento:")) {
                    velocidadeDoVento = Float.parseFloat(line.split(":")[1]);
                } else if (line.startsWith("Voltagem:")) {
                    voltagem = Float.parseFloat(line.split(":")[1]);                
                } else if (line.startsWith("Pluviometria:")) {
                    pluviometria = Float.parseFloat(line.split(":")[1].replace(",", "."));
                } else if (line.startsWith("pH:")) {
                    ph = Float.parseFloat(line.split(":")[1].replace(",", "."));
                } else if (line.startsWith("Rpm:")) {
                    rpm = Float.parseFloat(line.split(":")[1]);

                    PlacaOutPut registro = new PlacaOutPut(dataHora, temperatura, umidade, pressao, luminosidade, co2, qualidadeDoAr, velocidadeDoVento, voltagem, rpm, placa, ph, pluviometria);
                    registros.add(registro);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
            e.printStackTrace(System.err);
        }

        return registros;
    }
}