/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50171
Source Host           : localhost:3306
Source Database       : egies

Target Server Type    : MYSQL
Target Server Version : 50171
File Encoding         : 65001

Date: 2017-07-14 17:16:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_column
-- ----------------------------
DROP TABLE IF EXISTS `t_column`;
CREATE TABLE `t_column` (
  `columnId` int(11) NOT NULL AUTO_INCREMENT,
  `parentId` int(11) DEFAULT NULL,
  `columnName` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`columnId`),
  KEY `FK_3rahb1pitc2q2kdg13q029kdb` (`userId`),
  CONSTRAINT `FK_3rahb1pitc2q2kdg13q029kdb` FOREIGN KEY (`userId`) REFERENCES `s_user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_column
-- ----------------------------
INSERT INTO `t_column` VALUES ('1', '0', '应急测绘', '2017-07-14 15:00:07', 'Test', null);
