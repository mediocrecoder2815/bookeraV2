insert into author_book (book_id, author_id) values
    ((select book_id from books where isbn = '9780747532743'), (select author_id from authors where surname = 'Rowling')),
    ((select book_id from books where isbn = '9780747538493'), (select author_id from authors where surname = 'Rowling')),
    ((select book_id from books where isbn = '9780385121675'), (select author_id from authors where surname = 'King')),
    ((select book_id from books where isbn = '9780450411436'), (select author_id from authors where surname = 'King')),
    ((select book_id from books where isbn = '9781476733951'), (select author_id from authors where surname = 'Howey')),
    ((select book_id from books where isbn = '9781476751397'), (select author_id from authors where surname = 'Howey')),
    ((select book_id from books where isbn = '9780451524935'), (select author_id from authors where surname = 'Orwell')),
    ((select book_id from books where isbn = '9780452262935'), (select author_id from authors where surname = 'Orwell')),
    ((select book_id from books where isbn = '9780141439600'), (select author_id from authors where surname = 'Dickens')),
    ((select book_id from books where isbn = '9780141439747'), (select author_id from authors where surname = 'Dickens'));

insert into book_category (book_id, category_id) values
    ((select book_id from books where isbn = '9780747532743'), (select category_id from categories where category_name = 'Fantasy')),
    ((select book_id from books where isbn = '9780747538493'), (select category_id from categories where category_name = 'Fantasy')),
    ((select book_id from books where isbn = '9780385121675'), (select category_id from categories where category_name = 'Horror')),
    ((select book_id from books where isbn = '9780450411436'), (select category_id from categories where category_name = 'Horror')),
    ((select book_id from books where isbn = '9781476733951'), (select category_id from categories where category_name = 'Sci-Fi')),
    ((select book_id from books where isbn = '9781476751397'), (select category_id from categories where category_name = 'Sci-Fi')),
    ((select book_id from books where isbn = '9780451524935'), (select category_id from categories where category_name = 'Sci-Fi')),
    ((select book_id from books where isbn = '9780452262935'), (select category_id from categories where category_name = 'Mystery')),
    ((select book_id from books where isbn = '9780141439600'), (select category_id from categories where category_name = 'History')),
    ((select book_id from books where isbn = '9780141439747'), (select category_id from categories where category_name = 'Children'));