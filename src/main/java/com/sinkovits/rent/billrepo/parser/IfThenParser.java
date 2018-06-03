package com.sinkovits.rent.billrepo.parser;

import java.util.Optional;

public class IfThenParser implements Parser {

    private final String ifValue;
    private final String thenValue;

    public IfThenParser(String ifValue, String thenValue) {
        this.ifValue = ifValue;
        this.thenValue = thenValue;
    }

    @Override
    public Optional<String> parse(String text) {
        if (text.contains(ifValue)) {
            return Optional.of(thenValue);
        }
        return Optional.empty();
    }
}
