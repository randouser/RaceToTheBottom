package org.group3.game.model.stock;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StockDaoImpl implements StockDao{


    @Autowired
    JdbcTemplate jdbcTemplate;



    @Override
    public Stock findByStockCode(String stockId){

        String sql = "SELECT * FROM stock WHERE STOCK_CODE = ?";
        Object[] args = {stockId};

        return (Stock)jdbcTemplate.queryForObject(sql, args, new StockRowMapper());


    }
 
}
