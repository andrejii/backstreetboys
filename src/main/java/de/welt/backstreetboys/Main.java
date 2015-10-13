package de.welt.backstreetboys;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import de.welt.backstreetboys.actors.Master;
import de.welt.backstreetboys.messages.WholeWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Main {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.debug("start");
        new Main().run();
    }

    private void run() {
        ActorSystem system = ActorSystem.create("BackstreetBoys");
        ActorRef master = system.actorOf(Master.createMaster(), "Nick");
//        master.tell(new WholeWork(), ActorRef.noSender());

        system.scheduler().schedule(
            Duration.Zero(),
            Duration.create(10, TimeUnit.SECONDS),
            master,
            new WholeWork(),
            system.dispatcher(), null);
    }
}
