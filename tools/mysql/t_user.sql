/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50171
Source Host           : localhost:3306
Source Database       : egies

Target Server Type    : MYSQL
Target Server Version : 50171
File Encoding         : 65001

Date: 2017-07-14 17:17:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for s_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_user
-- ----------------------------
