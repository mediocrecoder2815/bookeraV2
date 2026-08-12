create table roles(
    role_id smallserial primary key,
    role_name varchar(5) unique not null
);

create table user_roles(
    user_id uuid references users(user_id),
    role_id smallint references roles(role_id),
    primary key (user_id, role_id)
);

INSERT INTO roles(role_name)
VALUES
    ('USER'),
    ('ADMIN');