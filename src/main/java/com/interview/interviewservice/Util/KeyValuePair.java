package com.interview.interviewservice.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValuePair implements Serializable {
    private String key;
    private String value;

    public KeyValuePair(Long key, String value) {
        this.key = String.valueOf(key);
        this.value = value;
    }
}
