package com.gpads.moringa.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


//@Entity
@Document("placaMoringa")
public class Placa {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String modelo;
    private float latitude;
    private float longitude;
    
    public Placa(){}
    public Placa(String modelo, float latitude, float longitude) {
        this.modelo = modelo;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public ObjectId getId() {
        return id;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

}
