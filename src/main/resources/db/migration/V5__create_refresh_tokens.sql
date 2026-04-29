CREATE TABLE  refresh_tokens
(
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_refresh_user FOREIGN KEY(user_id) REFERENCES users(id)
);