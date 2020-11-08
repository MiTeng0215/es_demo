package com.aegis.es_demo.domin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestBuilder {
    private String name;

    public String getNickName() {
        return "ccccc";
    }
}
