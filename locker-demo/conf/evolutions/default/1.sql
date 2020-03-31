
# --- !Ups

CREATE SEQUENCE user_id_seq;

CREATE TABLE users (
  user_id                                     BIGINT NOT NULL DEFAULT NEXTVAL('user_id_seq'),
  username                                    VARCHAR NOT NULL UNIQUE,
  password                                    VARCHAR NOT NULL UNIQUE,
  name                                        VARCHAR NOT NULL,
  email                                       VARCHAR NOT NULL UNIQUE,
  phone_number                                BIGINT NOT NULL UNIQUE,
  PRIMARY KEY (user_id),
  CONSTRAINT valid_phone
    CHECK ("phone_number" <= 99999999999)
);

# --- !Downs

DROP TABLE users;
DROP SEQUENCE user_id_seq;
