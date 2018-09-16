package com.wesine.uvdemo;

public class TestCLass {
    public static void main(String[] args) {
        String str = "WS 3DD33VF";
        String str1 = " WS3DD33VF";
        String str2 = "WS3DD33VF ";
        String str3 = "ws 3dd33vf";
        System.out.println(str.replace(" ","").toUpperCase());
        System.out.println(str1.replace(" ","").toUpperCase());
        System.out.println(str2.replace(" ","").toUpperCase());
        System.out.println(str3.replace(" ","").toUpperCase());
    }



}
