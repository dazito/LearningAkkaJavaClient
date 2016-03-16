package com.dazito.java.akkademy.client;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import com.dazito.java.dakkabase.messages.*;
import scala.compat.java8.FutureConverters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Created by daz on 27/02/2016.
 */
public class Client {
    private static final int TIMEOUT = 5000;

    private final ActorSystem system = ActorSystem.create("LocalSystem-Java");
    private final ActorSelection remoteDb;

    public Client(String remoteAddress) {
        remoteDb = system.actorSelection("akka.tcp://dakkabase-java@" + remoteAddress + "/user/dakkabase-db");
    }

    public CompletionStage set(String key, Object value) {
        return FutureConverters.toJava(Patterns.ask(remoteDb, new SetRequest(key, value), TIMEOUT));
    }

    public CompletionStage<Object> get(String key) {
        return FutureConverters.toJava(Patterns.ask(remoteDb, new GetRequest(key), TIMEOUT));
    }

    public CompletionStage<Object> delete(String key) {
        return FutureConverters.toJava(Patterns.ask(remoteDb, new DeleteMessage(key), TIMEOUT));
    }

    public CompletionStage<Object> setIfNotExists(String key, Object value) {
        return FutureConverters.toJava(Patterns.ask(remoteDb, new SetIfNotExists(key, value), TIMEOUT));
    }

    public CompletionStage<Object> setBatchRequest(Map<String, Object> map) {
        final List<SetRequest> setList = new ArrayList<>(map.size());
        map.forEach((s, o) -> {
            setList.add(new SetRequest(s, o));
        });

        return FutureConverters.toJava(Patterns.ask(remoteDb, new ListSetRequest(setList), TIMEOUT));
    }
}
