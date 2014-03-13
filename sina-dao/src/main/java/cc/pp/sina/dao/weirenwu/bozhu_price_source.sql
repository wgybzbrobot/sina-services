-- phpMyAdmin SQL Dump
-- version 4.1.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2014-03-12 14:26:51
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
-- 表的结构 `bozhu_price_source`
--

CREATE TABLE IF NOT EXISTS `bozhu_price_source` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '价格来源ID',
  `name` varchar(50) NOT NULL COMMENT '价格来源名称',
  `qq` varchar(50) NOT NULL COMMENT 'QQ',
  `telephone` varchar(50) NOT NULL COMMENT '电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='博主价格渠道表' AUTO_INCREMENT=5 ;

--
-- 转存表中的数据 `bozhu_price_source`
--

INSERT INTO `bozhu_price_source` (`id`, `name`, `qq`, `telephone`) VALUES
(1, '博主报价', '', '18888888888'),
(2, '分析价', '123456', ''),
(3, '微博易价', '', ''),
(4, '微任务价', '', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
