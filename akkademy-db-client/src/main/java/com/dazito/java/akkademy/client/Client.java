package com.dazito.java.akkademy.client;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import com.dazito.java.dakkabase.messages.DeleteMessage;
import com.dazito.java.dakkabase.messages.GetRequest;
import com.dazito.java.dakkabase.messages.SetIfNotExists;
import com.dazito.java.dakkabase.messages.SetRequest;
import scala.compat.java8.FutureConverters;

import java.util.concurrent.CompletionStage;

/**
 * Created by daz on 27/02/2016.
 */
public class Client {
    private final ActorSystem system = ActorSystem.create("LocalSystem-Java");
    private final ActorSelection remoteDb;

    public Client(String remoteAddress) {
        remoteDb = system.actorSelection("akka.tcp://dakkabase-java@" + remoteAddress + "/user/dakkabase-db");
    }

    public CompletionStage set(String key, Object value) {
        return FutureConverters.toJava(Patterns.ask(remoteDb, new SetRequest(key, value), 5000));
    }

    public CompletionStage<Object> get(String key) {
        return FutureConverters.toJava(Patterns.ask(remoteDb, new GetRequest(key), 5000));
    }

    public CompletionStage<Object> delete(String key) {
        return FutureConverters.toJava(Patterns.ask(remoteDb, new DeleteMessage(key), 5000));
    }

    public CompletionStage<Object> setIfNotExists(String key, Object value) {
        return FutureConverters.toJava(Patterns.ask(remoteDb, new SetIfNotExists(key, value), 5000));
    }
}
