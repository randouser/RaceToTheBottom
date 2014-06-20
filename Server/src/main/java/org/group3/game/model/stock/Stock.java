package org.group3.game.model.stock;

 

public class Stock implements java.io.Serializable {
 
	private Integer stockId;
	private String stockCode;
	private String stockName;
 
	public Stock() {
	}


	public Integer getStockId() {
		return this.stockId;
	}
 
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public String getStockCode() {
		return this.stockCode;
	}
 
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
 

	public String getStockName() {
		return this.stockName;
	}
 
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
 
	@Override
	public String toString() {
		return "Stock [stockCode=" + stockCode + ", stockId=" + stockId
				+ ", stockName=" + stockName + "]";
	}
}

