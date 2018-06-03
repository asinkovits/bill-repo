package com.sinkovits.rent.billrepo.parser;

import com.sinkovits.rent.billrepo.Bill;

import java.util.List;
import java.util.Optional;

public class BasicBillParser {

    private static final String BLANK = "";

    private final List<Parser> nameParsers;
    private final List<Parser> valueParser;

    public BasicBillParser(List<Parser> nameParsers, List<Parser> valueParser) {
        this.nameParsers = nameParsers;
        this.valueParser = valueParser;
    }

    public Bill parse(final String text) {
        String name = tryParse(text, nameParsers);
        String value = tryParse(text, valueParser);
        return new Bill(name, value);
    }

    private String tryParse(String text, List<Parser> parsers) {
        for (Parser parser : parsers) {
            Optional<String> res = parser.parse(text);
            if (res.isPresent()) {
                return res.get();
            }
        }
        return BLANK;
    }
}
