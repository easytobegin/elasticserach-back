package com.panport.logCloud.constant;

/**
 * Created by minisheep on 2019/8/22.
 */
public class Constant {
    public final static String CLUSTER_IP = "10.1.16.54:6379,10.1.16.54:6380,10.1.16.54:6381,10.1.16.54:8082,10.1.16.54:8083,10.1.16.54:8084";

    public final static Integer SINGLE_REDIS_MAXTOTAL = 300;

    public final static Integer SINGLE_REDIS_MAXIDLE = 8;

    public final static Integer SINGLE_REDIS_MAXWAIT = 1000 * 100;

    public final static Boolean SINGLE_REDIS_BORROW = false;

    public final static String SINGLE_REDIS_IP = "10.1.16.54";

    public final static Integer SINGLE_REDIS_PORT = 6379;

    public final static Integer SINGLE_REDIS_TIMEOUT = 10000;

    public final static String SINGLE_REDIS_PASSWORD = "123456";


    // alarm redis队列key
    public final static String ALARM_KRY = "alarmKey";

    public final static String RULEENNAME = "ruleEnName";

    public final static String CONTENT = "content";

    public final static String TIMESTAMP = "timestamp";

}
