package com.dazito.java.akkademy.client;

import akka.actor.AbstractActorWithStash;
import akka.japi.pf.ReceiveBuilder;
import com.dazito.java.dakkabase.messages.Connected;
import com.dazito.java.dakkabase.messages.GetRequest;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by daz on 09/03/2016.
 */
public class RemoteActorProxyWihtBecome extends AbstractActorWithStash {

    public RemoteActorProxyWihtBecome() {
        receive(ReceiveBuilder
                .match(GetRequest.class, message -> {
                    stash();
                })
                .match(Connected.class, message -> {
                    context().become(online());
                    unstash();
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
