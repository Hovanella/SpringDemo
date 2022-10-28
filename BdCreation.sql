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
    genre_id  int         NOT NULL,
	path varchar(40) NOT NULL,

    foreign key (author_id) references author (id)
	foreign key (genre_id) references genre (id)
);

create table rating(
  id       int primary key AUTO_INCREMENT,
  mark   int check ( 0-9 )  NOT NULL,
  user_id  int         NOT NULL,
  track_id int NOT NULL,

    foreign key (user_id) references user (id)
    foreign key (track_id) references track (id)
);

