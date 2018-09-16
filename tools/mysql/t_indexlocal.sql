/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50171
Source Host           : localhost:3306
Source Database       : egies

Target Server Type    : MYSQL
Target Server Version : 50171
File Encoding         : 65001

Date: 2017-07-14 17:16:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_indexlocal
-- ----------------------------
DROP TABLE IF EXISTS `t_indexlocal`;
CREATE TABLE `t_indexlocal` (
  `indexId` int(11) NOT NULL AUTO_INCREMENT,
  `indexName` varchar(255) DEFAULT NULL,
  `indexTime` datetime DEFAULT NULL,
  `indexPath` varchar(255) DEFAULT NULL,
  `sourcePath` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `action` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `sqlSentence` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`indexId`),
  KEY `FK_h598mo9b30t9bpq6bfe4ewkgv` (`userId`),
  CONSTRAINT `FK_h598mo9b30t9bpq6bfe4ewkgv` FOREIGN KEY (`userId`) REFERENCES `s_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_indexlocal
-- ----------------------------
