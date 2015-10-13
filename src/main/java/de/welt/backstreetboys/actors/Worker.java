package de.welt.backstreetboys.actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import de.welt.backstreetboys.messages.Result;
import de.welt.backstreetboys.messages.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Worker extends UntypedActor {
    private final static Logger LOG = LoggerFactory.getLogger(Worker.class);

    @Override
    public void onReceive(Object message) {
        if (message instanceof Task) {
            LOG.info("Get ids from {}", ((Task) message).url);
            Set<String> idsToUpdate = parseForArticleIDs(
                    downloadFeed(((Task) message).url), ((Task) message).type);
            LOG.info("Found {} ids.", idsToUpdate.size());
            getSender().tell(new Result(idsToUpdate), getSelf());
        } else
            unhandled(message);
    }

    public static Props createWorker() {
        return Props.create(Worker.class);
    }

    private String downloadFeed(final String url) {
        try {
            return Resources.toString(new URL(url), Charsets.UTF_8);
        } catch (IOException e) {
            LOG.error("Download from url {} fails.", url, e);
        }
        return "";
    }

    private Set<String> parseForArticleIDs(final String rawResponse, final String type) {
        switch (type) {
            case "json":
                return parseForArticleIDsInJSON(rawResponse);
            case "xml":
                return parseForArticleIDsInXML(rawResponse);
            case "html":
                return parseForArticleIDsInXML(rawResponse);
            default:
                LOG.error("No parser for type {}.", type);
                return Collections.emptySet();
        }

    }

    private Set<String> parseForArticleIDsInXML(final String rawResponse) {
        final Pattern pattern = Pattern.compile("/article\\d{9}");
        Matcher matcher = pattern.matcher(rawResponse);
        final Set<String> ids = new HashSet<>();
        while (matcher.find()) {
            final String match = matcher.group();
            ids.add(match.substring("/article".length()));
        }
        return ids;
    }

    private Set<String> parseForArticleIDsInJSON(final String rawResponse) {
        final Pattern pattern = Pattern.compile("\"id\":\\d{9}");
        Matcher matcher = pattern.matcher(rawResponse);
        final Set<String> ids = new HashSet<>();
        while (matcher.find()) {
            final String match = matcher.group();
            ids.add(match.substring("\"id\":".length()));
        }
        return ids;
    }

}