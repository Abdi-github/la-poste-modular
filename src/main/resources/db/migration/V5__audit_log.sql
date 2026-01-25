-- =====================================================
-- V5__audit_log.sql
-- La Poste Modular — Audit Trail for domain actions
-- =====================================================

CREATE TABLE audit_log (
    id            BIGSERIAL    PRIMARY KEY,
    entity_type   VARCHAR(50)  NOT NULL,
    entity_id     BIGINT       NOT NULL,
    action        VARCHAR(30)  NOT NULL,
    performed_by  VARCHAR(255) NOT NULL,
    details       TEXT,
    created_at    TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_entity ON audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_performed_by ON audit_log(performed_by);
CREATE INDEX idx_audit_created_at ON audit_log(created_at DESC);

