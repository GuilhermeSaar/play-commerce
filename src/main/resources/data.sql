
INSERT INTO tb_client (name, cpf, email, phone, date_register, password, role)
    VALUES ('admin', '13685614578', 'teste@gmail.com', '19999563025', NOW(), '$2a$10$d7RMUSY0Ofd8bjt.fmRdWu3L.u3fhC0skvD3NuZr9Gc5w4ehH3xZO', 'ADMIN');


INSERT INTO tb_category (name)
VALUES
    ('Ação'),
    ('RPG'),
    ('Aventura');

INSERT INTO tb_game
(name, description, developer, publisher, release_date, price, classification, link_download, category_id)
VALUES
    ('Shadow of Eternia',
     'RPG de mundo aberto onde o jogador enfrenta criaturas das trevas para restaurar o equilíbrio do reino.',
     'Everbloom Studios',
     'Nova Prime Publishing',
     '2023-05-14',
     199.90,
     '12+',
     'https://download.com/shadow-eternia',
     2),

    ('CyberStrike Protocol',
     'Jogo de ação futurista com combates rápidos e missões táticas em uma metrópole cibernética.',
     'NeonWolf Interactive',
     'DarkPoint Media',
     '2022-11-02',
     149.50,
     '16+',
     'https://download.com/cyberstrike-protocol',
     1),

    ('Mystic Tales: The Lost Oracle',
     'Aventura narrativa com puzzles e escolhas que alteram profundamente a história.',
     'BlueMoon Artworks',
     'SilverKey Entertainment',
     '2021-09-30',
     89.99,
     '10+',
     'https://download.com/mystic-tales',
     3),

    ('Metal Fangs Arena',
     'Shooter multiplayer frenético com arenas cheias de armadilhas e armas personalizáveis.',
     'IronByte Games',
     'ThunderForge Studios',
     '2024-02-18',
     129.00,
     '18+',
     'https://download.com/metal-fangs',
     1),

    ('Galactic Frontier EX',
     'Simulador espacial onde o jogador explora galáxias, expande frotas e negocia com civilizações alienígenas.',
     'StarCore Labs',
     'Infinity Gate Publishing',
     '2023-12-07',
     249.99,
     'Livre',
     'https://download.com/galactic-frontier-ex',
     2);
