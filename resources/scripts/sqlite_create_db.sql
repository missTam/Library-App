--   Autorin: Tamara T. 
--   am/um: 2020-05-12
--   Typ: SQLite

CREATE TABLE authors 
    (
     _id INTEGER PRIMARY KEY ,
     first_name VARCHAR NOT NULL ,
     last_name VARCHAR NOT NULL
    );

CREATE TABLE genres
    (
     _id INTEGER PRIMARY KEY ,
     name VARCHAR NOT NULL
    );

CREATE TABLE books 
    (
     _id INTEGER PRIMARY KEY ,
     title VARCHAR NOT NULL ,
     publication_year INTEGER ,
     library_card INTEGER ,
     author INTEGER NOT NULL,
     FOREIGN KEY (library_card) REFERENCES library_cards (_id) ,
     FOREIGN KEY (author) REFERENCES authors (_id)
    );


CREATE TABLE library_cards 
    (
     _id INTEGER PRIMARY KEY ,
     first_name VARCHAR NOT NULL ,
     last_name VARCHAR NOT NULL ,
     valid_until DATE NOT NULL 
    );

CREATE TABLE relation
    (
     book INTEGER NOT NULL ,
     genre INTEGER NOT NULL
    );