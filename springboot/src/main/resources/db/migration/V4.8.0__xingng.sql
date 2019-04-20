CREATE TABLE `flyway_people` (
  `id` int(11) NOT NULL,
  `age` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `flyway_people` VALUES (1, 11, 'Lincon');