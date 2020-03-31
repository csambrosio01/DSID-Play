
# --- !Ups

CREATE SEQUENCE note_id_seq;

CREATE OR REPLACE FUNCTION trigger_set_timestamp()
    RETURNS TRIGGER AS $$
    BEGIN
        NEW.updated_at = NOW();
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE TABLE notes (
    note_id                                     BIGINT NOT NULL DEFAULT NEXTVAL('note_id_seq'),
    user_id                                     BIGINT NOT NULL,
    content                                     VARCHAR NOT NULL,
    description                                 VARCHAR NOT NULL,
    created_at                                  TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    updated_at                                  TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    PRIMARY KEY (note_id)
);

CREATE TRIGGER set_timestamp
    BEFORE UPDATE ON notes
    FOR EACH ROW
    EXECUTE PROCEDURE trigger_set_timestamp();

# --- !Downs

DROP TRIGGER set_timestamp ON notes;
DROP TABLE notes;
DROP FUNCTION trigger_set_timestamp();
DROP SEQUENCE note_id_seq;
