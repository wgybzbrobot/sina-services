-- phpMyAdmin SQL Dump
-- version 4.1.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2014-03-12 14:26:38
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
-- 表的结构 `bozhu_price`
--

CREATE TABLE IF NOT EXISTS `bozhu_price` (
  `username` bigint(32) unsigned NOT NULL COMMENT '用户id',
  `sourceid` int(10) unsigned NOT NULL COMMENT 'bz_price_source表ID',
  `typeid` tinyint(2) unsigned NOT NULL COMMENT '价格类型，见价格类型表',
  `price` decimal(10,2) unsigned NOT NULL COMMENT '价格',
  `update_time` datetime NOT NULL,
  UNIQUE KEY `username_sourceid_typeid_unique` (`username`,`sourceid`,`typeid`),
  KEY `sourceid` (`sourceid`),
  KEY `typeid` (`typeid`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微博账号报价、分析价表';

--
-- 转存表中的数据 `bozhu_price`
--

INSERT INTO `bozhu_price` (`username`, `sourceid`, `typeid`, `price`, `update_time`) VALUES
(1699668020, 1, 1, '5.00', '2013-10-12 01:02:03'),
(1699668020, 1, 2, '5.00', '2013-10-12 01:02:03'),
(1699668020, 2, 1, '5.00', '2013-10-12 01:02:03'),
(1772605247, 1, 1, '5.00', '2013-10-12 01:02:03'),
(1772605247, 1, 2, '5.00', '2013-10-12 01:02:03'),
(1772605247, 1, 3, '5.00', '2013-10-12 01:02:03'),
(1772605247, 1, 4, '5.00', '2013-10-12 01:02:03'),
(1772605247, 2, 1, '5.00', '2013-10-12 01:02:03'),
(1772605247, 2, 3, '5.00', '2013-10-12 01:02:03');

--
-- 限制导出的表
--

--
-- 限制表 `bozhu_price`
--
ALTER TABLE `bozhu_price`
  ADD CONSTRAINT `FK_bozhu_price_bozhu` FOREIGN KEY (`username`) REFERENCES `bozhu` (`username`),
  ADD CONSTRAINT `FK_bozhu_price_bozhu_price_source` FOREIGN KEY (`sourceid`) REFERENCES `bozhu_price_source` (`id`),
  ADD CONSTRAINT `FK_bozhu_price_bozhu_price_type` FOREIGN KEY (`typeid`) REFERENCES `bozhu_price_type` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
