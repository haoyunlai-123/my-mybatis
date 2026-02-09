package com.my.mybatis.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 插件拦截器责任链
 */
public class InterceptorChain {

    List<Interceptor> interceptors = new ArrayList<>();

    /**
     * 得到多层代理对象
     * @param target 被代理对象
     * @return  最终多层代理对象
     */
    public Object pluginAll(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public List<Interceptor> getInterceptors() {
        // 返回一个不可修改的列表，防止外部修改拦截器列表
        return Collections.unmodifiableList(interceptors);
    }

}
