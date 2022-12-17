package dev.akursekova.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

//@JsonFormat
public enum OrderType {
    //BUY("buy"),
    //SELL("sell");

    BUY,
    SELL

    //@JsonValue
//    private String type;
//    OrderType(String type) {
//        this.type = type;
//    }

//    @JsonCreator
//    public OrderType fromString(String string) {
//        OrderType OrderType = FORMAT_MAP.get(string);
//        if (status == null) {
//            throw new IllegalArgumentException(string + " has no corresponding value");
//        }
//        return status;
//    }
}
