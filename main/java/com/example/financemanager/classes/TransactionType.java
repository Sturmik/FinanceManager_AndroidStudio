package com.example.financemanager.classes;

import java.util.HashMap;
import java.util.Map;

// Type of the transaction
public enum TransactionType
{
    INCOME(0),
    EXPENSE(1);

    private int value;
    private static Map map = new HashMap<>();

    private TransactionType(int value) {
        this.value = value;
    }

    static {
        for (TransactionType pageType : TransactionType.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static TransactionType valueOf(int pageType) {
        return (TransactionType) map.get(pageType);
    }

    public int getValue() {
        return value;
    }
}
