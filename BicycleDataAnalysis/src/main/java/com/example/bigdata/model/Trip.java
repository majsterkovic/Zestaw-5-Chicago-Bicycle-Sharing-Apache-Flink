package com.example.bigdata.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
    public class Trip implements Serializable {
    public int tripId;
    public boolean startStop;
    public LocalDateTime eventTime;
    public int stationId;
    public double tripDuration;
    public String userType;
    public String gender;
    public int week;
    public double temperature;
    public String events;

}
