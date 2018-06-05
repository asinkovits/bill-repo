package com.sinkovits.rent.billrepo.parser;

import com.sinkovits.rent.billrepo.Bill;
import com.sinkovits.rent.billrepo.BillFile;
import com.sinkovits.rent.billrepo.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

//@Component
public class DBFileManager implements FileManager {

    // SQLs
    private static final String BILL_DATA_SELECT = "select bill_id, file_name, data from bill_file where bill_id = :bill_id";
    private static final String BILL_DATA_INSERT = "insert into bill_file(bill_id, file_name, data) values(:bill_id,:file_name,:data)";

    // Columns
    private static final String BILL_ID = "bill_id";
    private static final String FILE_NAME = "file_name";
    private static final String DATA = "data";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DBFileManager(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void persist(Bill bill, MultipartFile file) throws IOException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bill_id", bill.getId());
        params.addValue("file_name", file.getOriginalFilename());
        params.addValue("data", new SqlLobValue(file.getInputStream(),
                (int) file.getSize(), new DefaultLobHandler()), Types.BLOB);
        jdbcTemplate.update(BILL_DATA_INSERT, params);
    }

    @Override
    public BillFile retrieve(String id) {
        LobHandler lobHandler = new DefaultLobHandler();
        MapSqlParameterSource params = new MapSqlParameterSource(BILL_ID, id);
        BillFile file = jdbcTemplate.queryForObject(BILL_DATA_SELECT, params, new BillFileMapper());
        return file;
    }

    private class BillFileMapper implements RowMapper<BillFile> {

        LobHandler lobHandler = new DefaultLobHandler();

        @Override
        public BillFile mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BillFile(
                    rs.getString(FILE_NAME),
                    new ByteArrayResource(lobHandler.getBlobAsBytes(rs, DATA)));
        }
    }

}
