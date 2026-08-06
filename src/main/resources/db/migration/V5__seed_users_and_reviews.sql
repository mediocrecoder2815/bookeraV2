insert into users (user_id, avatar_url, hashed_password, name, surname, username) values
    ('11111111-1111-1111-1111-111111111111', 'https://example.com/users/alice.jpg', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhiGzQpY9zUjQp6rN0kYx3YwLh0p1z0K', 'Alice', 'Johnson', 'alice'),
    ('22222222-2222-2222-2222-222222222222', 'https://example.com/users/bob.jpg', '$2a$10$9VJ7mWxKfB7Lf6Yv7F7G0e5J9L0zYjWp6rN0kYx3YwLh0p1z0K', 'Bob', 'Smith', 'bobsmith'),
    ('33333333-3333-3333-3333-333333333333', 'https://example.com/users/carol.jpg', '$2a$10$4TkP8qYrC2NvL5sXb1Wp4h8J6L0zYjWp6rN0kYx3YwLh0p1z0K', 'Carol', 'Davis', 'carold'),
    ('44444444-4444-4444-4444-444444444444', 'https://example.com/users/dave.jpg', '$2a$10$2FgR6sWmD1KvP9tCj4Qm8a6J0L0zYjWp6rN0kYx3YwLh0p1z0K', 'Dave', 'Wilson', 'davew'),
    ('55555555-5555-5555-5555-555555555555', 'https://example.com/users/erin.jpg', '$2a$10$6HnS3xTbE2JwQ7rVk9Xn5d3K1L0zYjWp6rN0kYx3YwLh0p1z0K', 'Erin', 'Brown', 'erinerin');

insert into book_user (book_id, user_id) values
    ((select book_id from books where isbn = '9780747532743'), '11111111-1111-1111-1111-111111111111'),
    ((select book_id from books where isbn = '9780385121675'), '11111111-1111-1111-1111-111111111111'),
    ((select book_id from books where isbn = '9780451524935'), '22222222-2222-2222-2222-222222222222'),
    ((select book_id from books where isbn = '9781476733951'), '33333333-3333-3333-3333-333333333333'),
    ((select book_id from books where isbn = '9780141439747'), '44444444-4444-4444-4444-444444444444');

insert into reviews (user_id, book_id, content, rating) values
    ('11111111-1111-1111-1111-111111111111', (select book_id from books where isbn = '9780747532743'), 'A magical beginning to an incredible series.', 5),
    ('11111111-1111-1111-1111-111111111111', (select book_id from books where isbn = '9780385121675'), 'Terrifying and brilliantly written.', 4),
    ('22222222-2222-2222-2222-222222222222', (select book_id from books where isbn = '9780451524935'), 'A chillingly relevant dystopian classic.', 5),
    ('33333333-3333-3333-3333-333333333333', (select book_id from books where isbn = '9781476733951'), 'Gripping world-building and a tense plot.', 4),
    ('44444444-4444-4444-4444-444444444444', (select book_id from books where isbn = '9780141439747'), 'A moving story of hardship and redemption.', 4),
    ('55555555-5555-5555-5555-555555555555', (select book_id from books where isbn = '9780452262935'), 'Short, sharp and unforgettable.', 5);