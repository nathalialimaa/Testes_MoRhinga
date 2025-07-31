package com.gpads.moringa.repositories;



import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.gpads.moringa.entities.PlacaOutPut;

public interface PlacaOutPutRepositoryMongoDB extends MongoRepository<PlacaOutPut, ObjectId>{
    @Query(value = "{ 'placa.id': ?0 }", sort = "{ 'dataHora': -1 }")
    List<PlacaOutPut> findLatestByPlacaId(ObjectId placaId);

    @Query("{ 'placa.id': ?0, 'dataHora': { $gte: ?1, $lte: ?2 } }")
    List<PlacaOutPut> findByPlacaIdAndDateRange(ObjectId placaId, Date startDate, Date endDate);
}