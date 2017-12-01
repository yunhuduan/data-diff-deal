package cn.com.flaginfo.listener.com.support;

/**
 * 分页信息
 * @author Rain
 *
 */
public class QueryInfo {

	/**
	 * Number of records to include in result set
	 */
	private Integer pageLimit = 10;
	
	/**
	 * Number of all records
	 */
	private Integer total = 0;
	
	/**
	 * 查询当前页
	 */
	private Integer curPage = 1;
	
	/**
	 * 其实数据库ID
	 */
	private String startId;
	
	private Integer offset = null;
	
	/**
	 * if calculate total
	 */
	private boolean calculateTotal = true;
	
	public QueryInfo(){
		
	}
	
	public boolean isCalculateTotal() {
		return calculateTotal;
	}

	public void setCalculateTotal(boolean calculateTotal) {
		this.calculateTotal = calculateTotal;
	}
	
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Integer getTotal() {
		return total;
	}

	public Integer getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public String getStartId() {
		return startId;
	}

	public void setStartId(String startId) {
		this.startId = startId;
	}
	
	public Integer getOffset(){
		if(this.offset != null){
			return this.offset;
		}
		return (this.curPage-1)*this.pageLimit;
	}
	
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
}
