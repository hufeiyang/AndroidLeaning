package com.hfy.demo01.module.jetpack.viewmodel.fragment.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserContent {

    public static final List<UserItem> ITEMS = new ArrayList<UserItem>();
    public static final Map<String, UserItem> ITEM_MAP = new HashMap<String, UserItem>();
    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(UserItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static UserItem createDummyItem(int position) {
        return new UserItem(String.valueOf(position), "User " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about User: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class UserItem {
        public final String id;
        public final String content;
        public final String details;

        public UserItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}