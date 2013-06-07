package de.consol.research.cache.common;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserEventList implements Serializable {

    private static final int SIZE_LIMIT = 10; // Max number of messages
    private final List<String> messages;

    public static UserEventList emptyList() {
        return new UserEventList(new ArrayList<String>());
    }

    public static UserEventList append(UserEventList list, String msg) {
        List<String> newMessageList = new ArrayList<>();
        newMessageList.addAll(list.messages);
        newMessageList.add(format(msg));
        while ( newMessageList.size() > SIZE_LIMIT ) {
            newMessageList.remove(0);
        }
        return new UserEventList(newMessageList);
    }

    private static String format(String msg) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date()) + " " + msg;
    }

    private UserEventList(List<String> origMessageList) {
        if ( origMessageList == null || origMessageList.size() > SIZE_LIMIT ) {
            throw new IllegalArgumentException();
        }
        this.messages = Collections.unmodifiableList(origMessageList);
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object other) {
        if ( other instanceof UserEventList) {
            return messages.equals(((UserEventList) other).getMessages());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return messages.hashCode();
    }
}
