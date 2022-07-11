package com.yc.cloud.hikari;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;

import com.yc.cloud.hikari.config.MybatisPlusMetaObjectHandler;
import com.yc.cloud.hikari.plugins.MyPaginationInnerInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "hikari", name = "jdbc-url")
public class MybatisAutoConfiguration {

	/**
	 * 分页插件, 对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		interceptor.addInnerInterceptor(new MyPaginationInnerInterceptor());
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
		interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
		return interceptor;
	}

	/**
	 * 自定义公共字段自动注入
	 */
	@Bean
	public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
		return new MybatisPlusMetaObjectHandler();
	}


	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "hikari")
	public HikariDataSource dataSource() {
		HikariDataSource druidDataSource = new CryptDataSource();
		return druidDataSource;
	}

	static class CryptDataSource extends HikariDataSource {
		/**
		 * 密匙 8的倍数
		 */
		private final static String PKEY = "rd.yc.co";

		private String passwordDis;

		@Override
		public String getPassword() {
			String encPassword = super.getPassword();
			//  密文解密，解密方法可以修改
			String key = HexUtil.encodeHexStr(PKEY);
			SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes());
			passwordDis = aes.decryptStr(encPassword, CharsetUtil.CHARSET_UTF_8);
			return passwordDis;
		}

		public static void main(String[] args) {
			//明文
			String content = "123456";

			String key = HexUtil.encodeHexStr(PKEY);
			//构建
			SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes());

			//加密为16进制表示
			String encryptHex = aes.encryptHex(content);
			System.out.println("密文:" + encryptHex);
			//解密为字符串
			String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
			System.out.println("明文:" + decryptStr);
		}
	}
}
