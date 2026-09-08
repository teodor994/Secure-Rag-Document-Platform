CREATE TABLE USERS(
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email   VARCHAR(50) NOT NULL UNIQUE ,
    password_hash   VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);