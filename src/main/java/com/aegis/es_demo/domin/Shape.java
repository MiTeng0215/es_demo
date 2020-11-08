package com.aegis.es_demo.domin;

public class Shape {
    public void draw(){
        System.out.println("画一个形状");
    }
}
class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("画一个圆圈");
    }

    public static void main(String[] args) {
        Shape circle = new Circle();
        circle.draw();
    }
}
