/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50736 (5.7.36-log)
 Source Host           : localhost:3306
 Source Schema         : it-source

 Target Server Type    : MySQL
 Target Server Version : 50736 (5.7.36-log)
 File Encoding         : 65001

 Date: 25/09/2024 23:33:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `aid` int(32) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phoneNum` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`aid`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, '', 'root', '$2a$10$p7872GpXZBSzh3RcF4emIexlMF3HeFAi2lZQoUeDWMOHhIhmAh1OK', '', 1);

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role`  (
  `aid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`aid`, `rid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', '1');
INSERT INTO `admin_role` VALUES ('2', '2');
INSERT INTO `admin_role` VALUES ('3', '1');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `cname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`cid`) USING BTREE,
  UNIQUE INDEX `AK_nq_categoryname`(`cname`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, ' AI论文');
INSERT INTO `category` VALUES (2, ' linux嵌入式');
INSERT INTO `category` VALUES (6, ' matlab');
INSERT INTO `category` VALUES (9, ' 前端');
INSERT INTO `category` VALUES (3, ' 毕业设计代写');
INSERT INTO `category` VALUES (8, 'Java');
INSERT INTO `category` VALUES (5, 'python');
INSERT INTO `category` VALUES (7, '人工智能');
INSERT INTO `category` VALUES (4, '网络安全');

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `mid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`pid`, `mid`) USING BTREE,
  INDEX `FK_user_favorite`(`mid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (3, 1);
INSERT INTO `favorite` VALUES (3, 2);
INSERT INTO `favorite` VALUES (3, 3);
INSERT INTO `favorite` VALUES (3, 4);
INSERT INTO `favorite` VALUES (3, 5);
INSERT INTO `favorite` VALUES (3, 6);
INSERT INTO `favorite` VALUES (3, 7);
INSERT INTO `favorite` VALUES (3, 8);
INSERT INTO `favorite` VALUES (3, 9);
INSERT INTO `favorite` VALUES (3, 10);
INSERT INTO `favorite` VALUES (3, 11);
INSERT INTO `favorite` VALUES (3, 12);
INSERT INTO `favorite` VALUES (3, 13);

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `mid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `birthday` date NULL DEFAULT NULL,
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phoneNum` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `active` tinyint(1) NULL DEFAULT NULL,
  `activeCode` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`mid`) USING BTREE,
  UNIQUE INDEX `AK_nq_username`(`username`) USING BTREE,
  UNIQUE INDEX `AK_nq_code`(`activeCode`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (3, 'baizhan', '$2a$10$s8A8EPUzinL.NCoTyXKu3eza5BTliAQEyeiv.R53R7Hw4hFhPVH8W', '2022-06-05', '男', '13855555555', '461618768@qq.com', 1, '0a2c48d2-b270-4590-a0bc-4b8e5bd6cb7b');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `pid` int(32) NOT NULL AUTO_INCREMENT,
  `permissionName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permissionDesc` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '查询管理员', '/admin/all');
INSERT INTO `permission` VALUES (2, '查询角色', '/role/all');
INSERT INTO `permission` VALUES (3, '查询权限', '/permission/all');
INSERT INTO `permission` VALUES (4, '新增权限', '/permission/add');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(10, 0) NULL DEFAULT NULL,
  `hotline` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT NULL,
  `productDesc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pImage` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cid` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE,
  INDEX `FK_category_route`(`cid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 519 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (518, 'python 各大培训机构资源', 999, 'python.com', 1, '<p>python 各大培训机构资源</p><p><br></p>', '/upload/img/product/small/7d82c99a-3414-4f0c-8856-efb4b2f67753Tsing2.jpg', 5);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `rid` int(32) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `roleDesc` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', '管理所有数据');
INSERT INTO `role` VALUES (2, '商品管理员', '管理商品');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `rid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '1');
INSERT INTO `role_permission` VALUES ('1', '2');
INSERT INTO `role_permission` VALUES ('1', '3');
INSERT INTO `role_permission` VALUES ('1', '4');
INSERT INTO `role_permission` VALUES ('2', '3');

SET FOREIGN_KEY_CHECKS = 1;
