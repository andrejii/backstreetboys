package de.welt.backstreetboys.messages;

import java.util.Map;

public class Task {
    public String type;
    public String url;

    public Task(Map.Entry<String, String> workTask) {
        type = workTask.getValue();
        url = workTask.getKey();
    }
}
