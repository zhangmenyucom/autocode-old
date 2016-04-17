package com.taylor.api.common.util;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = -3731020601956390148L;
	
	/**
	 * 当前页
	 */
	private int currPage = 1;
	/**
	 * 每页记录数
	 */
	private int pageSize = 10;
	/**
	 * 总记录数
	 */
	private int count;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 是否有前一页
	 */
	private Boolean hasPre;
	/**
	 * 是否有下一页
	 */
	private Boolean hasNext;
	
	/**
	 * 页面列表List
	 */
	private List<T> resultList;
	
	/**
	 * 分页开始位
	 */
	private int offset;
	
	/**
	 * 分页Html
	 */
	private String pageStr;
	
	/**
	 * seo友好的html
	 */
	private String pageStr4Seo;
	
	/**
	 * 分页pre/next Lab
	 */
	private String pageLab;
	
	/**
	 * 分页取模 向前和向后各取的长度
	 */
	private int disModPage = 3;
	
	/**
	 *	点击页面跳转的url 
	 */
	private String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * 不处理的总分页数
	 */
	private int disTotalLen = 10;
	
	private String jumpCall = "jump";//跳转调用的Js函数如:gotoPage或jump

	public Page() {
	}
	
	public Page(int count, int pageSize, int currPage) {
		this.count = count;
		this.pageSize = pageSize;
		this.currPage = currPage;
		
		getTotalPage();
	}
	
	public Page(int count, int pageSize, int currPage, int disTotalLen, int disModPage) {
		this.count = count;
		this.pageSize = pageSize;
		this.currPage = currPage;
		
		getTotalPage();
		
		this.disTotalLen = disTotalLen;
		this.disModPage = disModPage;
	}
	
	/**
	 * @notes:分页浮动Lab 上一页/下一页	切换效果
	 *
	 * @author	taylor
	 * 2014-5-9	下午4:06:50
	 */
	public String getPageLab() {
		
		String nextStr = "";
		if (getHasNext()) {
			nextStr = "<i class=\"icon-nextRight\" onclick=\"" + jumpCall  + "(" + (this.currPage + 1) + ")\"></i>";
		} else {
			nextStr = "<i class=\"icon-nextRight2\"></i>";
		}
		
		String preStr = "";
		if (getHasPre()) {
			preStr = "<i class=\"icon-prevLeft2\" onclick=\"" + jumpCall  + "(" + (this.currPage - 1) + ")\"></i>";
		} else {
			preStr = "<i class=\"icon-prevLeft\"></i>";
		}
		
		pageLab = preStr + " " + nextStr;
		return pageLab;
	}

	public void setPageLab(String pageLab) {
		this.pageLab = pageLab;
	}

	/**
	 * @notes:向前取disModPage位
	 *
	 * @author	taylor
	 * 2014-4-18	下午3:37:17
	 */
	public String getPageStr() {
		/**
		 * 具体根据前台来定制页面显示的Html，并配合相应的js函数使用
		 * 如:gotoPage或jumpPage
		 */
		totalPage = 0 == totalPage ? getTotalPage():totalPage;
		StringBuilder html = new StringBuilder("<div id=\"paging\">");
		if (totalPage > disTotalLen) {//总页数大于10根据当前页处理
			if (currPage + disModPage > totalPage) {//当前页数 + 向后长度 > 总页数(只显示前面的...)
				//1...993 994 995 996 (997) 998 999
				
				if (getHasPre()) {//上一页
					html.append("<a href=\"javascript:" + jumpCall + "(" + (currPage - 1) + ");\"><i class=\"prev\"></i></a>");
				}
				//首页
				html.append("<a href=\"javascript:" + jumpCall + "(1);\">1</a>");
				//...
				html.append("<a class=\"morebox\" href=\"javascript:void(0);\"><i class=\"more\"></i></a>");
				/**
				 * 取后面页码标签
				 */
				int upStart = currPage - disModPage - (currPage + disModPage - totalPage);//向前的开始
				String labList = getPageLabList(upStart, totalPage);
				html.append(labList);
				
				if (getHasNext()) {//下一页
					html.append("<a href=\"javascript:" + jumpCall + "(" + (currPage + 1) + ");\"><i class=\"next\"></i></a>");
				}
			} else if (currPage + disModPage < disTotalLen) {//只显示后面的...
				//1 2 3 4 5 (6) 7 8 9 10...999
				
				if (getHasPre()) {//上一页
					html.append("<a href=\"javascript:" + jumpCall + "(" + (currPage - 1) + ");\"><i class=\"prev\"></i></a>");
				}
				/**
				 * 取前disTotalLen条的页码标签
				 */
				String labList = getPageLabList(1, disTotalLen); 
				html.append(labList);
				//...
				html.append("<a class=\"morebox\" href=\"javascript:void(0);\"><i class=\"more\"></i></a>");
				//尾页
				html.append("<a href=\"javascript:" + jumpCall + "(" + totalPage + ");\">"+ totalPage +"</a>");
				
				if (getHasNext()) {//下一页
					html.append("<a href=\"javascript:" + jumpCall + "(" + (currPage + 1) + ");\"><i class=\"next\"></i></a>");
				}
				
			} else {
				//1... 5 6 7 (8) 9 10 11...999
				if (getHasPre()) {//上一页
					html.append("<a href=\"javascript:" + jumpCall + "(" + (currPage - 1) + ");\"><i class=\"prev\"></i></a>");
				}
				//首页
				html.append("<a href=\"javascript:" + jumpCall + "(1);\">1</a>");
				//...
				html.append("<a class=\"morebox\" href=\"javascript:void(0);\"><i class=\"more\"></i></a>");
				/**
				 * 取currPage前后disModPage条一起的页码标签
				 */
				String labList = getPageLabList(currPage - disModPage, currPage + disModPage);
				html.append(labList);
				//...
				html.append("<a class=\"morebox\" href=\"javascript:void(0);\"><i class=\"more\"></i></a>");
				//尾页
				html.append("<a href=\"javascript:" + jumpCall + "(" + totalPage + ");\">" + totalPage + "</a>");
				
				if (getHasNext()) {//下一页
					html.append("<a href=\"javascript:" + jumpCall + "(" + (currPage + 1) + ");\"><i class=\"next\"></i></a>");
				}
				
			}
		} else {//总页数小于设置的全显长度，则全显

			if (getHasPre()) {//上一页
				html.append("<a href=\"javascript:" + jumpCall + "(" + (currPage - 1) + ");\"><i class=\"prev\"></i></a>");
			}

			html.append(getPageLabList(1, totalPage));
			
			if (getHasNext()) {//下一页
				html.append("<a href=\"javascript:" + jumpCall + "(" + (currPage + 1) + ");\"><i class=\"next\"></i></a>");
			}
		}
		
		if (totalPage > disTotalLen) {//当总页数大于 不处理的总分页数 即>10即会出现跳转到指定页操作功能
			html.append("<span>跳转至<input type=\"text\" id=\"jumpPage\" />页<button type=\"button\" value=\"${currentPage}\" onclick=\"javascript:jump($('#jumpPage').val());\">确定</button></span>");
		}
		
		html.append("</div>");
		pageStr = html.toString();
		return pageStr;
	}
	
	/**
	 * @notes:获取从start到end的分页Html内容（含当前页处理）
	 *
	 * @param start		分页标签拼写开始位
	 * @param end		分页标签拼写结束位
	 * @author	taylor
	 * 2014-4-18	下午4:43:55
	 */
	private String getPageLabList(int start, int end) {
		StringBuilder pageLabSb = new StringBuilder(0);
		for (int i = start; i <= end; i++) {
			if (i == currPage) {
				pageLabSb.append("<a class=\"active\" href=\"javascript:void(0);\">" + i + "</a>");
			} else {
				pageLabSb.append("<a href=\"javascript:" + jumpCall + "(" + i + ");\">" + i + "</a>");
			}
		}
		return pageLabSb.toString();
	}
	
	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}
	
	/**
	 * @notes:向前取disModPage位
	 *
	 * @author	taylor
	 * 2014-4-18	下午3:37:17
	 */
	public String getPageStr4Seo() {
		if(StringUtils.isNotBlank(pageStr4Seo)) return pageStr4Seo;
		
		if (getTotalPage() < 2) {
			return "";
		}
		
		/**
		 * 具体根据前台来定制页面显示的Html，并配合相应的js函数使用
		 * 如:gotoPage或jumpPage
		 */
		totalPage = 0 == totalPage ? getTotalPage():totalPage;
		StringBuilder html = new StringBuilder("<div id=\"paging\">");
		if (totalPage > disTotalLen) {//总页数大于10根据当前页处理
			if (currPage + disModPage > totalPage) {//当前页数 + 向后长度 > 总页数(只显示前面的...)
				//1...993 994 995 996 (997) 998 999
				
				if (getHasPre()) {//上一页
					html.append("<a  href=\"" + this.url + "&currentPage=" + (currPage - 1) + "\"><i class=\"prev\"></i></a>");
				}
				//首页
				html.append("<a  href=\"" + this.url + "&currentPage=1\">1</a>");
				//...
				html.append("<a class=\"morebox\" href=\"javascript:void(0);\"><i class=\"more\"></i></a>");
				/**
				 * 取后面页码标签
				 */
				int upStart = currPage - disModPage - (currPage + disModPage - totalPage);//向前的开始
				String labList = getPageLabList4Seo(upStart, totalPage);
				html.append(labList);
				
				if (getHasNext()) {//下一页
					html.append("<a  href=\"javascript:" + jumpCall + "(" + (currPage + 1) + ");\"><i class=\"next\"></i></a>");
				}
			} else if (currPage + disModPage < disTotalLen) {//只显示后面的...
				//1 2 3 4 5 (6) 7 8 9 10...999
				
				if (getHasPre()) {//上一页
					html.append("<a  href=\"" + this.url + "&currentPage=" + (currPage - 1) + "\"><i class=\"prev\"></i></a>");
				}
				/**
				 * 取前disTotalLen条的页码标签
				 */
				String labList = getPageLabList4Seo(1, disTotalLen); 
				html.append(labList);
				//...
				html.append("<a class=\"morebox\" href=\"javascript:void(0);\"><i class=\"more\"></i></a>");
				//尾页
				html.append("<a  href=\"" + this.url + "&currentPage=" + totalPage + "\">" + totalPage + "</a>");
				
				if (getHasNext()) {//下一页
					html.append("<a  href=\"" + this.url + "&currentPage=" + (currPage + 1) + "\"><i class=\"next\"></i></a>");
				}
				
			} else {
				//1... 5 6 7 (8) 9 10 11...999
				if (getHasPre()) {//上一页
					html.append("<a  href=\"" + this.url + "&currentPage=" + (currPage - 1) + "\"><i class=\"prev\"></i></a>");
				}
				//首页
				html.append("<a  href=\"" + this.url + "&currentPage=1\">1</a>");
				//...
				html.append("<a class=\"morebox\" href=\"javascript:void(0);\"><i class=\"more\"></i></a>");
				/**
				 * 取currPage前后disModPage条一起的页码标签
				 */
				String labList = getPageLabList4Seo(currPage - disModPage, currPage + disModPage);
				html.append(labList);
				//...
				html.append("<a class=\"morebox\" href=\"javascript:void(0);\"><i class=\"more\"></i></a>");
				//尾页
				html.append("<a  href=\"" + this.url + "&currentPage=" + totalPage + "\">" + totalPage + "</a>");
				
				if (getHasNext()) {//下一页
					html.append("<a  href=\"" + this.url + "&currentPage=" + (currPage + 1) + "\"><i class=\"next\"></i></a>");
				}
				
			}
		} else {//总页数小于设置的全显长度，则全显

			if (getHasPre()) {//上一页
				html.append("<a  href=\"" + this.url + "&currentPage=" + (currPage - 1) + "\"><i class=\"prev\"></i></a>");
			}

			html.append(getPageLabList4Seo(1, totalPage));
			
			if (getHasNext()) {//下一页
				html.append("<a  href=\"" + this.url + "&currentPage=" + (currPage + 1) + "\"><i class=\"next\"></i></a>");
			}
		}
		
		if (totalPage > disTotalLen) {//当总页数大于 不处理的总分页数 即>10即会出现跳转到指定页操作功能
			html.append("<span>跳转至<input type=\"text\" id=\"jumpPage\" />页<button type=\"button\" value=\"${currentPage}\" onclick=\"javascript:var pnum = $('#jumpPage').val();" +
					"" +
					"window.location.href='"+this.url+"&currentPage='+pnum+''\">确定</button></span>");
		}
		
		html.append("</div>");
		pageStr4Seo = html.toString();
		return pageStr4Seo;
	}
	
	/**
	 * @notes:获取从start到end的分页Html内容（含当前页处理）seo优化版
	 *
	 * @param start		分页标签拼写开始位
	 * @param end		分页标签拼写结束位
	 * @author	taylor
	 * 2014-4-18	下午4:43:55
	 */
	private String getPageLabList4Seo(int start, int end) {
		StringBuilder pageLab = new StringBuilder(0);
		for (int i = start; i <= end; i++) {
			if (i == currPage) {
				pageLab.append("<a class=\"active\" href=\"javascript:void(0);\">" + i + "</a>");
			} else {
				pageLab.append("<a href=\"" + this.url + "&currentPage=" + i + "\">" + i + "</a>");
			}
		}
		return pageLab.toString();
	}
	
	public void setPageStr4Seo(String pageStr4Seo) {
		this.pageStr4Seo = pageStr4Seo;
	}
	
	public int getOffset() {
		if (currPage < 2) {
			offset = 0;
		} else {
			offset = (currPage-1) * pageSize;
		}
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotalPage() {
		if (totalPage < 1) {
			if (count % pageSize > 0) {
				totalPage = count / pageSize + 1;
			} else {
				totalPage = count / pageSize;
			}
		}
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public Boolean getHasPre() {//是否还有上一页
		if (currPage > 1) {
			hasPre = true;
		} else {
			hasPre = false;
		}
		return hasPre;
	}

	public void setHasPre(Boolean hasPre) {
		this.hasPre = hasPre;
	}

	public Boolean getHasNext() {//是否还有下一页
		if (currPage > 0 && currPage < getTotalPage()) {
			hasNext = true;
		} else {
			hasNext = false;
		}
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	public int getDisModPage() {
		return disModPage;
	}

	public void setDisModPage(int disModPage) {
		this.disModPage = disModPage;
	}

	public int getDisTotalLen() {
		return disTotalLen;
	}

	public void setDisTotalLen(int disTotalLen) {
		this.disTotalLen = disTotalLen;
	}

	public String getJumpCall() {
		return jumpCall;
	}

	public void setJumpCall(String jumpCall) {
		this.jumpCall = jumpCall;
	}

	
	public static void main(String[] args) {
		Page<Object> page = new Page<Object>();
		page.setCount(1000);
		page.setPageSize(10);
	}
}
