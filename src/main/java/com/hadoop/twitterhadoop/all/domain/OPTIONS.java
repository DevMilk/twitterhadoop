package com.hadoop.twitterhadoop.all.domain;

import lombok.ToString;

@ToString
public enum OPTIONS {
    RT(CONSTANTS.most_rts,"number_of_top_users"),
    LIKE(CONSTANTS.most_likes,"number_of_top_users"),
    REP(CONSTANTS.most_reps,"number_of_top_users"),
    TIME(CONSTANTS.most_times,"date"),
    LENGTH(CONSTANTS.length,"length");

    public final String path;
    public final String argType;
    private OPTIONS(String path,String argType) {
        this.path = path;
        this.argType = argType;
    }
}
