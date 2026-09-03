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

INSERT INTO MENU_ITEM (name, price, restaurant_id, menu_date)
VALUES ('Big Mac', 6.99, 1, CURRENT_DATE),
       ('French Fries', 2.99, 1, CURRENT_DATE),
       ('Coca-Cola', 1.99, 1, CURRENT_DATE),
       ('Whopper', 7.49, 2, CURRENT_DATE),
       ('Onion Rings', 3.49, 2, CURRENT_DATE),
       ('Sprite', 1.99, 2, CURRENT_DATE),
       ('Original Recipe Chicken', 8.99, 3, CURRENT_DATE),
       ('Mashed Potatoes', 2.49, 3, CURRENT_DATE),
       ('Pepsi', 1.99, 3, CURRENT_DATE),
       ('Italian B.M.T.', 7.99, 4, CURRENT_DATE),
       ('Turkey Breast Sandwich', 6.99, 4, CURRENT_DATE),
       ('Chocolate Chip Cookie', 1.49, 4, CURRENT_DATE);

INSERT INTO VOTE (restaurant_id, user_id, vote_date)
VALUES (3, 2, CURRENT_DATE);