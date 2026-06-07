INSERT INTO USERS (email, password)
VALUES ('user@gmail.com', '{noop}user'),
       ('admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLE (user_id, role)
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (2, 'USER');

INSERT INTO RESTAURANT (name)
VALUES ('McDonalds'),
       ('Burger King'),
       ('KFC'),
       ('Subway');

INSERT INTO MENU (menu_date, restaurant_id)
VALUES ('2026-06-07', 1),
       ('2026-06-07', 2),
       ('2026-06-07', 3),
       ('2026-06-07', 4);

INSERT INTO MENU_ITEM (name, price, menu_id)
VALUES ('Big Mac', 6.99, 1),
       ('French Fries', 2.99, 1),
       ('Coca-Cola', 1.99, 1),
       ('Whopper', 7.49, 2),
       ('Onion Rings', 3.49, 2),
       ('Sprite', 1.99, 2),
       ('Original Recipe Chicken', 8.99, 3),
       ('Mashed Potatoes', 2.49, 3),
       ('Pepsi', 1.99, 3),
       ('Italian B.M.T.', 7.99, 4),
       ('Turkey Breast Sandwich', 6.99, 4),
       ('Chocolate Chip Cookie', 1.49, 4);