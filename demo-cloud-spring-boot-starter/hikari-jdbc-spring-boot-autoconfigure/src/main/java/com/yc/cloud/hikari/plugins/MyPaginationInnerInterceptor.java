package com.yc.cloud.hikari.plugins;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import lombok.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.function.Predicate;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class MyPaginationInnerInterceptor extends PaginationInnerInterceptor {

	private final static String[] KEYWORDS = { "master", "truncate", "insert", "select", "delete", "update", "declare",
			"alter", "drop", "sleep", "extractvalue", "concat" };

	/**
	 * 数据库类型
	 * <p>
	 * 查看 {@link #findIDialect(Executor)} 逻辑
	 */
	private DbType dbType;

	/**
	 * 方言实现类
	 * <p>
	 * 查看 {@link #findIDialect(Executor)} 逻辑
	 */
	private IDialect dialect;

	public MyPaginationInnerInterceptor(DbType dbType) {
		this.dbType = dbType;
	}

	public MyPaginationInnerInterceptor(IDialect dialect) {
		this.dialect = dialect;
	}

	@Override
	public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
			ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
		IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
		// size 小于 0 直接设置为 0 , 即不查询任何数据
		if (null != page && page.getSize() < 0) {
			page.setSize(0);
		}
		if (page != null && CollUtil.isNotEmpty(page.orders())) {
			page.orders().removeIf(sqlInjectPredicate());
		}
		super.beforeQuery(executor, ms, page, rowBounds, resultHandler, boundSql);
	}

	private Predicate<OrderItem> sqlInjectPredicate() {
		return o -> {
			for (String keyword : KEYWORDS) {
				if (StrUtil.containsIgnoreCase(o.getColumn(), keyword)) {
					return true;
				}
			}
			return false;
		};
	}
}
