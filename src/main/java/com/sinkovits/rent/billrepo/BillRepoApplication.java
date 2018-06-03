package com.sinkovits.rent.billrepo;

import com.google.common.collect.Lists;
import com.sinkovits.rent.billrepo.parser.BasicBillParser;
import com.sinkovits.rent.billrepo.parser.Parser;
import com.sinkovits.rent.billrepo.parser.Parsers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
public class BillRepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillRepoApplication.class, args);
    }

    @Bean
    public BasicBillParser billParser() {
        List<Parser> nameParsers = Lists.newArrayList(Parsers.ELECTRICITY_PARSER, Parsers.HEATH_PARSER);
        List<Parser> valueParser = Lists.newArrayList(Parsers.AMOUNT_PARSER);
        return new BasicBillParser(nameParsers, valueParser);
    }


    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.DERBY)
                .addScript("sql/create-schema.sql")
                .addScript("sql/test-data.sql")
                .build();
    }
}
