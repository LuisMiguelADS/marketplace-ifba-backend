CREATE TABLE users (
  id_usuario UUID PRIMARY KEY,
  nome_completo VARCHAR(255),
  role VARCHAR(50),
  email VARCHAR(255),
  telefone VARCHAR(20),
  password VARCHAR(255),
  cpf VARCHAR(14),
  data_registro TIMESTAMP,
  data_nascimento DATE,
  biografia TEXT,
  foto_perfilurl VARCHAR(255),
  endereco VARCHAR(255),
  instituicao VARCHAR(255),
  organizacao VARCHAR(255)
);
