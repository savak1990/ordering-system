
create table if not exists users(
  id bigint auto_increment,
  name varchar(70),
  balance decimal,
  primary key (id)
);

create table if not exists user_transaction(
  id bigint auto_increment,
  user_id bigint,
  amount decimal,
  transaction_date timestamp,
  primary key (id),
  foreign key (user_id) references users(id) on delete cascade
);