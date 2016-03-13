package com.dazito.java.akkademy.client.state;

/**
 * Created by daz on 13/03/2016.
 */
public enum State {

    DISCONNECTED, // Not online and no messages are queued
    CONNECTED, // Online and no messages are queued
    CONNECTED_AND_PENDING; // Online and messages are pending
}
