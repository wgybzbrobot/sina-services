-- phpMyAdmin SQL Dump
-- version 4.1.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2014-03-12 14:26:29
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
-- 表的结构 `bozhu`
--

CREATE TABLE IF NOT EXISTS `bozhu` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '全公司统一的微博账号ID',
  `username` bigint(32) unsigned NOT NULL COMMENT '微博平台上的用户名或ID',
  `ptype` enum('sina','tencent') NOT NULL COMMENT '微博平台类型',
  `default_price_source` int(10) unsigned DEFAULT NULL COMMENT '博主默认渠道的ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_ptype` (`username`,`ptype`),
  KEY `default_price_source` (`default_price_source`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='博主账号表' AUTO_INCREMENT=4 ;

--
-- 转存表中的数据 `bozhu`
--

INSERT INTO `bozhu` (`id`, `username`, `ptype`, `default_price_source`) VALUES
(1, 1772605247, 'sina', 1),
(2, 1699668020, 'sina', 2),
(3, 8888888888, 'sina', NULL);

--
-- 限制导出的表
--

--
-- 限制表 `bozhu`
--
ALTER TABLE `bozhu`
  ADD CONSTRAINT `bozhu_ibfk_1` FOREIGN KEY (`default_price_source`) REFERENCES `bozhu_price_source` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
