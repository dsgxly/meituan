package com.itdr.test;

import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List list = Arrays.asList(new Object[]{"a","b","c"});
        list.remove(0);
        System.out.println(list);
    }


}
