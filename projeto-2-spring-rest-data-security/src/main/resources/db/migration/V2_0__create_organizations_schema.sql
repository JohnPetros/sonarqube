-- sample.organization definition

CREATE OR REPLACE SEQUENCE `organizations_seq` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 nocache nocycle ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `organizations` (
    `id` bigint(20) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    `instituicao_ensino` varchar(255) DEFAULT NULL,
    `pais_sede` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;