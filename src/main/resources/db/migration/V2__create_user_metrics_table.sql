CREATE TABLE user_body_metrics (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,

    weight_kg       DECIMAL(5,2) CHECK (weight_kg > 0 AND weight_kg < 500),
    body_fat_pct    DECIMAL(4,1) CHECK (body_fat_pct >= 0 AND body_fat_pct <= 100),

    -- Optional / versioned
    height_cm       DECIMAL(5,2) CHECK (height_cm > 0 AND height_cm < 300),

    -- Derived (store only if enforced)
    bmi             DECIMAL(4,1),

    measurement_time VARCHAR(20)
        CHECK (measurement_time IN ('MORNING', 'AFTERNOON', 'EVENING')),

    measurement_method VARCHAR(50)
        CHECK (measurement_method IN (
            'SCALE', 'SMART_SCALE', 'CALIPERS',
            'DEXA_SCAN', 'MANUAL', 'OTHER'
        )),

    notes           TEXT,

    recorded_at     TIMESTAMP NOT NULL,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_body_metrics_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);