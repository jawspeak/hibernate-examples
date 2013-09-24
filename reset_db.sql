drop database if exists jaw_hbm;

create database jaw_hbm;
use jaw_hbm;

CREATE TABLE `stocks` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_stocks_name` (`name`),
  UNIQUE KEY `idx_stocks_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE extra_stock_data (
  id INT(10) unsigned NOT NULL auto_increment,
  note TEXT NOT NULL,
  stock_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id) USING btree
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE daily_summaries (
  id INT(10) unsigned NOT NULL auto_increment,
  date_at DATE NOT NULL,
  volume INT(10) NOT NULL,
  stock_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id) USING btree,
  UNIQUE KEY idx_daily_summaries_date (date_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

