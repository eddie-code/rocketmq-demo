package com.example.order.constants;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order.constants
 * @ClassName OrderStatus
 * @description
 * @date created in 2021-07-22 10:09
 * @modified by
 */
public enum OrderStatus {

    ORDER_CREATED("1"),

    ORDER_PAYED("2"),

    ORDER_FALL("3");

    private String status;

    private OrderStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }

}
