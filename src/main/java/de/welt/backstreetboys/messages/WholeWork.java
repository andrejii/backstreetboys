package de.welt.backstreetboys.messages;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class WholeWork {
    public Map<String, String> feeds = ImmutableMap.<String, String>builder()
            .put("http://www.welt.de/?config=ber_aufmacher", "json")
            .put("http://www.welt.de/politik?config=ber_aufmacher", "json")
            .put("http://www.welt.de/sport?config=ber_aufmacher", "json")
            .put("http://www.welt.de/?config=ber_highlights", "json")
            .put("http://www.welt.de/newssitemap/newssitemap.xml", "xml")
            .put("http://www.welt.de/", "html")
            .put("http://www.welt.de/politik/", "html")
            .put("http://www.welt.de/wirtschaft/", "html")
            .put("http://www.welt.de/finanzen/", "html")
            .put("http://www.welt.de/sport/", "html")
            .put("http://www.welt.de/wissenschaft/", "html")
            .put("http://www.welt.de/vermischtes/", "html")
            .put("http://www.welt.de/kultur/", "html")
            .put("http://www.welt.de/icon/", "html")
            .put("http://www.welt.de/reise/", "html")
            .put("http://www.welt.de/motor/", "html")
            .put("http://www.welt.de/regionales/", "html")
            .put("http://www.welt.de/debatte/", "html")
            .put("http://www.welt.de/videos/", "html")
            .put("http://www.welt.de/marktplatz/", "html")
            .build();

}