/*
 Navicat Premium Dump SQL

 Source Server         : asd
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : tushuguanlixitong

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 21/09/2026 16:18:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `author` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `price` decimal(10, 0) NULL DEFAULT NULL,
  `stock` int NULL DEFAULT NULL,
  `cid` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `cid`(`cid` ASC) USING BTREE,
  CONSTRAINT `cid` FOREIGN KEY (`cid`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, '三体', '刘慈欣', 50, NULL, NULL);
INSERT INTO `book` VALUES (2, '西游记', '吴承恩', 80, NULL, NULL);
INSERT INTO `book` VALUES (3, '红楼梦', '曹雪芹', 75, 10, 2);
INSERT INTO `book` VALUES (4, '流浪地球', '刘慈欣', 42, 15, 1);
INSERT INTO `book` VALUES (5, '呐喊', '鲁迅', 36, 9, 3);
INSERT INTO `book` VALUES (6, '史记', '司马迁', 98, 5, 4);
INSERT INTO `book` VALUES (7, '百年孤独', '马尔克斯', 55, 7, 3);
INSERT INTO `book` VALUES (8, '理想国', '柏拉图', 66, 6, 5);
INSERT INTO `book` VALUES (9, '三国演义', '罗贯中', 72, 11, 2);
INSERT INTO `book` VALUES (10, '人类简史', '尤瓦尔', 48, 14, 4);
INSERT INTO `book` VALUES (11, '马风顺导管指南', '马风顺', -100, 99999, 3);

-- ----------------------------
-- Table structure for borrow_record
-- ----------------------------
DROP TABLE IF EXISTS `borrow_record`;
CREATE TABLE `borrow_record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `book_id` int NULL DEFAULT NULL,
  `borrow_time` datetime NULL DEFAULT NULL,
  `return_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1 COMMENT '1未还 2已还',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE,
  CONSTRAINT `book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow_record
-- ----------------------------
INSERT INTO `borrow_record` VALUES (1, 1, 1, '2026-01-10 08:30:00', NULL, 1);
INSERT INTO `borrow_record` VALUES (2, 1, 2, '2026-02-15 09:20:00', '2026-03-15 10:10:00', 2);
INSERT INTO `borrow_record` VALUES (3, 1, 3, '2026-03-05 14:00:00', NULL, 1);
INSERT INTO `borrow_record` VALUES (4, 1, 4, '2026-03-20 15:30:00', '2026-04-18 11:20:00', 2);
INSERT INTO `borrow_record` VALUES (5, 1, 5, '2026-04-02 10:00:00', NULL, 1);
INSERT INTO `borrow_record` VALUES (6, 1, 6, '2026-04-12 16:10:00', '2026-05-10 09:30:00', 2);
INSERT INTO `borrow_record` VALUES (7, 1, 7, '2026-05-08 11:40:00', NULL, 1);
INSERT INTO `borrow_record` VALUES (8, 1, 8, '2026-05-22 08:50:00', '2026-06-20 14:15:00', 2);
INSERT INTO `borrow_record` VALUES (9, 1, 9, '2026-06-07 13:20:00', NULL, 1);
INSERT INTO `borrow_record` VALUES (10, 1, 10, '2026-06-18 17:00:00', '2026-07-16 10:40:00', 2);
INSERT INTO `borrow_record` VALUES (11, 1, 11, '2026-09-21 10:25:35', '2026-09-21 10:26:09', 2);
INSERT INTO `borrow_record` VALUES (12, 1, 11, '2026-09-21 10:25:51', NULL, 1);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '科幻小说');
INSERT INTO `category` VALUES (2, '古典文学');
INSERT INTO `category` VALUES (3, '现代文学');
INSERT INTO `category` VALUES (4, '历史');
INSERT INTO `category` VALUES (5, '哲学');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `real_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', '管理员', '13801380000');
INSERT INTO `user` VALUES (2, '马风顺', '是骚逼', '马风顺', '155454545');

SET FOREIGN_KEY_CHECKS = 1;
