package com.itdr.test;

import redis.clients.jedis.Jedis;

public class Test {

    public static void main(String[] args){
        Jedis jedis = new Jedis("39.105.36.227",6379);
        System.out.println(jedis.ping());
    }


}
