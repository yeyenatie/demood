-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: demo_db
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称快照',
  `product_price` decimal(10,2) NOT NULL COMMENT '下单时单价',
  `num` int NOT NULL COMMENT '购买数量',
  `sub_total` decimal(10,2) NOT NULL COMMENT '小计金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0未删 1已删',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` (`id`, `order_id`, `product_id`, `product_name`, `product_price`, `num`, `sub_total`, `create_time`, `deleted`) VALUES (8,1,1,'iPhone 15',5999.00,1,5999.00,'2026-10-17 12:20:30',0),(9,1,5,'无线鼠标',129.00,1,129.00,'2026-10-17 12:20:30',0),(10,2,2,'小米14',3999.00,1,3999.00,'2026-10-17 12:20:30',0),(11,2,5,'无线鼠标',129.00,1,129.00,'2026-10-17 12:20:30',0),(12,3,3,'华为Mate60',6499.00,1,6499.00,'2026-10-17 12:20:30',0),(13,3,5,'无线鼠标',129.00,1,129.00,'2026-10-17 12:20:30',0),(14,4,4,'机械键盘',399.00,1,399.00,'2026-10-17 12:20:30',0),(15,4,10,'USB-C 数据线',39.00,1,39.00,'2026-10-17 12:20:30',0),(16,5,6,'27寸显示器',1599.00,1,1599.00,'2026-10-17 12:20:30',0),(17,6,8,'笔记本电脑',6999.00,1,6999.00,'2026-10-17 12:20:30',0);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '下单用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `order_status` tinyint DEFAULT '0' COMMENT '订单状态 0待付款 1已付款 2已发货 3已完成 4已取消',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货手机号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0未删 1已删',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` (`id`, `order_no`, `user_id`, `total_amount`, `order_status`, `receiver_name`, `receiver_phone`, `address`, `create_time`, `update_time`, `deleted`) VALUES (7,'ORD202501010001',2,6128.00,0,'张三','13800000002','北京市朝阳区建国路1号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(8,'ORD202501010002',3,4128.00,1,'李四','13800000003','上海市浦东新区世纪大道2号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(9,'ORD202501010003',4,6628.00,2,'王五','13800000004','广州市天河区天河路3号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(10,'ORD202501010004',5,528.00,3,'赵六','13800000005','深圳市南山区科技园4号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(11,'ORD202501010005',6,1798.00,0,'孙七','13800000006','杭州市西湖区文三路5号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(12,'ORD202501010006',7,6999.00,1,'周八','13800000007','成都市武侯区人民南路6号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(13,'ORD202501010007',8,199.00,4,'吴九','13800000008','武汉市洪山区珞喻路7号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(14,'ORD202501010008',9,1638.00,2,'郑十','13800000009','西安市雁塔区长安路8号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(15,'ORD202501010009',10,39.00,0,'刘一','13800000010','南京市玄武区中山路9号','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(16,'ORD202501010010',2,6398.00,3,'张三','13800000002','北京市朝阳区建国路1号','2026-10-17 12:20:30','2026-10-17 12:20:30',0);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `product_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '商品描述',
  `product_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品图片相对路径',
  `status` tinyint DEFAULT '1' COMMENT '状态 0下架 1上架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0未删 1已删',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`id`, `product_name`, `price`, `stock`, `product_desc`, `product_image`, `status`, `create_time`, `update_time`, `deleted`) VALUES (7,'iPhone 15',5999.00,100,'苹果手机 128G','/img/iphone15.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(8,'小米14',3999.00,200,'小米旗舰 256G','/img/mi14.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(9,'华为Mate60',6499.00,80,'华为旗舰 256G','/img/mate60.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(10,'机械键盘',399.00,500,'87键 青轴','/img/keyboard.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(11,'无线鼠标',129.00,600,'2.4G 静音','/img/mouse.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(12,'27寸显示器',1599.00,150,'2K 165Hz','/img/monitor.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(13,'蓝牙耳机',499.00,300,'主动降噪','/img/earphone.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(14,'笔记本电脑',6999.00,60,'16G 512G','/img/laptop.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(15,'移动电源',199.00,800,'20000mAh','/img/powerbank.jpg',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(16,'USB-C 数据线',39.00,1000,'1.5米 快充','/img/cable.jpg',0,'2026-10-17 12:20:30','2026-10-17 12:20:30',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '角色描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0未删 1已删',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`, `role_name`, `role_code`, `description`, `create_time`, `update_time`, `deleted`) VALUES (4,'超级管理员','ROLE_ADMIN','拥有系统全部权限','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(5,'普通用户','ROLE_USER','普通购物用户','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(6,'商品管理员','ROLE_PRODUCT','负责商品上下架与维护','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(7,'订单管理员','ROLE_ORDER','负责订单查询与处理','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(8,'客服','ROLE_SERVICE','处理售后与咨询','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(9,'财务','ROLE_FINANCE','负责对账与结算','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(10,'仓储','ROLE_WAREHOUSE','负责库存与发货','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(11,'运营','ROLE_OPERATION','负责活动与推广','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(12,'审核员','ROLE_AUDITOR','负责内容审核','2026-10-17 12:20:30','2026-10-17 12:20:30',0),(13,'访客','ROLE_GUEST','仅浏览权限','2026-10-17 12:20:30','2026-10-17 12:20:30',0);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '邮箱',
  `status` tinyint DEFAULT '1' COMMENT '状态 0禁用 1正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0未删 1已删',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `status`, `create_time`, `update_time`, `deleted`) VALUES (8,'admin','e10adc3949ba59abbe56e057f20f883e','超级管理员','13800000001','admin@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(9,'zhangsan','e10adc3949ba59abbe56e057f20f883e','张三','13800000002','zhangsan@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(10,'lisi','e10adc3949ba59abbe56e057f20f883e','李四','13800000003','lisi@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(11,'wangwu','e10adc3949ba59abbe56e057f20f883e','王五','13800000004','wangwu@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(12,'zhaoliu','e10adc3949ba59abbe56e057f20f883e','赵六','13800000005','zhaoliu@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(13,'sunqi','e10adc3949ba59abbe56e057f20f883e','孙七','13800000006','sunqi@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(14,'zhouba','e10adc3949ba59abbe56e057f20f883e','周八','13800000007','zhouba@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(15,'wujiu','e10adc3949ba59abbe56e057f20f883e','吴九','13800000008','wujiu@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(16,'zhengshi','e10adc3949ba59abbe56e057f20f883e','郑十','13800000009','zhengshi@shop.com',1,'2026-10-17 12:20:30','2026-10-17 12:20:30',0),(17,'liuyi','e10adc3949ba59abbe56e057f20f883e','刘一','13800000010','liuyi@shop.com',0,'2026-10-17 12:20:30','2026-10-17 12:20:30',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0未删 1已删',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`id`, `user_id`, `role_id`, `create_time`, `deleted`) VALUES (7,1,1,'2026-10-17 12:20:30',0),(8,2,2,'2026-10-17 12:20:30',0),(9,3,2,'2026-10-17 12:20:30',0),(10,4,2,'2026-10-17 12:20:30',0),(11,5,2,'2026-10-17 12:20:30',0),(12,6,7,'2026-10-17 12:20:30',0),(13,7,8,'2026-10-17 12:20:30',0),(14,8,4,'2026-10-17 12:20:30',0),(15,9,5,'2026-10-17 12:20:30',0),(16,10,10,'2026-10-17 12:20:30',0);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'demo_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-17 13:02:41
