package com.overthinker.cloud.common.core.constants;

/**
 * 分页相关常量类
 * 定义分页查询中使用的默认参数和限制值
 *
 * @author overthinker
 */
public class PageConst {

    /** 默认页码 */
    public static final int DEFAULT_PAGE_NUM = 1;

    /** 默认每页记录数 */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /** 最大每页记录数 */
    public static final int MAX_PAGE_SIZE = 100;

    /** 每页记录数选项 */
    public static final int[] PAGE_SIZE_OPTIONS = {10, 20, 30, 50, 100};

    /** 分页查询最大偏移量 */
    public static final long MAX_OFFSET = 10000L;

    /** 页码参数名称 */
    public static final String PAGE_NUM_PARAM = "pageNum";

    /** 每页大小参数名称 */
    public static final String PAGE_SIZE_PARAM = "pageSize";

    /** 排序参数名称 */
    public static final String ORDER_PARAM = "orderBy";

    /** 排序方向参数名称 */
    public static final String ORDER_TYPE_PARAM = "orderType";

    /** 升序排序方向 */
    public static final String ORDER_ASC = "asc";

    /** 降序排序方向 */
    public static final String ORDER_DESC = "desc";

    /** 默认排序字段 */
    public static final String DEFAULT_ORDER_FIELD = "createTime";

    /** 默认排序方向 */
    public static final String DEFAULT_ORDER_TYPE = ORDER_DESC;

    private PageConst() {
    }
}
