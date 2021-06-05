package com.lansoft.constant;

import com.lansoft.generator.MybatisGenerator;
import com.lansoft.generator.MybatisPlusGenerator;
import com.lansoft.generator.MybatisPlusGenerator2;
import com.lansoft.generator.TkMybatisGenerator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author guowd
 * @date 2021/6/6  2:05
 */
public class MybatisConstant {
    public static final Map<String, MybatisGenerator> PLUGIN_TYPE = new LinkedHashMap();
    public static final String CONFIG_PARAM = "CustomMybatisConfigData";

    public MybatisConstant() {
    }

    static {
        PLUGIN_TYPE.put("Mybatis", new TkMybatisGenerator(false));
        PLUGIN_TYPE.put("通用Mapper", new TkMybatisGenerator(true));
        PLUGIN_TYPE.put("MybatisPlus", new MybatisPlusGenerator());
    }
}
