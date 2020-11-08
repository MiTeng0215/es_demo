package com.aegis.es_demo.config;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }//获取

    @Override
    public Object getCredentials() {
        return token;
    }
}
