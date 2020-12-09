#### 作业记录：

##### 一、设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。并在新结构在演示常见的增删改查操作。代码、sql和配置文件，上传到github。

项目见apache-shardingsphere-demo

p_order表结构

```mysql
CREATE TABLE `p_order_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `serial_no` bigint(20) NOT NULL COMMENT '订单号',
  `predict_sum` decimal(14,5) NOT NULL COMMENT '订单金额（单位：元）',
  `discounts_sum` decimal(14,5) DEFAULT NULL COMMENT '优惠金额（单位：元）',
  `logistics_sum` decimal(14,5) DEFAULT NULL COMMENT '运费金额（单位：元）',
  `actual_sum` decimal(14,5) DEFAULT NULL COMMENT '实付金额（单位：元）',
  `pay_method_id` bigint(20) DEFAULT NULL COMMENT '关联支付方式表',
  `logistics_id` bigint(20) DEFAULT NULL COMMENT '关联物流表',
  `order_state` int(2) NOT NULL COMMENT '订单状态（）',
  `logistics_state` int(2) NOT NULL COMMENT '物流状态（）',
  `operator_id` bigint(20) NOT NULL COMMENT '下单用户ID（关联user表ID）',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `operation_id` bigint(20) NOT NULL COMMENT '上传人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `change_time` datetime NOT NULL COMMENT '更新时间',
  `logistics_time` datetime DEFAULT NULL COMMENT '发货时间',
  `success_time` datetime DEFAULT NULL COMMENT '完成时间',
  `sign` int(2) NOT NULL COMMENT '逻辑删除标识（1：正常；2：删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2000003 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

serial_no 为偶数 进_0库，为奇数 进_1库

pay_method_id %16，根据结果 进对应的表

测试：

insert一条订单

测试类代码：

```java
@Test
	public void insertPOrder1() {
		POrder pOrder = new POrder();
		pOrder.setSerialNo(1L); //serial_no 为偶数 进_0库，为奇数 进_1库
		pOrder.setPredictSum(new BigDecimal("0"));
		pOrder.setDiscountsSum(new BigDecimal("0"));
		pOrder.setLogisticsSum(new BigDecimal("0"));
		pOrder.setActualSum(new BigDecimal("0"));
		pOrder.setPayMethodId(14L); //pay_method_id %16 根据结果进对应的表
		pOrder.setLogisticsId(0L);
		pOrder.setOrderState(0);
		pOrder.setLogisticsState(0);
		pOrder.setOperatorId(0L);
		pOrder.setRemark("");
		pOrder.setOperationId(0L);
		pOrder.setCreateTime(new Date());
		pOrder.setChangeTime(new Date());
		pOrder.setLogisticsTime(new Date());
		pOrder.setSuccessTime(new Date());
		pOrder.setSign(0);
		
		pOrderDao.insert(pOrder);
	}
```

日志：

```
2020-12-09 23:05:52.836  INFO 43099 --- [           main] o.a.s.c.config.log.ConfigurationLogger   : ShardingRuleConfiguration
bindingTables:
- p_order
defaultDatabaseStrategy:
  inline:
    algorithmExpression: ds$->{serial_no % 2}
    shardingColumn: serial_no
tables:
  p_order:
    actualDataNodes: ds$->{0..1}.p_order_$->{0..15}
    logicTable: p_order
    tableStrategy:
      inline:
        algorithmExpression: p_order_$->{pay_method_id % 16}
        shardingColumn: pay_method_id

2020-12-09 23:05:52.836  INFO 43099 --- [           main] o.a.s.c.config.log.ConfigurationLogger   : Properties
sql.show: 'true'

2020-12-09 23:05:53.340  INFO 43099 --- [           main] ApacheShardingsphereDemoApplicationTests : Started ApacheShardingsphereDemoApplicationTests in 2.311 seconds (JVM running for 3.286)

2020-12-09 23:05:54.189  INFO 43099 --- [           main] ShardingSphere-SQL                       : Rule Type: sharding
2020-12-09 23:05:54.189  INFO 43099 --- [           main] ShardingSphere-SQL                       : Logic SQL: insert into p_order (serial_no, predict_sum, discounts_sum, 
      logistics_sum, actual_sum, pay_method_id, 
      logistics_id, order_state, logistics_state, 
      operator_id, remark, operation_id, 
      create_time, change_time, logistics_time, 
      success_time, sign)
    values (?, ?, ?, 
      ?, ?, ?, 
      ?, ?, ?, 
      ?, ?, ?, 
      ?, ?, ?, 
      ?, ?)
2020-12-09 23:05:54.189  INFO 43099 --- [           main] ShardingSphere-SQL                       : SQLStatement: InsertSQLStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement@61394494, tablesContext=TablesContext(tables=[Table(name=p_order, alias=Optional.absent())], schema=Optional.absent())), columnNames=[serial_no, predict_sum, discounts_sum, logistics_sum, actual_sum, pay_method_id, logistics_id, order_state, logistics_state, operator_id, remark, operation_id, create_time, change_time, logistics_time, success_time, sign], insertValueContexts=[InsertValueContext(parametersCount=17, valueExpressions=[ParameterMarkerExpressionSegment(startIndex=290, stopIndex=290, parameterMarkerIndex=0), ParameterMarkerExpressionSegment(startIndex=293, stopIndex=293, parameterMarkerIndex=1), ParameterMarkerExpressionSegment(startIndex=296, stopIndex=296, parameterMarkerIndex=2), ParameterMarkerExpressionSegment(startIndex=306, stopIndex=306, parameterMarkerIndex=3), ParameterMarkerExpressionSegment(startIndex=309, stopIndex=309, parameterMarkerIndex=4), ParameterMarkerExpressionSegment(startIndex=312, stopIndex=312, parameterMarkerIndex=5), ParameterMarkerExpressionSegment(startIndex=322, stopIndex=322, parameterMarkerIndex=6), ParameterMarkerExpressionSegment(startIndex=325, stopIndex=325, parameterMarkerIndex=7), ParameterMarkerExpressionSegment(startIndex=328, stopIndex=328, parameterMarkerIndex=8), ParameterMarkerExpressionSegment(startIndex=338, stopIndex=338, parameterMarkerIndex=9), ParameterMarkerExpressionSegment(startIndex=341, stopIndex=341, parameterMarkerIndex=10), ParameterMarkerExpressionSegment(startIndex=344, stopIndex=344, parameterMarkerIndex=11), ParameterMarkerExpressionSegment(startIndex=354, stopIndex=354, parameterMarkerIndex=12), ParameterMarkerExpressionSegment(startIndex=357, stopIndex=357, parameterMarkerIndex=13), ParameterMarkerExpressionSegment(startIndex=360, stopIndex=360, parameterMarkerIndex=14), ParameterMarkerExpressionSegment(startIndex=370, stopIndex=370, parameterMarkerIndex=15), ParameterMarkerExpressionSegment(startIndex=373, stopIndex=373, parameterMarkerIndex=16)], parameters=[1, 0, 0, 0, 0, 14, 0, 0, 0, 0, , 0, 2020-12-09 23:05:53.536, 2020-12-09 23:05:53.536, 2020-12-09 23:05:53.536, 2020-12-09 23:05:53.536, 0])])
2020-12-09 23:05:54.189  INFO 43099 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: insert into p_order_14 (serial_no, predict_sum, discounts_sum, 
      logistics_sum, actual_sum, pay_method_id, 
      logistics_id, order_state, logistics_state, 
      operator_id, remark, operation_id, 
      create_time, change_time, logistics_time, 
      success_time, sign)
    values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ::: [1, 0, 0, 0, 0, 14, 0, 0, 0, 0, , 0, 2020-12-09 23:05:53.536, 2020-12-09 23:05:53.536, 2020-12-09 23:05:53.536, 2020-12-09 23:05:53.536, 0]

2020-12-09 23:05:54.247  INFO 43099 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2020-12-09 23:05:54.254  INFO 43099 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
2020-12-09 23:05:54.255  INFO 43099 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Shutdown initiated...
2020-12-09 23:05:54.256  INFO 43099 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Shutdown completed.
```

查询全部订单

selectAll测试类代码：

```java
@Test
	public void selectPOrder() {
		List<POrder> pOrderList = pOrderDao.selectAll();
		pOrderList.forEach(pOrder -> {
			System.out.println(pOrder);
		});
	}
```

日志：

```
2020-12-09 23:11:03.958  INFO 43250 --- [           main] o.a.s.c.config.log.ConfigurationLogger   : ShardingRuleConfiguration
bindingTables:
- p_order
defaultDatabaseStrategy:
  inline:
    algorithmExpression: ds$->{serial_no % 2}
    shardingColumn: serial_no
tables:
  p_order:
    actualDataNodes: ds$->{0..1}.p_order_$->{0..15}
    logicTable: p_order
    tableStrategy:
      inline:
        algorithmExpression: p_order_$->{pay_method_id % 16}
        shardingColumn: pay_method_id

2020-12-09 23:11:03.958  INFO 43250 --- [           main] o.a.s.c.config.log.ConfigurationLogger   : Properties
sql.show: 'true'

2020-12-09 23:11:04.405  INFO 43250 --- [           main] ApacheShardingsphereDemoApplicationTests : Started ApacheShardingsphereDemoApplicationTests in 2.0 seconds (JVM running for 2.824)

2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Rule Type: sharding
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Logic SQL: select * from p_order
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : SQLStatement: SelectSQLStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement@531ec978, tablesContext=TablesContext(tables=[Table(name=p_order, alias=Optional.absent())], schema=Optional.absent())), projectionsContext=ProjectionsContext(startIndex=7, stopIndex=7, distinctRow=false, projections=[ShorthandProjection(owner=Optional.absent())], columnLabels=[id, serial_no, predict_sum, discounts_sum, logistics_sum, actual_sum, pay_method_id, logistics_id, order_state, logistics_state, operator_id, remark, operation_id, create_time, change_time, logistics_time, success_time, sign]), groupByContext=org.apache.shardingsphere.sql.parser.relation.segment.select.groupby.GroupByContext@93501be, orderByContext=org.apache.shardingsphere.sql.parser.relation.segment.select.orderby.OrderByContext@11d4d979, paginationContext=org.apache.shardingsphere.sql.parser.relation.segment.select.pagination.PaginationContext@195580ba, containsSubquery=false)
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_0
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_1
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_2
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_3
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_4
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_5
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_6
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_7
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_8
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_9
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_10
2020-12-09 23:11:05.199  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_11
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_12
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_13
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_14
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_15
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_0
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_1
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_2
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_3
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_4
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_5
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_6
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_7
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_8
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_9
2020-12-09 23:11:05.200  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_10
2020-12-09 23:11:05.201  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_11
2020-12-09 23:11:05.201  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_12
2020-12-09 23:11:05.201  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_13
2020-12-09 23:11:05.201  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_14
2020-12-09 23:11:05.201  INFO 43250 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_15
POrder(id=2000003, serialNo=null, predictSum=null, discountsSum=null, logisticsSum=null, actualSum=null, payMethodId=null, logisticsId=null, orderState=null, logisticsState=null, operatorId=null, remark=, operationId=null, createTime=null, changeTime=null, logisticsTime=null, successTime=null, sign=0)
POrder(id=2000003, serialNo=null, predictSum=null, discountsSum=null, logisticsSum=null, actualSum=null, payMethodId=null, logisticsId=null, orderState=null, logisticsState=null, operatorId=null, remark=, operationId=null, createTime=null, changeTime=null, logisticsTime=null, successTime=null, sign=0)
POrder(id=2000004, serialNo=null, predictSum=null, discountsSum=null, logisticsSum=null, actualSum=null, payMethodId=null, logisticsId=null, orderState=null, logisticsState=null, operatorId=null, remark=, operationId=null, createTime=null, changeTime=null, logisticsTime=null, successTime=null, sign=0)
POrder(id=2000005, serialNo=null, predictSum=null, discountsSum=null, logisticsSum=null, actualSum=null, payMethodId=null, logisticsId=null, orderState=null, logisticsState=null, operatorId=null, remark=, operationId=null, createTime=null, changeTime=null, logisticsTime=null, successTime=null, sign=0)

2020-12-09 23:11:05.301  INFO 43250 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2020-12-09 23:11:05.306  INFO 43250 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
2020-12-09 23:11:05.306  INFO 43250 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Shutdown initiated...
2020-12-09 23:11:05.307  INFO 43250 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Shutdown completed.
```



##### 二、基于hmily TCC或ShardingSphere的Atomikos XA实现一个简单的分布式事务应用demo（二选一），提交到github。

使用hmily TCC实现一个简单的分布式事务应用demo。

代码详情见learn-hmily-tcc-demo

模拟一个简单的转账操作，张三（demo1）转账给李四（demo2），项目learn-hmily-tcc-demo1 操作张三账户，连接数据库demo1，项目learn-hmily-tcc-demo2 操作李四账户，连接数据库demo2。

同时创建hmily数据库，用于存储hmily框架记录的数据。

```mysql
CREATE DATABASE `hmily` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'
```

创建demo1数据库，并初始化张三数据

```mysql
CREATE DATABASE `demo1` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
```

```mysql
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info` (
`id` BIGINT ( 20 ) NOT NULL AUTO_INCREMENT,
`account_name` VARCHAR ( 100 ) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '户主姓名',
`account_no` VARCHAR ( 100 ) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '银行卡号',
`account_password` VARCHAR ( 100 ) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '帐户密码',
`account_balance` DOUBLE NULL DEFAULT NULL COMMENT '帐户余额',
PRIMARY KEY ( `id` ) USING BTREE 
) ENGINE = INNODB AUTO_INCREMENT = 5 CHARACTER 
SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;
INSERT INTO `account_info` VALUES ( 2, '张三的账户', '1', '', 10000 );
```

创建demo2数据库，并初始化李四数据

```mysql
CREATE DATABASE `demo2` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
```

```mysql
CREATE TABLE `account_info` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`account_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '户主姓名',
`account_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '银行卡号',
`account_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT'帐户密码',
`account_balance` double NULL DEFAULT NULL COMMENT '帐户余额',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;
INSERT INTO `account_info` VALUES (3, '李四的账户', '2', NULL, 0);
```

每个数据库都创建try、confirm、cancel三张日志表：

```mysql
CREATE TABLE `local_try_log` (
`tx_no` varchar(64) NOT NULL COMMENT '事务id',
`create_time` datetime DEFAULT NULL,
PRIMARY KEY (`tx_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `local_confirm_log` (
`tx_no` varchar(64) NOT NULL COMMENT '事务id',
`create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `local_cancel_log` (
`tx_no` varchar(64) NOT NULL COMMENT '事务id',
`create_time` datetime DEFAULT NULL,
PRIMARY KEY (`tx_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

测试：

1、启动discover-server服务注册中心

2、测试张三向李四转账成功：

访问http://localhost:56081/demo1/transfer?amount=1 返回true，

日志记录：

<img src="/Users/miwang/javaWeb/git-workspace/JAVA-000/Week_08/WX20201209-221627@2x.png" alt="WX20201209-221627@2x"  />

![WX20201209-221715@2x](/Users/miwang/javaWeb/git-workspace/JAVA-000/Week_08/WX20201209-221715@2x.png)

![WX20201209-222416@2x](/Users/miwang/javaWeb/git-workspace/JAVA-000/Week_08/WX20201209-222416@2x.png)

![WX20201209-222427@2x](/Users/miwang/javaWeb/git-workspace/JAVA-000/Week_08/WX20201209-222427@2x.png)



3、手动制造异常回滚

访问http://localhost:56081/demo1/transfer?amount=2 

![WX20201209-222530@2x](/Users/miwang/javaWeb/git-workspace/JAVA-000/Week_08/WX20201209-222530@2x.png)

![WX20201209-222540@2x](/Users/miwang/javaWeb/git-workspace/JAVA-000/Week_08/WX20201209-222540@2x.png)

数据库中两个账户的account_balance字段值不变