package com.interview.interviewservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Flag {

    NONE(Flag.NONE_ID),
    ENABLED(Flag.ENABLED_ID), APPROVED(Flag.APPROVED_ID),
    DISABLED(Flag.DISABLED_ID), REJECTED(Flag.REJECTED_ID);

    public static final int NONE_ID = 0;

    public static final int DISABLED_ID = 1;

    public static final int ENABLED_ID = 2;

    public static final int APPROVED_ID = 3;

    public static final int REJECTED_ID = 4;

    public static final int DONE_ID = 5;

    public static final int CANCELLED_ID = 6;

    private int id;

    private String name;

    Flag(int id) {
        this.id = id;
    }


    Flag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Flag getFlag(int id) {
        return Arrays.stream(Flag.values()).filter(flag -> flag.getId() == id).findFirst().orElse(Flag.NONE);
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return this.name != null ? this.name : name();
    }

}
