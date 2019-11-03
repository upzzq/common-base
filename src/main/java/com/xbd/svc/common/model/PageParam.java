package com.xbd.svc.common.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 分页请求参数
 */
@Data
public class PageParam {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer pageNum = 1;

    /**
     * 每页的数量
     */
    @ApiModelProperty(value = "每页的数量")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String sortBy;

    /**
     * 升序/降序
     */
    @ApiModelProperty(value = "true:升序/false:降序")
    private Boolean desc;

}
