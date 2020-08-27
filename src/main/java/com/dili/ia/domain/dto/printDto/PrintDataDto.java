package com.dili.ia.domain.dto.printDto;

/**
 * 打印数据传输对象
 */
public class PrintDataDto<T> {

    private String name;
    private T item;

    public PrintDataDto(){}

    public PrintDataDto(String name, T item) {
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
