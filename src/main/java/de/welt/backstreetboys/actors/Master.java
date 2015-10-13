package de.welt.backstreetboys.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;
import com.google.common.base.Joiner;
import de.welt.backstreetboys.Time;
import de.welt.backstreetboys.messages.Result;
import de.welt.backstreetboys.messages.Task;
import de.welt.backstreetboys.messages.WholeWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Master extends UntypedActor {

    private long countOfFeeds;
    private long processedFeeds;
    private ActorRef workerRouter;
    private final Time time = new Time();
    private final static Logger LOG = LoggerFactory.getLogger(Master.class);
    private Set<String> allIDs = new HashSet<String>();

    public Master() {
        workerRouter = getContext().actorOf(Worker.createWorker().withRouter(new RoundRobinPool(4)), "workerRouter");
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof WholeWork) {
            time.start();
            processMessages(((WholeWork) message).feeds);
        } else if (message instanceof Result) {
            allIDs.addAll(((Result) message).ids);
            if (countOfFeeds == ++processedFeeds) {
                afterDonwloadAll();
            }
        } else {
            unhandled(message);
        }
    }

    private void processMessages(Map<String, String> workTasks) {
        processedFeeds = 0;
        this.countOfFeeds = workTasks.size();
        workTasks.entrySet().forEach(workTask -> workerRouter.tell(new Task(workTask), getSelf()));
    }

    private void afterDonwloadAll() {
        time.end();
        LOG.info("Backstreet Boys downloaded all feeds in {} ms.", time.elapsedTimeMilliseconds());
        LOG.info("Now send {} ids to message queue: [{}]", allIDs.size(), Joiner.on(",").join(allIDs));
//        getContext().system().shutdown();

    }

    public static Props createMaster() {
        return Props.create(Master.class);
    }
}
