package org.group3.game.model.stock;

import org.group3.game.model.stock.Stock;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by alleninteractions on 6/16/14.
 */
public class StockRowMapper implements RowMapper{
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Stock stock = new Stock();
        stock.setStockId(resultSet.getInt("STOCK_ID"));
        stock.setStockName(resultSet.getString("STOCK_NAME"));
        stock.setStockCode(resultSet.getString("STOCK_CODE"));
        return stock;
    }
}
