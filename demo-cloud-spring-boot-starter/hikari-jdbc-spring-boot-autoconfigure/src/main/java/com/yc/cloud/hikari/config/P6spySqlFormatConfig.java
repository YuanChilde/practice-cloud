package com.yc.cloud.hikari.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6spySqlFormatConfig implements MessageFormattingStrategy {

    /**
     * 过滤掉定时任务的 SQL
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return StrUtil.isNotBlank(sql) ? DateUtil.now()
                + " | 耗时 " + elapsed + " ms | SQL 语句：" + StrUtil.LF + sql.replaceAll("[\\s]+", StrUtil.SPACE) + ";" : StrUtil.EMPTY;
    }
}
