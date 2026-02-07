package com.my.mybatis.parsing;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;

/**
 * 解析sql: select * from t_user where id = #{id} and name = #{name}
 * ==> select * from t_user where id = ? and name = ?
 */
@AllArgsConstructor
public class GenericTokenParser {

    private String openToken; // 开始标记: #{
    private String closeToken; // 结束标记: }

    private String parse(String text) {
        if (StrUtil.isBlank(text)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        char[] textChars = text.toCharArray();
        int offset = 0;
        // select * from t_user where id = #{id} and name = #{name}
        int start = text.indexOf(openToken);
        while (start >= 0) {
            int end = text.indexOf(closeToken, start);
            if (end >= 0) {
                result.append(textChars, offset, start - offset);
                offset = start + openToken.length();
                String paramName = new String(textChars, offset, end - offset);
                System.out.println(paramName);
                result.append("?");
                offset = end + closeToken.length();
            } else {
                result.append(textChars, offset, text.length() - offset);
                offset =  text.length();
            }
            start = text.indexOf(openToken, offset);
        }

        if (offset < text.length()) {
            result.append(text.substring(offset));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}");
        String parse = genericTokenParser.parse("select * from t_user where id = #{id} and name = #{name");
        System.out.println(parse);

    }
}
