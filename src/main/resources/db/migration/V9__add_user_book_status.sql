create table book_status(
    status_id smallserial primary key,
    name varchar(12) not null unique
);
insert into book_status(name)
values
    ('IN PLANS'),
    ('READING'),
    ('DONE');

create table user_books(
    user_id UUID references users(user_id),
    book_id bigint references books(book_id),
    status_id smallint references book_status(status_id),
    primary key (user_id, book_id)
);