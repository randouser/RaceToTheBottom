package org.group3.game.model.stock;


import org.group3.game.model.stock.Stock;

public interface StockService {
 

	Stock findByStockCode(String stockCode);
}
