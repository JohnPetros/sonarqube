-- sample.addresses test data

INSERT INTO addresses (id, logradouro, numero, bairro, municipio, estado, cep, organization_id) VALUES
  (NEXT VALUE FOR addresses_seq, 'Rua 1 - Teste', '1 - Teste', 'Bairro 1 - Teste', 'Cidade 1 - Teste', 'Estado 1 - Teste', 'cep 1 - Teste', 1),
  (NEXT VALUE FOR addresses_seq, 'Rua 2 - Teste', '2 - Teste', 'Bairro 1 - Teste', 'Cidade 2 - Teste', 'Estado 2 - Teste', 'cep 2 - Teste', 2);
