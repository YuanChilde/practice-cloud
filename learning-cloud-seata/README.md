### 简要说明
1. 先创建业务demo数据库，执行seata-demo-db.sql
2. 创建nacos配置文件，在nacos数据库中执行nacos-seata.sql,并按实际情况修改application.yml、dubbo.yml和seata-server-dg.yml等文件中的数据库和nacos的连接信息
3. 按实际情况修改seata-account-service、seata-order-service、seata-stock-service和seata-business项目里bootstrap.yml的nacos的连接信息
4. 启动服务：seata-account-service、seata-order-service、seata-stock-service和seata-business
5. 入口项目seata-business，http://127.0.0.1:7010/business/dubbo/buy，post参数
   {
   "userId": 1,
   "commodityCode": "C201901140001",
   "count": 2,
   "amount": 4
   }