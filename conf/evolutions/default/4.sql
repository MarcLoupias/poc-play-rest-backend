# --- !Ups

CREATE TABLE  `Cinema` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `screen` int(11) DEFAULT NULL,
  `seat` int(11) DEFAULT NULL,
  `county_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_f099123e13ed6ddbc0407fc5ebd` (`county_id`),
  CONSTRAINT `FK_f099123e13ed6ddbc0407fc5ebd` FOREIGN KEY (`county_id`) REFERENCES `County` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO Cinema (name, screen, seat, county_id)
  SELECT CinemaTmp.name, CinemaTmp.screen, CinemaTmp.seat, County.id
  FROM CinemaTmp
    INNER JOIN County ON ( UCASE(County.code) = UCASE(CinemaTmp.code_dep) );

DROP TABLE CinemaTmp;

# --- !Downs

