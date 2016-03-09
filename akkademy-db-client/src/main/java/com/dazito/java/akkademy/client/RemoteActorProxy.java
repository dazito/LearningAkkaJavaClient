package com.dazito.java.akkademy.client;

import akka.actor.AbstractActorWithStash;
import akka.japi.pf.ReceiveBuilder;
import com.dazito.java.dakkabase.messages.Connected;
import com.dazito.java.dakkabase.messages.Disconnected;
import com.dazito.java.dakkabase.messages.GetRequest;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by daz on 08/03/2016.
 */
public class RemoteActorProxy extends AbstractActorWithStash {

    private boolean isOnline = false;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(GetRequest.class, message -> {
                    if(isOnline) {
                        processMessage(message);
                    }
                    else {
                        stash();
                    }
                })
                .match(Connected.class, message -> {
                    isOnline = true;
                    unstash();
                })
                .match(Disconnected.class, message -> {
                    isOnline = false;
                })
                .build();
    }

    private void processMessage(Object message) {
        // Do something
    }
}
