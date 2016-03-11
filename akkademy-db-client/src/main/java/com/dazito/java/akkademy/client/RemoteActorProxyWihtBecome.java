package com.dazito.java.akkademy.client;

import akka.actor.AbstractActorWithStash;
import akka.japi.pf.ReceiveBuilder;
import com.dazito.java.akkademy.client.exception.ConnectionTimeoutException;
import com.dazito.java.dakkabase.messages.CheckConnected;
import com.dazito.java.dakkabase.messages.Connected;
import com.dazito.java.dakkabase.messages.GetRequest;
import scala.PartialFunction;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;

import java.util.concurrent.TimeUnit;

/**
 * Created by daz on 09/03/2016.
 */
public class RemoteActorProxyWihtBecome extends AbstractActorWithStash {

    private final ExecutionContextExecutor executor;

    public RemoteActorProxyWihtBecome() {

        // Schedule a message to check if we are connected, if it is not connected we escalate it.
        // This is in order to avoid stash leaks.

        executor = getContext().system().dispatcher();
        getContext().system().scheduler().scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS), self(), new CheckConnected(), executor, null);

        receive(ReceiveBuilder
                        .match(GetRequest.class, message -> {
                            context().unbecome();
                            stash();
                        })
                        .match(Connected.class, message -> {
                            context().become(online());
                            unstash();
                        })
                        .match(CheckConnected.class, message -> {
                            throw new ConnectionTimeoutException("Connection timed out");
                        })
                        .build()
        );
    }



    private PartialFunction<Object, BoxedUnit> online () {
        return ReceiveBuilder.match(GetRequest.class, message -> processMessage(message)).build();
    }

    private void processMessage(Object message) {
        // Do something
    }
}
