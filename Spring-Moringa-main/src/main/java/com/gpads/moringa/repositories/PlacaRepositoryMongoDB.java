package com.gpads.moringa.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.gpads.moringa.entities.Placa;

public interface PlacaRepositoryMongoDB extends MongoRepository<Placa, ObjectId>{
    @Query("{ 'latitude': ?0, 'longitude': ?1 }")
    Placa findByLatLong(float latitude, float longitude);
}