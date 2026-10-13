package com.lastma.data.models;

public enum ViolationType {
    OVERSPEEDING(50_000),
    DUI(11_000),
    EXPIRED_PAPERS(100_000),
    TRAFFIC_LIGHT_OFFENCE(15_000),
    ONE_WAY(25_000),
    ;

    ViolationType(int fee) {
    }
}
