package com.dazito.java.akkademy.client;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by daz on 27/02/2016.
 */
public class ClientIntegrationTest {
    private Client client = new Client("127.0.0.1:2553");

    @Test
    public void itShouldSetRecord() throws Exception {
        client.set("123", 123);
        Integer result = (Integer) ((CompletableFuture) client.get("123")).get();

        assert (result == 123);
    }

    @Test
    public void itShouldDeleteRecord() throws Exception {
        CompletableFuture completableFuture = (CompletableFuture) client.delete("123");
        String result = (String) completableFuture.get();

        assert (Objects.equals(result, "123"));
    }

    @Test(expected = ExecutionException.class)
    public void getValueShouldFail() throws Exception {
        CompletableFuture completableFuture = (CompletableFuture) client.get("-1");
        Integer result = (Integer) completableFuture.get();
    }

    @Test
    public void setIfNotExist() throws Exception {
        client.setIfNotExists("does not exist", "a non existing value");
        CompletableFuture completableFuture = (CompletableFuture) client.get("does not exist");
        String result = (String) completableFuture.get();

        assert (Objects.equals(result, "a non existing value"));
    }

    @Test
    public void setBatchRequest() throws Exception {
        final Map<String, Object> map = new HashMap<>();
        for(int i = 0; i<5; i++) {
            map.put(String.valueOf(i), i);
        }
        final CompletableFuture completableFuture = (CompletableFuture) client.setBatchRequest(map);
        final String result = (String) completableFuture.get();
        assert(Objects.equals(result, "OK"));
    }
}
