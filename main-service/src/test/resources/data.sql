insert into users (name, email)
VALUES ('user1', 'user1@mail.ru'),
       ('user2', 'user2@mail.ru'),
       ('user3', 'user3@mail.ru');

insert into categories (name)
VALUES ('category1'),
       ('category2'),
       ('category3');

insert into events (annotation, category_id, created, description, event_date, initiator_id, latitude, longitude, paid, participant_limit, published, request_moderation, state, title)
VALUES ('annotation1', 1, '2022-09-16 12:00:00', 'description1', '2022-09-30 12:00:00', 1, 100.1, 200.1, true, 0, null, true, 'PENDING', 'title1'),
       ('annotation2', 2, '2022-09-14 12:00:00', 'description2', '2022-09-29 18:00:00', 1, 100.1, 200.1, false, 5, '2022-09-16 14:00:00', true, 'PUBLISHED', 'title2');