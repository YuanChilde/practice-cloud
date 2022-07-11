### 简要说明
1. 先创建业务demo数据库，执行seata-demo-db.sql
2. 创建nacos配置文件，在nacos数据库中执行nacos-seata.sql,并按实际情况修改application.yml、dubbo.yml和seata-server-dg.yml等文件中的域名地址信息
3. 启动account-service、order-service、stock-service和business四个微服务
4. api入口business项目，http://127.0.0.1:7010/business/dubbo/buy，post参数
   {
   "userId": 1,
   "commodityCode": "C201901140001",
   "count": 2,
   "amount": 4
   }