package org.group3.game.model.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StockServiceImpl implements StockService{
 
	@Autowired
	StockDao stockDao;

 
	@Override
	public Stock findByStockCode(String stockCode){
		return stockDao.findByStockCode(stockCode);
	}
}