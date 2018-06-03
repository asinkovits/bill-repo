package com.sinkovits.rent.billrepo.parser;

import java.util.Optional;

public interface Parser {

    Optional<String> parse(String text);
}
