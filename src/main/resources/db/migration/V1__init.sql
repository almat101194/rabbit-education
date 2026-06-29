CREATE TABLE IF NOT EXISTS messages
(
    id         BIGSERIAL PRIMARY KEY,
    content    TEXT        NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW()
);