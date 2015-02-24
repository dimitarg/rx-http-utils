package com.dgeorgiev.rx.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Json {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static <A> A read(String raw, Class<A> resultClass) {
        try{
            return mapper.readValue(raw, resultClass);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
