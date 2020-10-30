package com.fuckwzxy.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Object data;
    private Map<Object,Object> meta = new HashMap<>();
}
