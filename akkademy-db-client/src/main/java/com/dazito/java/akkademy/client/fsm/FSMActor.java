package com.dazito.java.akkademy.client.fsm;

import akka.actor.AbstractFSM;
import akka.actor.ActorSelection;
import com.dazito.java.akkademy.client.state.Flush;
import com.dazito.java.akkademy.client.state.State;
import com.dazito.java.dakkabase.messages.Connected;
import com.dazito.java.dakkabase.messages.GetRequest;
import com.dazito.java.dakkabase.messages.Request;

import java.util.LinkedList;

import static com.dazito.java.akkademy.client.state.State.*;

/**
 * Created by daz on 13/03/2016.
 */

class EventQueue extends LinkedList<Request> {}

public class FSMActor extends AbstractFSM<State, EventQueue> {

    private ActorSelection remoteDb;

    public FSMActor(String dbPath){
        remoteDb = context().actorSelection(dbPath);
    }

    {
       startWith(DISCONNECTED, new EventQueue());

        when(DISCONNECTED, matchEvent(Flush.class, (msg, container) -> stay())
                .event(Request.class, (msg, container) -> {
                    remoteDb.tell(new Connected(), self());
                    container.add(msg);
                    return stay();
                })
                .event(Connected.class, (msg, container) -> {
                    if (container.size() == 0) {
                        return goTo(CONNECTED);
                    }
                    else{
                        return goTo(CONNECTED_AND_PENDING);
                    }
                })
        );

        when(CONNECTED, matchEvent(Flush.class, (msg, container) -> stay())
                .event(Request.class, (msg, container) -> {
                        container.add(msg);
                        return goTo(CONNECTED_AND_PENDING);
                    }
                )
        );

        when(CONNECTED_AND_PENDING, matchEvent(Flush.class, (msg, container) -> {
                    remoteDb.tell(container, self());
                    container = new EventQueue();
                    return goTo(CONNECTED);
                }
            )
            .event(Request.class, (msg, container) -> {
                    container.add(msg);
                    return goTo(CONNECTED_AND_PENDING);
                }
            )
        );

        initialize();
    }


}
