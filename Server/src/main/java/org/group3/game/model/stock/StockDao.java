package org.group3.game.model.stock;


import org.group3.game.model.stock.Stock;

public interface StockDao {
 

	Stock findByStockCode(String stockCode);
 
}
