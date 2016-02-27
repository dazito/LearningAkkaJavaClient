package com.dazito.java.akkademy.client;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

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
}
