package com.dazito.java.akkademy.client;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import com.dazito.java.dakkabase.messages.CheckConnected;
import com.dazito.java.dakkabase.messages.Connected;
import scala.PartialFunction;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;

import java.util.concurrent.TimeUnit;

/**
 * Created by daz on 26/03/2016.
 */
public class ConnectionMonitorActor extends AbstractActor {

    private boolean isOnline;

    public ConnectionMonitorActor(boolean isOnline, final ActorSelection actor) throws Exception {
        this.isOnline = isOnline;

        AskableActorSelection askableActorSelection = new AskableActorSelection(actor);
        Future<Object> future = askableActorSelection.ask(new Identify(1), new Timeout(5, TimeUnit.SECONDS));

        ActorIdentity ident = (ActorIdentity) Await.result(future, new Timeout(5, TimeUnit.SECONDS).duration());
        ActorRef ref = ident.getRef();

        ExecutionContextExecutor executor = getContext().system().dispatcher();
        getContext().system().scheduler().schedule(Duration.apply(1000, TimeUnit.MILLISECONDS), Duration.apply(1000, TimeUnit.MILLISECONDS), ref , new Connected(), executor, self());


    }

    public ConnectionMonitorActor(final ActorSelection actor) throws Exception {
        this(false, actor);
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(Connected.class, msg -> {
                    isOnline = true;
                    System.out.println("Is connected...!");
                })
                .match(CheckConnected.class, msg -> sender().tell(isOnline, self()))
                .matchAny(msg -> {
                    isOnline = false;
                    System.out.println("Oh no! Lost the connection to the server!");
                })
                .build();
    }
}
