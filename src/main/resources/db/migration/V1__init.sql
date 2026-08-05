create table authors(
    author_id BIGSERIAL PRIMARY KEY,
    date_of_birth DATE NOT NULL,
    name VARCHAR(25) NOT NULL,
    surname varchar(25) NOT NULL,
    country varchar(2) NOT NULL,
    description varchar(255),
    photo_url varchar(255) UNIQUE
);

create table books(
    book_id bigserial primary key,
    name varchar(50) not null,
    isbn varchar(14) not null UNIQUE,
    description varchar(255),
    picture_url varchar(255) unique,
    date_of_publish date,
    total_pages smallint not null
);
CREATE TABLE author_book (
    book_id   BIGINT NOT NULL REFERENCES books(book_id),
    author_id BIGINT NOT NULL REFERENCES authors(author_id),
    PRIMARY KEY (author_id, book_id)
);
create table categories(
    category_id serial primary key ,
    category_name varchar(20) not null unique
);

create table book_category(
    category_id integer not null references categories(category_id),
    book_id bigint not null references books(book_id),
    primary key (category_id,book_id)
);

create table users(
    user_id UUID primary key,
    avatar_url varchar(255) unique,
    hashed_password varchar(255) not null,
    name varchar(50) not null,
    surname varchar(50) not null,
    username varchar(50) not null unique
);

create table book_user(
    user_id UUID not null references users(user_id),
    book_id bigint not null references books(book_id),
    primary key (book_id, user_id)
);

create table reviews(
    review_id bigserial primary key,
    user_id UUID not null references users(user_id),
    book_id bigint not null references books(book_id),
    content text not null,
    rating smallint not null,
    unique (user_id, book_id)
);