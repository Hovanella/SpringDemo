create table user
(
    id       int primary key AUTO_INCREMENT,
    login    varchar(40) NOT NULL,
    password varchar(20) NOT NULL,
    is_Admin bool        NOT NULL
);

create table genre
(
    id   int primary key AUTO_INCREMENT,
    name varchar(40) NOT NULL
);

create table author
(
    id   int primary key AUTO_INCREMENT,
    name varchar(40) NOT NULL
);

create table track
(
    id        int primary key AUTO_INCREMENT,
    name      varchar(40) NOT NULL,
    author_id int         NOT NULL,

    foreign key (author_id) references author (id)
);

create table rating(
  id       int primary key AUTO_INCREMENT,
  rating_mark   int check ( 0-9 )  NOT NULL,
  user_id  int         NOT NULL,

    foreign key (user_id) references user (id)
);