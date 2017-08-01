package org.starlightfinancial.deductiongateway.utility;

import java.util.List;

/**
 * Created by senlin.deng on 2017-07-31.
 */
public class PageBean {
    private Long total = 0L;
    private List<?> rows;
    private Integer pageNumber = 1;
    private Integer pageSize = 20;
    private String name;
    private String order = "asc";

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }



}
