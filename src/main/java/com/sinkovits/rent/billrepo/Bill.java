package com.sinkovits.rent.billrepo;

import java.util.UUID;

public class Bill {

    private final String id;
    private final String name;
    private final String value;

    public Bill(String name, String value) {
        this(UUID.randomUUID().toString(), name, value);
    }

    public Bill(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
