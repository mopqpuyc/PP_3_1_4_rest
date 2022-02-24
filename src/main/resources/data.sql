insert into users (name, age, username, password)
    values ('Admin2', 30, 'Admin', '$2y$10$LA3C7cUEW50jSImPQq2C2.2HiBNFI0C6dQKOsbCr1mhg2xhqG2ut6');
insert into roles (id, id) values (1, 'ADMIN');
insert into users_roles (user_id, roles_id) values (1, 1);
