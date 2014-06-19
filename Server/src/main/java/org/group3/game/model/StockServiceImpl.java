package org.group3.game.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 

 
@Service("stockService")
public class StockServiceImpl implements StockService{
 
	@Autowired
	StockDao stockDao;

 
	@Override
	public Stock findByStockCode(String stockCode){
		return stockDao.findByStockCode(stockCode);
	}
}