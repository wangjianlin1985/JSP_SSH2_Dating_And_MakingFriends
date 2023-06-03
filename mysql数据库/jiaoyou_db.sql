/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : jiaoyou_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-07-11 17:29:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `leaveword`
-- ----------------------------
DROP TABLE IF EXISTS `leaveword`;
CREATE TABLE `leaveword` (
  `leaveWordId` int(11) NOT NULL auto_increment,
  `title` varchar(60) default NULL,
  `content` longtext,
  `addTime` varchar(20) default NULL,
  `userObj` varchar(20) default NULL,
  `replyContent` varchar(20) default NULL,
  `replyTime` varchar(20) default NULL,
  PRIMARY KEY  (`leaveWordId`),
  KEY `FKDA787641C80FC67` (`userObj`),
  CONSTRAINT `FKDA787641C80FC67` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of leaveword
-- ----------------------------
INSERT INTO `leaveword` VALUES ('1', '我可以来相亲吗', '我年龄比较大了，想找另外一半！', '2018-04-13 15:27:52', 'user2', '可以的哈', '2018-04-13 15:30:29');
INSERT INTO `leaveword` VALUES ('2', '周末有相亲活动吗', '希望大家多组织些相亲活动！', '2018-04-13 15:32:25', 'user2', '收到你的建议', '2018-04-13 15:38:52');

-- ----------------------------
-- Table structure for `t_activityinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_activityinfo`;
CREATE TABLE `t_activityinfo` (
  `activityId` int(11) NOT NULL auto_increment,
  `typeObj` int(11) default NULL,
  `title` varchar(80) default NULL,
  `content` longtext,
  `activityTime` varchar(30) default NULL,
  `personNum` int(11) default NULL,
  `userObj` varchar(20) default NULL,
  `addTime` varchar(20) default NULL,
  PRIMARY KEY  (`activityId`),
  KEY `FK45FAC668DB300D68` (`typeObj`),
  KEY `FK45FAC668C80FC67` (`userObj`),
  CONSTRAINT `FK45FAC668C80FC67` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`),
  CONSTRAINT `FK45FAC668DB300D68` FOREIGN KEY (`typeObj`) REFERENCES `t_activitytype` (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_activityinfo
-- ----------------------------
INSERT INTO `t_activityinfo` VALUES ('1', '1', '80后剩斗士相亲快跑', '80后们，你爸javascript:void(0)妈叫你来相亲了，他们都急着抱孙子呢！', '2018-05-01 下午3点', '1', 'user1', '2018-03-28 12:13:15');
INSERT INTO `t_activityinfo` VALUES ('2', '2', '90后相亲成家团结友爱活动', '90后都20好几了，也可以成家了，趁这个机会来这里认识认识，说不定能找到你的另一半！', '2018-04-25 下午2点', '1', 'user1', '2018-03-28 13:15:16');
INSERT INTO `t_activityinfo` VALUES ('3', '3', '大家一起来周末相亲交友', '周末了，大家都基本放假了，还没找到另一半的朋友们速度行动了，无论你是80后还是90后，都可以来这里逛逛！', '2018-04-22 上午9点', '1', 'user1', '2018-03-28 15:06:30');

-- ----------------------------
-- Table structure for `t_activitytype`
-- ----------------------------
DROP TABLE IF EXISTS `t_activitytype`;
CREATE TABLE `t_activitytype` (
  `typeId` int(11) NOT NULL auto_increment,
  `typeName` varchar(20) default NULL,
  PRIMARY KEY  (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_activitytype
-- ----------------------------
INSERT INTO `t_activitytype` VALUES ('1', '80后相亲');
INSERT INTO `t_activitytype` VALUES ('2', '90后相亲');
INSERT INTO `t_activitytype` VALUES ('3', '周末相亲');

-- ----------------------------
-- Table structure for `t_news`
-- ----------------------------
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `newsId` int(11) NOT NULL auto_increment,
  `title` varchar(80) default NULL,
  `content` longtext,
  `publishDate` varchar(20) default NULL,
  PRIMARY KEY  (`newsId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_news
-- ----------------------------
INSERT INTO `t_news` VALUES ('1', '相亲交友网站成立了', '还是单身的你，是否还在寻觅你心中的他或她，那就赶快来这里寻找你的缘分吧！', '2018-03-26');

-- ----------------------------
-- Table structure for `t_signup`
-- ----------------------------
DROP TABLE IF EXISTS `t_signup`;
CREATE TABLE `t_signup` (
  `signId` int(11) NOT NULL auto_increment,
  `activityObj` int(11) default NULL,
  `userObj` varchar(20) default NULL,
  `signUpTime` varchar(20) default NULL,
  PRIMARY KEY  (`signId`),
  KEY `FK4712AB83D75772E7` (`activityObj`),
  KEY `FK4712AB83C80FC67` (`userObj`),
  CONSTRAINT `FK4712AB83C80FC67` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`),
  CONSTRAINT `FK4712AB83D75772E7` FOREIGN KEY (`activityObj`) REFERENCES `t_activityinfo` (`activityId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_signup
-- ----------------------------
INSERT INTO `t_signup` VALUES ('1', '1', 'user1', '2018-03-28 15:16:13');
INSERT INTO `t_signup` VALUES ('3', '3', 'user2', '2018-03-28 16:45:00');
INSERT INTO `t_signup` VALUES ('4', '1', 'user2', '2018-03-28 16:45:05');
INSERT INTO `t_signup` VALUES ('5', '2', 'user2', '2018-07-11 17:20:43');

-- ----------------------------
-- Table structure for `t_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_userinfo`;
CREATE TABLE `t_userinfo` (
  `user_name` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  `userType` varchar(20) default NULL,
  `name` varchar(20) default NULL,
  `sex` varchar(4) default NULL,
  `userPhoto` varchar(50) default NULL,
  `birthday` varchar(20) default NULL,
  `telephone` varchar(20) default NULL,
  `address` varchar(80) default NULL,
  `registerTime` varchar(20) default NULL,
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userinfo
-- ----------------------------
INSERT INTO `t_userinfo` VALUES ('user1', '123', '相亲组织者', '双鱼林', '男', 'upload/0eb151bf-5a60-42f9-95a4-c820e6aa4939.jpg', '2018-03-25', '13598439843', '四川成都', '2018-03-28 12:12:15');
INSERT INTO `t_userinfo` VALUES ('user2', '123', '相亲用户', '张萌萌', '女', 'upload/c7703fe5-8f12-44c9-b891-101a2cedda33.jpg', '2018-03-24', '13598492943', '四川达州', '2018-03-28 15:12:54');
