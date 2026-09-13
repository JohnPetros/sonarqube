-- Add foreign key id_organization in groups

ALTER TABLE `groups`
  ADD COLUMN `organization_id` bigint(20) DEFAULT NULL,
  ADD CONSTRAINT `fk_groups_organization` FOREIGN KEY (`organization_id`) REFERENCES `organizations` (`id`);
