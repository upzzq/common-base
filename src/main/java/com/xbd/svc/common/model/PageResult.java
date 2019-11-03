package com.xbd.svc.common.model;

import lombok.Data;

import java.util.List;

/**
 * 分页结果返回对象
 * @param <T>
 */
@Data
public class PageResult<T> {

    /**
     * 总数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 分页后的数据
     */
    private List<T> data;

    public PageResult() {}

    public PageResult(Long total, List<T> data) {
        this.total = total;
        this.data = data;
    }

    public PageResult(Long total, Integer totalPage, List<T> data) {
        this.total = total;
        this.totalPage = totalPage;
        this.data = data;
    }
}
