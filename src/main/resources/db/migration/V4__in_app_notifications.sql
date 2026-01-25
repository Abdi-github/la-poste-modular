-- ===========================================================
-- V4__in_app_notifications.sql
-- La Poste Modular — In-App Notification System
-- Supports individual, role-based, branch-based, and broadcast notifications
-- ===========================================================

CREATE TABLE in_app_notification (
    id               BIGSERIAL PRIMARY KEY,
    -- Targeting: NULL means "all". Combine for fine-grained targeting.
    target_employee_id BIGINT,        -- specific employee (NULL = not individual)
    target_role        VARCHAR(30),   -- ADMIN, BRANCH_MANAGER, EMPLOYEE (NULL = all roles)
    target_branch_id   BIGINT,        -- specific branch (NULL = all branches)
    -- Content
    title              VARCHAR(255)   NOT NULL,
    message            TEXT           NOT NULL,
    category           VARCHAR(30)    NOT NULL DEFAULT 'INFO',  -- INFO, WARNING, URGENT, SYSTEM
    reference_url      VARCHAR(500),  -- optional deep link, e.g. /parcels, /deliveries
    -- Sender
    sender_employee_id BIGINT,        -- who sent it (NULL = system-generated)
    -- Metadata
    created_at         TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP,
    created_by         VARCHAR(255)
);

CREATE INDEX idx_inapp_target_employee ON in_app_notification(target_employee_id);
CREATE INDEX idx_inapp_target_role     ON in_app_notification(target_role);
CREATE INDEX idx_inapp_target_branch   ON in_app_notification(target_branch_id);
CREATE INDEX idx_inapp_created_at      ON in_app_notification(created_at DESC);

-- Read receipts: tracks which employee has read which notification
CREATE TABLE in_app_notification_read (
    id              BIGSERIAL PRIMARY KEY,
    notification_id BIGINT    NOT NULL REFERENCES in_app_notification(id) ON DELETE CASCADE,
    employee_id     BIGINT    NOT NULL,
    read_at         TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (notification_id, employee_id)
);

CREATE INDEX idx_inapp_read_employee ON in_app_notification_read(employee_id);


-- ════════════════════════════════════════════════════════
-- Seed some initial in-app notifications
-- ════════════════════════════════════════════════════════

-- System broadcast to all employees
INSERT INTO in_app_notification (title, message, category, reference_url, created_at) VALUES
('Systemwartung geplant', 'Am Samstag, 12. April 2026, findet zwischen 02:00 und 06:00 Uhr eine geplante Systemwartung statt. Bitte speichern Sie Ihre Arbeit rechtzeitig.', 'SYSTEM', NULL, now() - interval '2 days'),
('Neue Zustellungsrichtlinien', 'Bitte beachten Sie die aktualisierten Zustellungsrichtlinien ab sofort. Details finden Sie im internen Handbuch.', 'INFO', '/deliveries', now() - interval '5 days');

-- Targeted to ADMIN role
INSERT INTO in_app_notification (title, message, category, target_role, reference_url, created_at) VALUES
('Monatsbericht verfügbar', 'Der Betriebsbericht für März 2026 ist jetzt verfügbar. Bitte überprüfen Sie die Kennzahlen.', 'INFO', 'ADMIN', '/dashboard', now() - interval '1 day');

-- Targeted to BRANCH_MANAGER role
INSERT INTO in_app_notification (title, message, category, target_role, reference_url, created_at) VALUES
('Personalplanung Q2', 'Bitte aktualisieren Sie die Personalplanung für das zweite Quartal bis zum 15. April.', 'WARNING', 'BRANCH_MANAGER', '/employees', now() - interval '3 days');

-- Targeted to branch 1 (Zürich)
INSERT INTO in_app_notification (title, message, category, target_branch_id, created_at) VALUES
('Renovierung Filiale Zürich', 'Die Filiale Zürich Sihlpost wird ab dem 20. April teilweise renoviert. Bitte informieren Sie Ihre Teams.', 'WARNING', 1, now() - interval '1 day');

-- Individual notification to employee 4 (Peter Keller, courier)
INSERT INTO in_app_notification (title, message, category, target_employee_id, reference_url, sender_employee_id, created_at) VALUES
('Route-Änderung morgen', 'Ihre Zustellroute für morgen wurde aktualisiert. Bitte prüfen Sie die neue Route vor Dienstbeginn.', 'URGENT', 4, '/deliveries', 1, now() - interval '6 hours');

-- Individual notification to employee 7 (Anna Weber, clerk)
INSERT INTO in_app_notification (title, message, category, target_employee_id, sender_employee_id, created_at) VALUES
('Willkommen zurück!', 'Schön, dass Sie aus dem Urlaub zurück sind. Bei Fragen zur aktuellen Lage wenden Sie sich an Ihre Filialleitung.', 'INFO', 7, 9, now() - interval '12 hours');

-- Mark some as read by certain employees
INSERT INTO in_app_notification_read (notification_id, employee_id, read_at) VALUES
(1, 1, now() - interval '1 day'),   -- Hans Müller read "Systemwartung"
(1, 4, now() - interval '1 day'),   -- Peter Keller read "Systemwartung"
(2, 1, now() - interval '4 days'),  -- Hans Müller read "Neue Zustellungsrichtlinien"
(3, 1, now() - interval '12 hours'); -- Hans Müller read "Monatsbericht"

