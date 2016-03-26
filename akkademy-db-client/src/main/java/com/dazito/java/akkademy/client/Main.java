package com.dazito.java.akkademy.client;

import java.util.function.Consumer;

/**
 * Created by daz on 26/03/2016.
 */
public class Main {

    public static void main(String... args) throws InterruptedException {
        final Client client = new Client("127.0.0.1:2553");
        while (true) {
            Thread.sleep(1000);
            client.isConnected().thenAccept(new Consumer<Object>() {
                @Override
                public void accept(Object object) {
                    final Boolean isConnected = (Boolean) object;
                    System.out.println("Main: isConnected? " + isConnected);
                }
            });
        }

    }
}
