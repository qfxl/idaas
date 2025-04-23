package com.zbycorp.common;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 抽象实例工厂类
 *
 * @author xuyonghong
 * @date 2025-04-09 17:23
 **/
@RequiredArgsConstructor
public abstract class AbstractInstanceFactory<Key, Instance> {

    /**
     * IoC DI 容器自动注入
     */
    private final List<Instance> instanceList;

    /**
     * 实例的kv映射，用于快速查询实例, O(1)
     */
    private Map<Key, Instance> instanceMap;

    /**
     * 每个实例惟一映射的类型执行函数
     * 必须由实现类实现, 只有实现类知道实例如何映射实例类型
     *
     * @return 类型执行函数
     */
    protected abstract Function<? super Instance, ? extends Key> keyFunction();

    /**
     * PostConstruct在依赖注入完成后进行
     */
    @PostConstruct
    final void initInstanceMap() {
        // 构造实例Map
        instanceMap = instanceList.stream().collect(
                Collectors.toMap(this.keyFunction(), Function.identity()));
    }

    /**
     * 获取实例对象
     * 实现类可以重写本方法
     *
     * @param k
     * @return 实例
     */
    public Instance getInstance(Key k) {
        return instanceMap.get(k);
    }
}