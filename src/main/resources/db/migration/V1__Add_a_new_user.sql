CREATE TABLE "public".user
(
    username VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    update_at TIMESTAMP NOT NULL,
    suspended BOOLEAN DEFAULT FALSE ,
    firstName VARCHAR(100),
    lastName VARCHAR(100),
    password VARCHAR(500)
);