package com.dili.ia.domain.dto;

import java.util.Map;

/**
 * 打印数据传输对象
 */
public class PrintDataDto {

    private String name;
    private Map<String,Object> item;

    public PrintDataDto(){}

    public PrintDataDto(String name, Map<String, Object> item) {
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getItem() {
        return item;
    }

    public void setItem(Map<String, Object> item) {
        this.item = item;
    }
}
