-- sample.adress definition

CREATE OR REPLACE SEQUENCE `marathons_seq` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 nocache nocycle ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `marathons` (
    `id` bigint(20) NOT NULL,
    `member_id` bigint(20) NOT NULL,
    `peso` varchar(255) DEFAULT NULL,
    `score` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_marathons_member` (`member_id`),
    CONSTRAINT `fk_marathons_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;