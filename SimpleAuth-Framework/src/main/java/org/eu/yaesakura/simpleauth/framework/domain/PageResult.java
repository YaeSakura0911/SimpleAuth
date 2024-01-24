package org.eu.yaesakura.simpleauth.framework.domain;

import lombok.Data;

import java.util.List;

/**
 * @author YaeSakura
 */

@Data
public class PageResult<T> {
    // 当前页
    private Integer current;
    // 分页大小
    private Integer pageSize;
    // 总条数
    private Integer total;
    // 分页数据
    private List<T> data;

    public PageResult (Integer current, Integer pageSize, Integer total) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
    }
}
