package com.sinkovits.rent.billrepo;

import com.sinkovits.rent.billrepo.parser.PdfFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Service
public class BillService {

    // SQLs
    private static final String BILL_INSERT = "insert into bill(id,name,value) values(:id,:name,:value)";
    private static final String BILL_UPDATE = "update bill set name = :name, value = :value where id = :id";
    private static final String BILL_SELECT_ONE = "select id,name,value from bill where id = :id";
    private static final String BILL_SELECT_ALL = "select id,name,value from bill";

    // Columns
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String VALUE = "value";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PdfFileParser fileParser;
    private final FileManager fileManager;

    @Autowired
    public BillService(NamedParameterJdbcTemplate jdbcTemplate, PdfFileParser fileParser, FileManager fileManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.fileParser = fileParser;
        this.fileManager = fileManager;
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
        Bill bill = fileParser.parse(file.getInputStream());
        saveBill(bill);
        fileManager.persist(bill, file);
        return bill;
    }

    public Bill updateBillFile(String id, MultipartFile file) throws IOException {
        Bill bill = getBill(id);
        fileManager.persist(bill, file);
        return bill;
    }

    public BillFile getBillFile(String id) {
        return fileManager.retrieve(id);
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
}
