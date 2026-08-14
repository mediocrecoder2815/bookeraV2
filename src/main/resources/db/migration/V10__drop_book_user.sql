alter table user_books
    drop constraint user_books_book_id_fkey,
    add constraint user_books_book_id_fkey
        foreign key (book_id) references books(book_id) on delete cascade;

drop table book_user;
