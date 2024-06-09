package com.example.bigdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    public int id;
    public String stationName;
    public int totalDocks;
    public int docksInService;
    public String status;
    public String latitude;
    public String longitude;
    public String location;
}
