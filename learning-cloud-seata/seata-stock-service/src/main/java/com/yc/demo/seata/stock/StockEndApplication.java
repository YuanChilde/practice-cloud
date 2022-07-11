package com.yc.demo.seata.stock;


import cn.hutool.core.util.StrUtil;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

@Log4j2
@EnableDiscoveryClient
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.yc.demo.seata")
@MapperScan("com.yc.demo.seata.**.mapper")
public class StockEndApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StockEndApplication.class);
        ConfigurableApplicationContext run = app.run(args);
        //日志框架的优先级被识别
        try {
            ConfigurableEnvironment environment = run.getEnvironment();
            String serverIp = environment.getProperty("spring.cloud.client.ip-address");
            String serverPort = environment.getProperty("server.port");
            log.info("app started. http://" + serverIp + ":" + serverPort + StrUtil.blankToDefault(environment.getProperty("server" +
                    ".servlet" + ".context-path"), ""));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        //LoggerUtil.pointLog.info("我来测试埋点==============================");
    }
}