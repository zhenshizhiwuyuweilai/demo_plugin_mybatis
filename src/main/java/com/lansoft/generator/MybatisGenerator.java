package com.lansoft.generator;

import com.lansoft.model.MybatisConfig;

/**
 * mybatis代码生成接口
 *
 * @author guowd
 * @date 2021/6/6  2:32
 */
public interface MybatisGenerator {

    /**
     * 生成mybatis代码
     *
     * @param config mybatis配置
     */
    void generatorCode(MybatisConfig config);
}
