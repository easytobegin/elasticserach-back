package com.panport.logCloud.entity.alarm;
import lombok.Data;

import java.util.List;

@Data
public class Hits {

    private int total;
    private int max_score;
    private List<Hitss> hits;
}