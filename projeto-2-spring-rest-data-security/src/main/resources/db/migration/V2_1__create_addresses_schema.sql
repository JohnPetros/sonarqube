-- sample.adress definition

CREATE OR REPLACE SEQUENCE `addresses_seq` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 nocache nocycle ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `addresses` (
    `id` bigint(20) NOT NULL,
    `organization_id` bigint(20) NOT NULL,
    `logradouro` varchar(255) DEFAULT NULL,
    `numero` varchar(255) DEFAULT NULL,
    `bairro` varchar(255) DEFAULT NULL,
    `cep` varchar(255) DEFAULT NULL,
    `municipio` varchar(255) DEFAULT NULL,
    `estado` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_addresses_organization` (`organization_id`),
    CONSTRAINT `fk_addresses_organization` FOREIGN KEY (`organization_id`) REFERENCES `organizations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;