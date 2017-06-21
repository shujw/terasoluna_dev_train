package jp.co.tutorial.app.customer;

import java.io.Serializable;
import java.util.List;

/**
 * 顧客情報のパラメータプロパティを持つ Form.
 *
 * @author intra-mart
 */
public class CustomerListForm implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** 顧客リスト. */
    private List<CustomerForm> customerList;

    /** ページ番号. */
    private String page;

    /** ソートキー. */
    private String sortName;

    /** ソート順. */
    private String sortOrder;

    /** 1ページに表示する行数. */
    private String rowNum;

    /**
     * 1ページに表示する行数のリスト. 表示する行のリストをカンマ区切りで指定
     */
    private String pagerRowList;

    public List<CustomerForm> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<CustomerForm> customerList) {
        this.customerList = customerList;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }

    public String getPagerRowList() {
        return pagerRowList;
    }

    public void setPagerRowList(String pagerRowList) {
        this.pagerRowList = pagerRowList;
    }

}
