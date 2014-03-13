-- phpMyAdmin SQL Dump
-- version 4.1.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2014-03-12 14:26:34
-- 服务器版本： 5.5.34-MariaDB-log
-- PHP Version: 5.5.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pp_fenxi`
--

-- --------------------------------------------------------

--
-- 表的结构 `bozhu_detail`
--

CREATE TABLE IF NOT EXISTS `bozhu_detail` (
  `bzid` bigint(11) unsigned NOT NULL COMMENT 'bozhu表ID',
  `influence` int(10) NOT NULL COMMENT '影响力',
  `active` tinyint(2) NOT NULL COMMENT '活跃度',
  `wbnum` int(10) NOT NULL COMMENT '微博数',
  `fannum` int(10) NOT NULL COMMENT '粉丝数',
  `malerate` float NOT NULL COMMENT '男性比例（需要用1',
  `vrate` float NOT NULL COMMENT '加V比例',
  `exsit_fan_rate` float NOT NULL COMMENT '粉丝存在比例',
  `act_fan` int(10) NOT NULL COMMENT '活跃粉丝数',
  `act_fan_rate` float NOT NULL COMMENT '活跃粉丝比例',
  `fan_fans` bigint(11) NOT NULL COMMENT '粉丝及粉丝的粉丝数',
  `act_fan_fans` bigint(11) NOT NULL COMMENT '活跃粉及活跃粉的粉丝数',
  `wb_avg_daily` float NOT NULL COMMENT '每日平均微博数',
  `wb_avg_repost_lastweek` float NOT NULL COMMENT '最近一周微博平均转评数',
  `wb_avg_repost_lastmonth` float NOT NULL COMMENT '最近一月微博平均转评数',
  `wb_avg_repost` float NOT NULL COMMENT '微博平均转评数',
  `orig_wb_rate` float NOT NULL COMMENT '原创微博比例',
  `orig_wb_avg_repost` float NOT NULL COMMENT '原创微博平均转评数',
  `wb_avg_valid_repost_lastweek` float NOT NULL COMMENT '最近一周内平均每条微博的有效转评量',
  `wb_avg_valid_repost_lastmonth` float NOT NULL COMMENT '最近一个月内平均每条微博的有效转评量',
  `rt_user_avg_quality` float NOT NULL COMMENT '平均转发用户质量',
  `avg_valid_fan_cover_last100` bigint(11) NOT NULL COMMENT '最近100条微博的平均有效粉丝覆盖量',
  `identity_type` char(50) NOT NULL COMMENT '身份分类，如：草根大号，音乐达人',
  `industry_type` char(50) NOT NULL COMMENT '行业分类，如：IT，汽车',
  `fans_age` char(250) NOT NULL COMMENT '粉丝年龄分布',
  `fans_tags` char(250) NOT NULL COMMENT '粉丝标签数据',
  PRIMARY KEY (`bzid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博主详细信息数据库';

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
