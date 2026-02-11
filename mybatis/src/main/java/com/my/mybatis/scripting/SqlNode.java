package com.my.mybatis.scripting;

import java.util.Map;

/**
 * SQL节点
 */
public interface SqlNode {

    void apply(DynamicContext context);

}
