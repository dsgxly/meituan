package com.itdr.test;

import org.apache.commons.lang.time.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        String date = com.itdr.utils.DateUtils.dateToStr(DateUtils.addHours(new Date(),-1));
        System.out.println(date);
    }


}
