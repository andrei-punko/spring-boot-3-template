
create table ARTICLES (
    ID int not null,
    TITLE varchar(100) not null,
    SUMMARY varchar(255),
    TS timestamp,
    TEXT varchar not null,
    AUTHOR varchar(50) not null,
    DATE_CREATED timestamp not null,
    DATE_UPDATED timestamp not null,
    primary key (id)
);

create sequence ARTICLE_ID_SEQ start with 1 increment by 1;

create index ARTICLE_TITLE_IDX on ARTICLES(TITLE);
create index ARTICLE_DATE_CREATED_IDX on ARTICLES(DATE_CREATED);
create index ARTICLE_TS_IDX on ARTICLES(TS);
