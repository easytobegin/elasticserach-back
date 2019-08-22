package com.panport.logCloud.entity.alarm;

import lombok.Data;

@Data
public class AlarmBean {

    private int took;
    private boolean timed_out;
    private Shards Shards;
    private Hits hits;

    @Override
    public String toString() {
        return "AlarmBean{" +
                "took=" + took +
                ", timed_out=" + timed_out +
                ", Shards=" + Shards +
                ", hits=" + hits +
                '}';
    }
}