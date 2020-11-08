package com.aegis.es_demo.highConcurrency;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Demo1 {
    String a;
    String b;
    public Demo1(String a, String b) {
        this.a = a;
        this.b = b;
    }

    /**
     * 如果类定义了有参构造,当创建对象时会默认调用有参构造,如果想继续使用无参构造,必须显示的书写无参构造方法
     * @param args
     */
    public static void main(String[] args) {
        Demo1 demo1 = new Demo1();
    }
}
