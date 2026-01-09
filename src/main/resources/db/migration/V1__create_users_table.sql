CREATE TABLE users (
    id                  BIGSERIAL PRIMARY KEY,
    username            VARCHAR(50) NOT NULL,
    email               VARCHAR(100) NOT NULL,
    password_hash       VARCHAR(255) NOT NULL,
    first_name          VARCHAR(50),
    last_name           VARCHAR(50),
    date_of_birth       DATE,
    gender              VARCHAR(20) CHECK (
                            gender IN ('MALE', 'FEMALE', 'OTHER', 'PREFER_NOT_TO_SAY')
                        ),
    preferred_units     VARCHAR(10) DEFAULT 'METRIC'
                        CHECK (preferred_units IN ('METRIC', 'IMPERIAL')),
    timezone            VARCHAR(50) DEFAULT 'UTC',
    role                VARCHAR(20) DEFAULT 'USER'
                        CHECK (role IN ('USER', 'TRAINER', 'ADMIN')),
    is_active           BOOLEAN DEFAULT TRUE,
    is_email_verified   BOOLEAN DEFAULT FALSE,
    last_login_at       TIMESTAMP,
    failed_login_attempts INTEGER DEFAULT 0,
    account_locked_until TIMESTAMP,
    password_changed_at TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP,
    CONSTRAINT check_age
        CHECK (date_of_birth IS NULL OR date_of_birth < CURRENT_DATE - INTERVAL '13 years')
);

CREATE UNIQUE INDEX uq_users_email_active
    ON users(email)
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX uq_users_username_active
    ON users(username)
    WHERE deleted_at IS NULL;

CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(is_active) WHERE is_active = TRUE;