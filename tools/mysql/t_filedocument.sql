/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50171
Source Host           : localhost:3306
Source Database       : egies

Target Server Type    : MYSQL
Target Server Version : 50171
File Encoding         : 65001

Date: 2017-07-14 17:16:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_filedocument
-- ----------------------------
DROP TABLE IF EXISTS `t_filedocument`;
CREATE TABLE `t_filedocument` (
  `fdId` int(11) NOT NULL AUTO_INCREMENT,
  `fdName` varchar(255) DEFAULT NULL,
  `swfName` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`fdId`),
  KEY `FK_dl3rav5q7jybginc08xqouvs4` (`userId`),
  CONSTRAINT `FK_dl3rav5q7jybginc08xqouvs4` FOREIGN KEY (`userId`) REFERENCES `s_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_filedocument
-- ----------------------------
