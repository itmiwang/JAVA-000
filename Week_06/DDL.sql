-- 基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交 DDL 的 SQL 文件到 Github（后面 2 周的作业依然要是用到这个表结构）。

CREATE TABLE `p_goods` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '商品名称',
  `price` decimal(14,5) NOT NULL COMMENT '商品单价（单位：元）',
  `classifies_id` bigint(20) DEFAULT NULL COMMENT '关联商品分类表',
  `description_id` bigint(20) DEFAULT NULL COMMENT '关联商品描述表',
  `discounts_id` bigint(20) DEFAULT NULL COMMENT '关联商品折扣表',
  `images_id` varchar(255) DEFAULT NULL COMMENT '关联商品图片表',
  `operation_id` bigint(20) NOT NULL COMMENT '上传人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `change_time` datetime NOT NULL COMMENT '更新时间',
  `sign` int(2) NOT NULL COMMENT '逻辑删除标识（1：正常；2：逻辑删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';


CREATE TABLE `p_order` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


CREATE TABLE `p_user` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `sex` int(1) DEFAULT NULL COMMENT '性别（0：男；1：女）',
  `birthday` varchar(6) DEFAULT NULL COMMENT '出生日期',
  `photo` varchar(32) DEFAULT NULL COMMENT '头像',
  `operation_id` bigint(20) NOT NULL COMMENT '上传人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `change_time` datetime NOT NULL COMMENT '更新时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `sign` int(2) NOT NULL COMMENT '逻辑删除标识（1：正常；2：逻辑删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';



