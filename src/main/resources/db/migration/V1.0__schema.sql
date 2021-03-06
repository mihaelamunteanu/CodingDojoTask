CREATE TABLE weather (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    temperature DOUBLE PRECISION
);

CREATE TABLE app_user (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    active boolean not null,
    expire_date date,
    locked_user boolean not null,
    password varchar(255),
    start_date date,
    user_name varchar(255)
);

CREATE TABLE role (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    role_name varchar(255),
    description varchar(255)
);

CREATE TABLE user_role (
    user_fk INT not null,
    role_fk INT not null,
    FOREIGN KEY(user_fk) REFERENCES app_user(id),
    FOREIGN KEY(role_fk) REFERENCES role(id)
);