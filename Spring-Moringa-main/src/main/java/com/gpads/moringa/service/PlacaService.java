package com.gpads.moringa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gpads.moringa.entities.Placa;
import com.gpads.moringa.repositories.PlacaRepositoryMongoDB;

@Service
public class PlacaService {
    
    private final PlacaRepositoryMongoDB placaRepositoryMongoDB;    

    public PlacaService(PlacaRepositoryMongoDB placaRepositoryMongoDB) {
        this.placaRepositoryMongoDB = placaRepositoryMongoDB;
    }
    public List<Placa> findAll(){
        return placaRepositoryMongoDB.findAll();
    }
    public void save(Placa entity){
        placaRepositoryMongoDB.save(entity);
    }
    public Placa findByLatLong(float lat, float log){
        return placaRepositoryMongoDB.findByLatLong(lat, log);
    }
    
}