package com.panport.logCloud.entity.alarm;

import lombok.Data;

import java.util.Date;

@Data
public class Source {

    private String content;
    private Boolean enable;
    private String ruleEnName;
}