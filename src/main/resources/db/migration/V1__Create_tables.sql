
create table articles (
    id int not null,
    title varchar(100) not null,
    summary varchar(255),
    ts timestamp,
    text varchar not null,
    author varchar(50) not null,
    date_created timestamp not null,
    date_updated timestamp not null,
    primary key (id)
);

create sequence article_id_seq START WITH 1 INCREMENT BY 1;

create index article_title_idx on articles(title);
create index article_date_created_idx on articles(date_created);
create index article_ts_idx on articles(ts);
