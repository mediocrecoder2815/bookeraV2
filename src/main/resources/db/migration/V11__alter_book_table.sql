alter table books
    add column avg_rating numeric default 0.0,
    add column review_count int