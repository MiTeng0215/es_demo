package com.aegis.es_demo.domin;


import java.util.stream.Stream;


public class TestClass {

    public static void main(String[] args) {
        int oriState = 0;
        int state = 1;
        switch(oriState){

        }

    }
}
class Numbered {
    final int n;
    Numbered(int n) {
        this.n = n;
    }
    @Override
    public String toString() {
        return "Numbered(" + n + ")";
    }
    public String toOther() {
        return "OtherNumbered(" + n + ")";
    }
}
class FunctionMap2 {
    public static void main(String[] args) {
        Stream.of(1, 5, 7, 9, 11, 13)
                .map(Numbered::new)
                .forEach(System.out::println);
    }
}