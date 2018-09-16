/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50171
Source Host           : localhost:3306
Source Database       : egies

Target Server Type    : MYSQL
Target Server Version : 50171
File Encoding         : 65001

Date: 2017-07-14 17:17:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_index_column
-- ----------------------------
DROP TABLE IF EXISTS `t_index_column`;
CREATE TABLE `t_index_column` (
  `columnId` int(11) NOT NULL,
  `indexId` int(11) NOT NULL,
  PRIMARY KEY (`indexId`,`columnId`),
  KEY `FK_swfuvuqtmk303raqi8amdw8n4` (`columnId`),
  CONSTRAINT `FK_f7p61sch92oscy8db8ohotdtb` FOREIGN KEY (`indexId`) REFERENCES `s_index` (`indexId`),
  CONSTRAINT `FK_swfuvuqtmk303raqi8amdw8n4` FOREIGN KEY (`columnId`) REFERENCES `s_column` (`columnId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_index_column
-- ----------------------------
