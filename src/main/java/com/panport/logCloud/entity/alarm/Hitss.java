package com.panport.logCloud.entity.alarm;

import lombok.Data;

/**
 * Created by minisheep on 2019/8/22.
 */
@Data
public class Hitss {
    private String _index;
    private String _type;
    private String _id;
    private int _score;
    private Source _source;
}
