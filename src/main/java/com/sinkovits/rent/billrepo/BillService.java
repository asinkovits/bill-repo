package com.sinkovits.rent.billrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;

@Service
public class BillService {

    // SQLs
    private static final String BILL_INSERT = "insert into bill(id,name,value) values(:id,:name,:value)";
    private static final String BILL_UPDATE = "update bill set name = :name, value = :value where id = :id";
    private static final String BILL_SELECT_ONE = "select id,name,value from bill where id = :id";
    private static final String BILL_SELECT_ALL = "select id,name,value from bill";

    private static final String BILL_DATA_SELECT = "select bill_id, file_name, data from bill_file where bill_id = :bill_id";
    private static final String BILL_DATA_INSERT = "insert into bill_file(bill_id, file_name, data) values(:bill_id,:file_name,:data)";

    // Columns
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String BILL_ID = "bill_id";
    private static final String FILE_NAME = "file_name";
    private static final String DATA = "data";

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BillService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Bill> getAllBills() {
        return jdbcTemplate.query(BILL_SELECT_ALL, new BillMapper());
    }

    public Bill getBill(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(BILL_SELECT_ONE, params, new BillMapper());
    }

    public void saveBill(Bill bill) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(bill);
        jdbcTemplate.update(BILL_INSERT, params);
    }

    public void updateBill(Bill bill) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(bill);
        jdbcTemplate.update(BILL_UPDATE, params);
    }

    public Bill processBillFile(MultipartFile file) throws IOException {
        Bill bill = new Bill("foo", "bar");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bill_id", bill.getId());
        params.addValue("file_name", file.getOriginalFilename());
        params.addValue("data", new SqlLobValue(file.getInputStream(),
                (int) file.getSize(), new DefaultLobHandler()), Types.BLOB);
        jdbcTemplate.update(BILL_DATA_INSERT, params);
        return bill;
    }

    public BillFile getBillFile(String id) {
        LobHandler lobHandler = new DefaultLobHandler();
        MapSqlParameterSource params = new MapSqlParameterSource(BILL_ID, id);
        BillFile file = jdbcTemplate.queryForObject(BILL_DATA_SELECT, params, new BillFileMapper());
        return file;
    }

    private class BillMapper implements RowMapper<Bill> {
        @Override
        public Bill mapRow(ResultSet rs, int i) throws SQLException {
            return new Bill(
                    rs.getString(ID),
                    rs.getString(NAME),
                    rs.getString(VALUE));
        }
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
