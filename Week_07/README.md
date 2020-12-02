#### 作业记录：

##### 一、按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。

p_order表结构

```mysql
CREATE TABLE `p_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `serial_no` varchar(32) NOT NULL COMMENT '订单号',
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
) ENGINE=InnoDB AUTO_INCREMENT=1000003 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

方法一：JDBC拼接insert语句，插入100w条数据，执行用时1267815ms

```java
public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(Constants.jdbcUrl, Constants.jdbcUsername, Constants.jdbcPassword);
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 1000000; i++) {
            String insert = "INSERT INTO `p_order`(`serial_no`, `predict_sum`, `discounts_sum`, `logistics_sum`, `actual_sum`, `pay_method_id`, `logistics_id`," +
                    " `order_state`, `logistics_state`, `operator_id`, `remark`, `operation_id`, `create_time`, `change_time`, `logistics_time`, `success_time`, `sign`)" +
                    " VALUES ("+i+", '11', '0.1', '0.1', '10.8', 1, 1, 0, 0, 1, '测试订单', 1, now(), now(), now(), now(), 1);";
            Statement insertStatement = connection.createStatement();
            insertStatement.execute(insert);
        }
        connection.close();
        System.out.println("执行用时"+ (System.currentTimeMillis() - start)+"ms");
}
```



方法二：使用存储过程，插入100w条数据，执行用时17147ms

```mysql
CREATE DEFINER=`root`@`localhost` PROCEDURE `gzqtest`()
begin
declare i int default 0;
set i=0;
start transaction;
while i<1000000 do
INSERT INTO `p_order`(`serial_no`, `predict_sum`, `discounts_sum`, `logistics_sum`, `actual_sum`, `pay_method_id`, `logistics_id`,
    `order_state`, `logistics_state`, `operator_id`, `remark`, `operation_id`, `create_time`, `change_time`, `logistics_time`, `success_time`, `sign`)
    VALUES (i, '11', '0.1', '0.1', '10.8', 1, 1, 0, 0, 1, '测试订单', 1, now(), now(), now(), now(), 1);
set i=i+1;
end while;
commit;
end
```



##### 二、读写分离-动态切换数据源版本1.0

serial_no 为偶数 进_0库，为奇数 进_1库

pay_method_id 为偶数 进_0表，为奇数 进_1表

```java
@Test
public void insertPOrder() {
    POrder pOrder = new POrder();
    pOrder.setSerialNo(0L); //serial_no 为偶数 进_0库，为奇数 进_1库
    pOrder.setPredictSum(new BigDecimal("0"));
    pOrder.setDiscountsSum(new BigDecimal("0"));
    pOrder.setLogisticsSum(new BigDecimal("0"));
    pOrder.setActualSum(new BigDecimal("0"));
    pOrder.setPayMethodId(1L); //pay_method_id 为偶数 进_0表，为奇数 进_1表
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

执行insert

```
2020-12-02 22:40:17.304  INFO 18001 --- [           main] o.a.s.core.util.ConfigurationLogger      : ShardingRuleConfiguration
bindingTables:
- p_order
defaultDatabaseStrategy:
  inline:
    algorithmExpression: ds$->{serial_no % 2}
    shardingColumn: serial_no
tables:
  p_order:
    actualDataNodes: ds$->{0..1}.p_order_$->{0..1}
    logicTable: p_order
    tableStrategy:
      inline:
        algorithmExpression: p_order_$->{pay_method_id % 2}
        shardingColumn: pay_method_id

2020-12-02 22:40:17.304  INFO 18001 --- [           main] o.a.s.core.util.ConfigurationLogger      : Properties
sql.show: 'true'

2020-12-02 22:40:17.718  INFO 18001 --- [           main] ApacheShardingsphereDemoApplicationTests : Started ApacheShardingsphereDemoApplicationTests in 1.878 seconds (JVM running for 2.701)

2020-12-02 22:40:18.507  INFO 18001 --- [           main] ShardingSphere-SQL                       : Rule Type: sharding
2020-12-02 22:40:18.507  INFO 18001 --- [           main] ShardingSphere-SQL                       : Logic SQL: insert into p_order (serial_no, predict_sum, discounts_sum, 
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
2020-12-02 22:40:18.507  INFO 18001 --- [           main] ShardingSphere-SQL                       : SQLStatement: InsertSQLStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.core.parse.sql.statement.dml.InsertStatement@256bb5be, tablesContext=TablesContext(tables=[Table(name=p_order, alias=Optional.absent())], schema=Optional.absent())), columnNames=[serial_no, predict_sum, discounts_sum, logistics_sum, actual_sum, pay_method_id, logistics_id, order_state, logistics_state, operator_id, remark, operation_id, create_time, change_time, logistics_time, success_time, sign], insertValueContexts=[InsertValueContext(parametersCount=17, valueExpressions=[ParameterMarkerExpressionSegment(startIndex=290, stopIndex=290, parameterMarkerIndex=0), ParameterMarkerExpressionSegment(startIndex=293, stopIndex=293, parameterMarkerIndex=1), ParameterMarkerExpressionSegment(startIndex=296, stopIndex=296, parameterMarkerIndex=2), ParameterMarkerExpressionSegment(startIndex=306, stopIndex=306, parameterMarkerIndex=3), ParameterMarkerExpressionSegment(startIndex=309, stopIndex=309, parameterMarkerIndex=4), ParameterMarkerExpressionSegment(startIndex=312, stopIndex=312, parameterMarkerIndex=5), ParameterMarkerExpressionSegment(startIndex=322, stopIndex=322, parameterMarkerIndex=6), ParameterMarkerExpressionSegment(startIndex=325, stopIndex=325, parameterMarkerIndex=7), ParameterMarkerExpressionSegment(startIndex=328, stopIndex=328, parameterMarkerIndex=8), ParameterMarkerExpressionSegment(startIndex=338, stopIndex=338, parameterMarkerIndex=9), ParameterMarkerExpressionSegment(startIndex=341, stopIndex=341, parameterMarkerIndex=10), ParameterMarkerExpressionSegment(startIndex=344, stopIndex=344, parameterMarkerIndex=11), ParameterMarkerExpressionSegment(startIndex=354, stopIndex=354, parameterMarkerIndex=12), ParameterMarkerExpressionSegment(startIndex=357, stopIndex=357, parameterMarkerIndex=13), ParameterMarkerExpressionSegment(startIndex=360, stopIndex=360, parameterMarkerIndex=14), ParameterMarkerExpressionSegment(startIndex=370, stopIndex=370, parameterMarkerIndex=15), ParameterMarkerExpressionSegment(startIndex=373, stopIndex=373, parameterMarkerIndex=16)], parameters=[0, 0, 0, 0, 0, 1, 0, 0, 0, 0, , 0, 2020-12-02 22:40:17.881, 2020-12-02 22:40:17.881, 2020-12-02 22:40:17.881, 2020-12-02 22:40:17.881, 0])])
2020-12-02 22:40:18.508  INFO 18001 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: insert into p_order_1 (serial_no, predict_sum, discounts_sum, 
      logistics_sum, actual_sum, pay_method_id, 
      logistics_id, order_state, logistics_state, 
      operator_id, remark, operation_id, 
      create_time, change_time, logistics_time, 
      success_time, sign)
    values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ::: [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, , 0, 2020-12-02 22:40:17.881, 2020-12-02 22:40:17.881, 2020-12-02 22:40:17.881, 2020-12-02 22:40:17.881, 0]

```



执行selectAll

```
2020-12-02 23:03:58.753  INFO 18651 --- [           main] ShardingSphere-SQL                       : Rule Type: sharding
2020-12-02 23:03:58.753  INFO 18651 --- [           main] ShardingSphere-SQL                       : Logic SQL: select * from p_order
2020-12-02 23:03:58.754  INFO 18651 --- [           main] ShardingSphere-SQL                       : SQLStatement: SelectSQLStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.core.parse.sql.statement.dml.SelectStatement@59845579, tablesContext=TablesContext(tables=[Table(name=p_order, alias=Optional.absent())], schema=Optional.absent())), projectionsContext=ProjectionsContext(startIndex=7, stopIndex=7, distinctRow=false, projections=[ShorthandProjection(owner=Optional.absent())]), groupByContext=org.apache.shardingsphere.core.preprocessor.segment.select.groupby.GroupByContext@30c19bff, orderByContext=org.apache.shardingsphere.core.preprocessor.segment.select.orderby.OrderByContext@4e375bba, paginationContext=org.apache.shardingsphere.core.preprocessor.segment.select.pagination.PaginationContext@ab2e6d2, containsSubquery=false)
2020-12-02 23:03:58.754  INFO 18651 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_0
2020-12-02 23:03:58.754  INFO 18651 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: select * from p_order_1
2020-12-02 23:03:58.754  INFO 18651 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_0
2020-12-02 23:03:58.754  INFO 18651 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: select * from p_order_1
POrder(id=2000003, serialNo=null, predictSum=null, discountsSum=null, logisticsSum=null, actualSum=null, payMethodId=null, logisticsId=null, orderState=null, logisticsState=null, operatorId=null, remark=, operationId=null, createTime=null, changeTime=null, logisticsTime=null, successTime=null, sign=0)
POrder(id=2000003, serialNo=null, predictSum=null, discountsSum=null, logisticsSum=null, actualSum=null, payMethodId=null, logisticsId=null, orderState=null, logisticsState=null, operatorId=null, remark=, operationId=null, createTime=null, changeTime=null, logisticsTime=null, successTime=null, sign=0)

```

