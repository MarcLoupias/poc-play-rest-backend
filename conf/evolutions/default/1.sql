
# --- !Ups

CREATE TABLE  `User` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `hashedPwd` tinyblob,
  `login` varchar(255) DEFAULT NULL,
  `iterations` int(11) NOT NULL,
  `pwdDerivatedKeyBitLength` int(11) NOT NULL,
  `pwdPBKDF2algo` varchar(255) DEFAULT NULL,
  `salt` tinyblob,
  `saltByteLength` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `Category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `Product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `numberInteger` int(11) DEFAULT NULL,
  `numberLong` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_f018012d02dc4ccab9396eb4dac` (`category_id`),
  CONSTRAINT `FK_f018012d02dc4ccab9396eb4dac` FOREIGN KEY (`category_id`) REFERENCES `Category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `County` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table User;
drop table Product;
drop table Category;
drop table County;

SET FOREIGN_KEY_CHECKS=1;

