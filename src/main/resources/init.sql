INSERT INTO users (
    id_usuario,
    nome_completo,
    role,
    email,
    telefone,
    password,
    cpf,
    data_registro,
    data_nascimento,
    biografia,
    foto_perfilurl,
    endereco,
    instituicao,
    organizacao
) VALUES (
    RANDOM_UUID(),
    'Administrador Padrão',
    'ADMIN',
    'admin@admin.com',
    '(00) 00000-0000',
    '$2a$10$7y9IzE0TQxgEvBQnMS9jVOG/6O8N5e3AgTP1XZ.juYoA6hjN5CH0G', -- senha: admin123
    '00000000000',
    CURRENT_TIMESTAMP(),
    DATE '1990-01-01',
    'Administrador padrão do sistema.',
    'https://example.com/avatar-admin.png',
    'Rua Exemplo, 123 - Cidade',
    'IFBA',
    'Administrativo'
);
