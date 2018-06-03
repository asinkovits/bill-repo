package com.sinkovits.rent.billrepo.parser;

import java.util.Optional;

public class BetweenParser implements Parser {

    private final String pre;
    private final String post;

    public BetweenParser(String pre, String post) {
        this.pre = pre;
        this.post = post;
    }

    @Override
    public Optional<String> parse(String text) {
        int start = text.indexOf(pre);
        if (start == -1) {
            return Optional.empty();
        }
        String remainder = text.substring(start + pre.length());
        int end = remainder.indexOf(post);
        if (end == -1){
            return Optional.empty();
        }
        return Optional.of(remainder.substring(0, end).replaceAll("[^0-9]", ""));
    }
}
