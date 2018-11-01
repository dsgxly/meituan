package com.itdr.vo;

public class test {
    public static void main(String[] args) {
        int sun =0;
        for(int i = 1; i <=10; i++){
            if(i%2!=0){
                continue;
            }else {
                sun+=i;
            }
        }
        System.out.println(sun);
    }
}
