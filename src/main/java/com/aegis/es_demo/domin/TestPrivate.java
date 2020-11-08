package com.aegis.es_demo.domin;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class TestPrivate {
    private  static  String name = "苗腾";
    protected  static   String salary = "1000";
}

class TestProtected extends TestPrivate {
    public static void main(String[] args) throws ParseException {
        List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining());
        System.out.println("合并字符串: " + mergedString);

    }
}