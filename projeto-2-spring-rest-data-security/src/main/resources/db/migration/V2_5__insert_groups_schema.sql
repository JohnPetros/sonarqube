-- update and add new sample.groups test data

UPDATE groups SET organization_id = 1 WHERE id = 1;
UPDATE groups SET organization_id = 1 WHERE id = 2;
UPDATE groups SET organization_id = 2 WHERE id = 3;
INSERT INTO groups (id, name, organization_id) VALUES(NEXT VALUE FOR groups_seq, 'Grupo 4 - Teste', NULL);
