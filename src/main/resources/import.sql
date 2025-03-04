SHOW TABLES;
--INSERT INTO cliente (id, nome, endereco, codigo, is_premium) VALUES (10001, 'sicrano', 'rua aquela', '123456', false);
INSERT INTO cliente (id, nome, endereco, codigo, is_premium) VALUES (1, 'Jhonata', 'Campina Grande - PB', '123456', true);
INSERT INTO fornecedor (id, nome, cnpj, codigo) VALUES (1, 'Thayla', '782756785', '123456');
INSERT INTO cafe (id,nome,origem,tipo,perfil_sensorial,preco,is_disponivel,is_premium,tamanho_embalagem,fornecedor_id) VALUES (1,'Café Arábica','Brasil','grão','Frutado, ácido, suave',25.50,true,true,'500g',1);
INSERT INTO cafe (id,nome,origem,tipo,perfil_sensorial,preco,is_disponivel,is_premium,tamanho_embalagem,fornecedor_id) VALUES (2, 'Café Robusta', 'Vietnam', 'moído', 'Amargo, forte, encorpado', 30.00, true, true, '250g', 1);
