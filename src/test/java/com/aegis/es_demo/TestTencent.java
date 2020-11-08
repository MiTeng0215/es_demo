package com.aegis.es_demo;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;
import java.util.Set;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestTencent {

    private Set<String> getPassword(int size) {
        Set<String> passwords = Sets.newHashSet();
        while (passwords.size() < size) {
            passwords.add(String.format("%04d", new Random().nextInt(9999)));
        }
        return passwords;
    }
    @Test
    public void testPassword() {
        Set<String> passwords = getPassword(5);
        System.out.println(passwords);
    }
}
