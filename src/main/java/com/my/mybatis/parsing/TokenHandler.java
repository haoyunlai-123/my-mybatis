package com.my.mybatis.parsing;

import java.sql.SQLException;

/**
 * 标记处理器
 */
public interface TokenHandler {

    /**
     * 处理标记
     * @param token 参数名称
     * @return 解析后的替代内容 name -> ?
     */
    String handleToken(String token);

}
