CREATE TABLE `EXAMPLE_TEST` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NUMBER` bigint(20) NOT NULL,
  `BLOB_ONE` mediumblob,
  `COMMENT` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MODIFIED_BY` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `MODIFIED_ON` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
)

