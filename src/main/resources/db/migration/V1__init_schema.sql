-- =====================================================
-- V1__init_schema.sql
-- La Poste Modular Monolith — Full initial schema
-- =====================================================

-- ── User Module ─────────────────────────────────────

CREATE TABLE permission (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP,
    created_by  VARCHAR(255)
);

CREATE TABLE role (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP,
    created_by  VARCHAR(255)
);

CREATE TABLE role_permission (
    role_id       BIGINT NOT NULL REFERENCES role(id),
    permission_id BIGINT NOT NULL REFERENCES permission(id),
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE customer (
    id               BIGSERIAL PRIMARY KEY,
    customer_number  VARCHAR(20)  NOT NULL UNIQUE,
    first_name       VARCHAR(100) NOT NULL,
    last_name        VARCHAR(100) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    phone            VARCHAR(30),
    keycloak_user_id VARCHAR(255) UNIQUE,
    status           VARCHAR(30)  NOT NULL,
    preferred_locale VARCHAR(5)   NOT NULL DEFAULT 'de',
    created_at       TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at       TIMESTAMP,
    created_by       VARCHAR(255)
);

CREATE TABLE employee (
    id                 BIGSERIAL PRIMARY KEY,
    employee_number    VARCHAR(20)  NOT NULL UNIQUE,
    first_name         VARCHAR(100) NOT NULL,
    last_name          VARCHAR(100) NOT NULL,
    email              VARCHAR(150) NOT NULL UNIQUE,
    phone              VARCHAR(30),
    keycloak_user_id   VARCHAR(255) UNIQUE,
    role               VARCHAR(30)  NOT NULL,
    status             VARCHAR(30)  NOT NULL,
    assigned_branch_id BIGINT,
    hire_date          DATE         NOT NULL,
    preferred_locale   VARCHAR(5)   NOT NULL DEFAULT 'de',
    created_at         TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP,
    created_by         VARCHAR(255)
);

-- ── Address Module ──────────────────────────────────

CREATE TABLE swiss_address (
    id           BIGSERIAL PRIMARY KEY,
    zip_code     VARCHAR(10)  NOT NULL,
    city         VARCHAR(100) NOT NULL,
    canton       VARCHAR(5)   NOT NULL,
    municipality VARCHAR(100),
    created_at   TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP,
    created_by   VARCHAR(255)
);

-- ── Branch Module ───────────────────────────────────

CREATE TABLE branch (
    id          BIGSERIAL PRIMARY KEY,
    branch_code VARCHAR(20)  NOT NULL UNIQUE,
    type        VARCHAR(30)  NOT NULL,
    status      VARCHAR(30)  NOT NULL,
    street      VARCHAR(255),
    zip_code    VARCHAR(10),
    city        VARCHAR(100),
    canton      VARCHAR(5),
    phone       VARCHAR(30),
    email       VARCHAR(150),
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION,
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP,
    created_by  VARCHAR(255)
);

CREATE TABLE branch_translation (
    id          BIGSERIAL PRIMARY KEY,
    branch_id   BIGINT      NOT NULL REFERENCES branch(id) ON DELETE CASCADE,
    locale      VARCHAR(5)  NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    UNIQUE (branch_id, locale)
);

-- ── Parcel Module ───────────────────────────────────

CREATE TABLE parcel (
    id                  BIGSERIAL PRIMARY KEY,
    tracking_number     VARCHAR(20)    NOT NULL UNIQUE,
    status              VARCHAR(30)    NOT NULL,
    type                VARCHAR(30)    NOT NULL,
    weight_kg           DOUBLE PRECISION,
    length_cm           DOUBLE PRECISION,
    width_cm            DOUBLE PRECISION,
    height_cm           DOUBLE PRECISION,
    price               NUMERIC(10, 2),
    sender_customer_id  BIGINT,
    origin_branch_id    BIGINT,
    sender_name         VARCHAR(255),
    sender_street       VARCHAR(255),
    sender_zip_code     VARCHAR(10),
    sender_city         VARCHAR(100),
    sender_phone        VARCHAR(30),
    recipient_name      VARCHAR(255)   NOT NULL,
    recipient_street    VARCHAR(255),
    recipient_zip_code  VARCHAR(10),
    recipient_city      VARCHAR(100),
    recipient_phone     VARCHAR(30),
    created_at          TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP,
    created_by          VARCHAR(255)
);

CREATE INDEX idx_parcel_tracking ON parcel(tracking_number);
CREATE INDEX idx_parcel_status   ON parcel(status);
CREATE INDEX idx_parcel_sender   ON parcel(sender_customer_id);

-- ── Delivery Module ─────────────────────────────────

CREATE TABLE delivery_route (
    id                   BIGSERIAL PRIMARY KEY,
    route_code           VARCHAR(30) NOT NULL UNIQUE,
    branch_id            BIGINT      NOT NULL,
    assigned_employee_id BIGINT,
    status               VARCHAR(30) NOT NULL,
    date                 DATE        NOT NULL,
    created_at           TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at           TIMESTAMP,
    created_by           VARCHAR(255)
);

CREATE TABLE delivery_slot (
    id                  BIGSERIAL PRIMARY KEY,
    route_id            BIGINT      NOT NULL REFERENCES delivery_route(id) ON DELETE CASCADE,
    tracking_number     VARCHAR(20) NOT NULL,
    sequence_order      INTEGER     NOT NULL,
    status              VARCHAR(30) NOT NULL,
    recipient_signature VARCHAR(255),
    created_at          TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP,
    created_by          VARCHAR(255)
);

CREATE TABLE delivery_attempt (
    id                BIGSERIAL PRIMARY KEY,
    delivery_slot_id  BIGINT      NOT NULL REFERENCES delivery_slot(id) ON DELETE CASCADE,
    result            VARCHAR(30) NOT NULL,
    notes             TEXT,
    attempt_timestamp TIMESTAMP   NOT NULL,
    created_at        TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP,
    created_by        VARCHAR(255)
);

CREATE TABLE pickup_request (
    id                  BIGSERIAL PRIMARY KEY,
    customer_id         BIGINT      NOT NULL,
    pickup_street       VARCHAR(255) NOT NULL,
    pickup_zip_code     VARCHAR(10) NOT NULL,
    pickup_city         VARCHAR(100) NOT NULL,
    preferred_date      DATE        NOT NULL,
    preferred_time_from TIME,
    preferred_time_to   TIME,
    status              VARCHAR(30) NOT NULL,
    created_at          TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP,
    created_by          VARCHAR(255)
);

-- ── Tracking Module ─────────────────────────────────

CREATE TABLE tracking_record (
    id                 BIGSERIAL PRIMARY KEY,
    tracking_number    VARCHAR(20) NOT NULL UNIQUE,
    current_status     VARCHAR(30) NOT NULL,
    current_branch_id  BIGINT,
    estimated_delivery TIMESTAMP,
    created_at         TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP,
    created_by         VARCHAR(255)
);

CREATE TABLE tracking_event (
    id                     BIGSERIAL PRIMARY KEY,
    tracking_number        VARCHAR(20)  NOT NULL,
    event_type             VARCHAR(30)  NOT NULL,
    branch_id              BIGINT,
    location               VARCHAR(255),
    description_key        VARCHAR(100),
    event_timestamp        TIMESTAMP    NOT NULL,
    scanned_by_employee_id VARCHAR(50),
    created_at             TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at             TIMESTAMP,
    created_by             VARCHAR(255)
);

CREATE INDEX idx_tracking_event_number ON tracking_event(tracking_number);

-- ── Notification Module ─────────────────────────────

CREATE TABLE notification_template (
    id            BIGSERIAL PRIMARY KEY,
    template_code VARCHAR(50) NOT NULL UNIQUE,
    type          VARCHAR(20) NOT NULL,
    event_type    VARCHAR(50) NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP,
    created_by    VARCHAR(255)
);

CREATE TABLE notification_template_translation (
    id          BIGSERIAL PRIMARY KEY,
    template_id BIGINT      NOT NULL REFERENCES notification_template(id) ON DELETE CASCADE,
    locale      VARCHAR(5)  NOT NULL,
    subject     VARCHAR(255) NOT NULL,
    body        TEXT         NOT NULL,
    UNIQUE (template_id, locale)
);

CREATE TABLE notification_log (
    id              BIGSERIAL PRIMARY KEY,
    recipient_email VARCHAR(150),
    recipient_phone VARCHAR(30),
    type            VARCHAR(20) NOT NULL,
    status          VARCHAR(20) NOT NULL,
    subject         VARCHAR(255),
    body            TEXT,
    reference_id    VARCHAR(50),
    event_type      VARCHAR(50),
    retry_count     INTEGER     NOT NULL DEFAULT 0,
    created_at      TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(255)
);

CREATE TABLE notification_preference (
    id               BIGSERIAL PRIMARY KEY,
    customer_id      BIGINT  NOT NULL UNIQUE,
    email_enabled    BOOLEAN NOT NULL DEFAULT true,
    sms_enabled      BOOLEAN NOT NULL DEFAULT false,
    in_app_enabled   BOOLEAN NOT NULL DEFAULT true,
    preferred_locale VARCHAR(5) NOT NULL DEFAULT 'de',
    created_at       TIMESTAMP  NOT NULL DEFAULT now(),
    updated_at       TIMESTAMP,
    created_by       VARCHAR(255)
);

-- ── Spring Modulith Event Publication ───────────────

CREATE TABLE IF NOT EXISTS event_publication (
    id               UUID PRIMARY KEY,
    listener_id      TEXT      NOT NULL,
    event_type       TEXT      NOT NULL,
    serialized_event TEXT      NOT NULL,
    publication_date TIMESTAMP NOT NULL,
    completion_date  TIMESTAMP
);

