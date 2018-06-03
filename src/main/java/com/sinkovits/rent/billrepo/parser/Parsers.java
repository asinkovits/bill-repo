package com.sinkovits.rent.billrepo.parser;

public class Parsers {

    public static final Parser ELECTRICITY_PARSER = new IfThenParser("Villamos", "Áram");
    public static final Parser HEATH_PARSER = new IfThenParser("Távhő", "Távhő");
    public static final Parser AMOUNT_PARSER = new BetweenParser("Fizetendő összeg:", "\n");

}
