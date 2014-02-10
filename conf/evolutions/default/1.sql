
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

CREATE TABLE  `County` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table User;
drop table County;

SET FOREIGN_KEY_CHECKS=1;

