package org.eu.yaesakura.simpleauth.framework.domain;

import lombok.Data;

import java.util.List;

/**
 * @author YaeSakura
 */

@Data
public class PageResult<T> {
    // 当前页
    private Long current;
    // 分页大小
    private Long pageSize;
    // 总条数
    private Long total;
    // 分页数据
    private List<T> data;

    public PageResult (Long current, Long pageSize, Long total) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
    }
}
