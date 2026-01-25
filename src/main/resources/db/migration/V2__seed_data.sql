-- ===========================================================
-- V2__seed_data.sql
-- La Poste Modular — Seed Data for Swiss Postal Platform
-- ===========================================================

-- ════════════════════════════════════════════════════════
-- 1. ROLES & PERMISSIONS
-- ════════════════════════════════════════════════════════
INSERT INTO permission (name, description) VALUES
    ('PARCEL_CREATE', 'Create new parcels'),
    ('PARCEL_VIEW', 'View parcel details'),
    ('PARCEL_CANCEL', 'Cancel parcels'),
    ('BRANCH_MANAGE', 'Manage branches'),
    ('EMPLOYEE_MANAGE', 'Manage employees'),
    ('DELIVERY_MANAGE', 'Manage delivery routes'),
    ('TRACKING_SCAN', 'Add tracking scan events'),
    ('NOTIFICATION_VIEW', 'View notification logs'),
    ('CUSTOMER_MANAGE', 'Manage customers'),
    ('ADMIN_FULL', 'Full admin access');

INSERT INTO role (name, description) VALUES
    ('ADMIN', 'System administrator with full access'),
    ('BRANCH_MANAGER', 'Branch manager with local operations access'),
    ('EMPLOYEE', 'General employee (courier, clerk, etc.)');

-- Admin gets all permissions
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM role r, permission p WHERE r.name = 'ADMIN';

-- Branch Manager permissions
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM role r, permission p
WHERE r.name = 'BRANCH_MANAGER' AND p.name IN ('PARCEL_CREATE','PARCEL_VIEW','PARCEL_CANCEL','DELIVERY_MANAGE','TRACKING_SCAN','EMPLOYEE_MANAGE','NOTIFICATION_VIEW');

-- Employee permissions
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM role r, permission p
WHERE r.name = 'EMPLOYEE' AND p.name IN ('PARCEL_CREATE','PARCEL_VIEW','TRACKING_SCAN','DELIVERY_MANAGE','CUSTOMER_MANAGE');

-- ════════════════════════════════════════════════════════
-- 2. BRANCHES (with translations)
-- ════════════════════════════════════════════════════════
INSERT INTO branch (id, branch_code, type, status, street, zip_code, city, canton, phone, email, latitude, longitude, created_at) VALUES
    (1, 'BR-ZRH01', 'POST_OFFICE', 'ACTIVE', 'Kasernenstrasse 95', '8004', 'Zürich', 'ZH', '+41 44 296 21 11', 'zuerich@post.ch', 47.3769, 8.5417, now()),
    (2, 'BR-BRN01', 'POST_OFFICE', 'ACTIVE', 'Viktoriastrasse 21', '3000', 'Bern', 'BE', '+41 31 338 11 11', 'bern@post.ch', 46.9480, 7.4474, now()),
    (3, 'BR-GVA01', 'POST_OFFICE', 'ACTIVE', 'Rue de Lausanne 15', '1201', 'Genève', 'GE', '+41 22 739 21 11', 'geneve@post.ch', 46.2044, 6.1432, now()),
    (4, 'BR-LUG01', 'AGENCY', 'ACTIVE', 'Via della Posta 2', '6900', 'Lugano', 'TI', '+41 91 807 11 11', 'lugano@post.ch', 46.0037, 8.9511, now()),
    (5, 'BR-BSL01', 'POST_OFFICE', 'ACTIVE', 'Rüdengasse 10', '4001', 'Basel', 'BS', '+41 61 266 21 11', 'basel@post.ch', 47.5596, 7.5886, now()),
    (6, 'BR-LZN01', 'AGENCY', 'ACTIVE', 'Bahnhofstrasse 11', '6003', 'Luzern', 'LU', '+41 41 229 21 11', 'luzern@post.ch', 47.0502, 8.3093, now()),
    (7, 'BR-STG01', 'AGENCY', 'ACTIVE', 'Bahnhofplatz 1', '9000', 'St. Gallen', 'SG', '+41 71 228 21 11', 'stgallen@post.ch', 47.4245, 9.3767, now()),
    (8, 'BR-BIE01', 'SORTING_CENTER', 'ACTIVE', 'Rue de Nidau 43', '2501', 'Biel/Bienne', 'BE', '+41 32 344 11 11', 'biel@post.ch', 47.1368, 7.2467, now());
SELECT setval('branch_id_seq', 8);

INSERT INTO branch_translation (branch_id, locale, name, description) VALUES
    -- Zürich
    (1, 'de', 'Post Zürich Sihlpost', 'Hauptpost Zürich — grösste Filiale der Schweiz'),
    (1, 'fr', 'Poste Zurich Sihlpost', 'Bureau principal de Zurich'),
    (1, 'it', 'Posta Zurigo Sihlpost', 'Ufficio postale principale di Zurigo'),
    (1, 'en', 'Post Zurich Sihlpost', 'Main post office Zurich — largest branch in Switzerland'),
    -- Bern
    (2, 'de', 'Post Bern Schanzenpost', 'Hauptpost Bern — Bundesstadt'),
    (2, 'fr', 'Poste Berne Schanzenpost', 'Bureau principal de Berne — Ville fédérale'),
    (2, 'it', 'Posta Berna Schanzenpost', 'Ufficio principale Berna — Città federale'),
    (2, 'en', 'Post Bern Schanzenpost', 'Main post office Bern — Federal City'),
    -- Genève
    (3, 'de', 'Post Genf', 'Hauptpost Genf — Westschweiz'),
    (3, 'fr', 'Poste Genève', 'Bureau principal de Genève — Suisse romande'),
    (3, 'it', 'Posta Ginevra', 'Ufficio principale Ginevra — Svizzera romanda'),
    (3, 'en', 'Post Geneva', 'Main post office Geneva — Western Switzerland'),
    -- Lugano
    (4, 'de', 'Post Lugano', 'Filiale Lugano — Tessin'),
    (4, 'fr', 'Poste Lugano', 'Succursale Lugano — Tessin'),
    (4, 'it', 'Posta Lugano', 'Filiale Lugano — Ticino'),
    (4, 'en', 'Post Lugano', 'Branch Lugano — Ticino'),
    -- Basel
    (5, 'de', 'Post Basel', 'Hauptpost Basel — Nordwestschweiz'),
    (5, 'fr', 'Poste Bâle', 'Bureau principal de Bâle'),
    (5, 'it', 'Posta Basilea', 'Ufficio principale Basilea'),
    (5, 'en', 'Post Basel', 'Main post office Basel'),
    -- Luzern
    (6, 'de', 'Post Luzern', 'Filiale Luzern — Zentralschweiz'),
    (6, 'fr', 'Poste Lucerne', 'Succursale Lucerne'),
    (6, 'it', 'Posta Lucerna', 'Filiale Lucerna'),
    (6, 'en', 'Post Lucerne', 'Branch Lucerne — Central Switzerland'),
    -- St. Gallen
    (7, 'de', 'Post St. Gallen', 'Filiale St. Gallen — Ostschweiz'),
    (7, 'fr', 'Poste Saint-Gall', 'Succursale Saint-Gall'),
    (7, 'it', 'Posta San Gallo', 'Filiale San Gallo'),
    (7, 'en', 'Post St. Gallen', 'Branch St. Gallen — Eastern Switzerland'),
    -- Biel/Bienne Sorting Center
    (8, 'de', 'Sortierzentrum Biel', 'Paketsortierzentrum Biel/Bienne'),
    (8, 'fr', 'Centre de tri Bienne', 'Centre de tri des colis Biel/Bienne'),
    (8, 'it', 'Centro di smistamento Bienne', 'Centro smistamento pacchi Biel/Bienne'),
    (8, 'en', 'Sorting Center Biel', 'Parcel sorting center Biel/Bienne');

-- ════════════════════════════════════════════════════════
-- 3. EMPLOYEES
-- ════════════════════════════════════════════════════════
INSERT INTO employee (employee_number, first_name, last_name, email, phone, role, status, assigned_branch_id, hire_date, preferred_locale) VALUES
    ('EMP-ADM01', 'Hans', 'Müller', 'hans.mueller@post.ch', '+41 79 100 0001', 'ADMIN', 'ACTIVE', 1, '2018-03-15', 'de'),
    ('EMP-MGR01', 'Sophie', 'Dubois', 'sophie.dubois@post.ch', '+41 79 100 0002', 'BRANCH_MANAGER', 'ACTIVE', 3, '2019-06-01', 'fr'),
    ('EMP-MGR02', 'Marco', 'Bernasconi', 'marco.bernasconi@post.ch', '+41 79 100 0003', 'BRANCH_MANAGER', 'ACTIVE', 4, '2020-01-10', 'it'),
    ('EMP-CRR01', 'Peter', 'Keller', 'peter.keller@post.ch', '+41 79 100 0004', 'EMPLOYEE', 'ACTIVE', 1, '2021-04-01', 'de'),
    ('EMP-CRR02', 'Marie', 'Favre', 'marie.favre@post.ch', '+41 79 100 0005', 'EMPLOYEE', 'ACTIVE', 3, '2021-07-15', 'fr'),
    ('EMP-CRR03', 'Luca', 'Rossi', 'luca.rossi@post.ch', '+41 79 100 0006', 'EMPLOYEE', 'ACTIVE', 4, '2022-02-01', 'it'),
    ('EMP-CLK01', 'Anna', 'Weber', 'anna.weber@post.ch', '+41 79 100 0007', 'EMPLOYEE', 'ACTIVE', 2, '2020-09-01', 'de'),
    ('EMP-CLK02', 'Thomas', 'Schneider', 'thomas.schneider@post.ch', '+41 79 100 0008', 'EMPLOYEE', 'ACTIVE', 5, '2023-01-15', 'de'),
    ('EMP-MGR03', 'Ursula', 'Fischer', 'ursula.fischer@post.ch', '+41 79 100 0009', 'BRANCH_MANAGER', 'ACTIVE', 2, '2017-11-01', 'de'),
    ('EMP-CRR04', 'Daniel', 'Meier', 'daniel.meier@post.ch', '+41 79 100 0010', 'EMPLOYEE', 'ON_LEAVE', 1, '2020-05-01', 'de');

-- ════════════════════════════════════════════════════════
-- 4. CUSTOMERS
-- ════════════════════════════════════════════════════════
INSERT INTO customer (customer_number, first_name, last_name, email, phone, status, preferred_locale) VALUES
    ('CUS-00001', 'Stefan', 'Widmer', 'stefan.widmer@example.ch', '+41 76 200 0001', 'ACTIVE', 'de'),
    ('CUS-00002', 'Isabelle', 'Martin', 'isabelle.martin@example.ch', '+41 76 200 0002', 'ACTIVE', 'fr'),
    ('CUS-00003', 'Giovanni', 'Ferrari', 'giovanni.ferrari@example.ch', '+41 76 200 0003', 'ACTIVE', 'it'),
    ('CUS-00004', 'Claudia', 'Baumann', 'claudia.baumann@example.ch', '+41 76 200 0004', 'ACTIVE', 'de'),
    ('CUS-00005', 'Pierre', 'Roux', 'pierre.roux@example.ch', '+41 76 200 0005', 'ACTIVE', 'fr');

-- ════════════════════════════════════════════════════════
-- 5. SWISS ADDRESSES (major cities)
-- ════════════════════════════════════════════════════════
INSERT INTO swiss_address (zip_code, city, canton, municipality) VALUES
    ('8001', 'Zürich', 'ZH', 'Zürich'),
    ('3000', 'Bern', 'BE', 'Bern'),
    ('1201', 'Genève', 'GE', 'Genève'),
    ('6900', 'Lugano', 'TI', 'Lugano'),
    ('4001', 'Basel', 'BS', 'Basel'),
    ('6003', 'Luzern', 'LU', 'Luzern'),
    ('9000', 'St. Gallen', 'SG', 'St. Gallen'),
    ('2501', 'Biel/Bienne', 'BE', 'Biel/Bienne'),
    ('1000', 'Lausanne', 'VD', 'Lausanne'),
    ('8400', 'Winterthur', 'ZH', 'Winterthur');

-- ════════════════════════════════════════════════════════
-- 6. NOTIFICATION TEMPLATES (multilingual)
-- ════════════════════════════════════════════════════════
INSERT INTO notification_template (id, template_code, type, event_type) VALUES
    (1, 'PARCEL_CREATED_EMAIL', 'EMAIL', 'PARCEL_CREATED'),
    (2, 'PARCEL_DELIVERED_EMAIL', 'EMAIL', 'PARCEL_DELIVERED'),
    (3, 'USER_CREATED_EMAIL', 'EMAIL', 'USER_CREATED');
SELECT setval('notification_template_id_seq', 3);

INSERT INTO notification_template_translation (template_id, locale, subject, body) VALUES
    -- PARCEL_CREATED
    (1, 'de', 'Ihr Paket wurde registriert', 'Sehr geehrte/r Kunde/in, Ihr Paket mit der Sendungsnummer {{trackingNumber}} wurde erfolgreich registriert. Sie können den Status jederzeit unter post.ch/tracking verfolgen.'),
    (1, 'fr', 'Votre colis a été enregistré', 'Cher/Chère client(e), votre colis avec le numéro de suivi {{trackingNumber}} a été enregistré avec succès. Vous pouvez suivre son statut sur post.ch/tracking.'),
    (1, 'it', 'Il vostro pacco è stato registrato', 'Gentile cliente, il vostro pacco con il numero di tracking {{trackingNumber}} è stato registrato con successo. Potete seguire lo stato su post.ch/tracking.'),
    (1, 'en', 'Your parcel has been registered', 'Dear customer, your parcel with tracking number {{trackingNumber}} has been successfully registered. You can track its status at post.ch/tracking.'),
    -- PARCEL_DELIVERED
    (2, 'de', 'Ihr Paket wurde zugestellt', 'Ihr Paket mit der Sendungsnummer {{trackingNumber}} wurde erfolgreich zugestellt. Vielen Dank für Ihr Vertrauen in die Schweizerische Post.'),
    (2, 'fr', 'Votre colis a été livré', 'Votre colis avec le numéro de suivi {{trackingNumber}} a été livré avec succès. Merci de votre confiance en La Poste Suisse.'),
    (2, 'it', 'Il vostro pacco è stato consegnato', 'Il vostro pacco con il numero di tracking {{trackingNumber}} è stato consegnato con successo. Grazie per la vostra fiducia nella Posta Svizzera.'),
    (2, 'en', 'Your parcel has been delivered', 'Your parcel with tracking number {{trackingNumber}} has been successfully delivered. Thank you for trusting Swiss Post.'),
    -- USER_CREATED
    (3, 'de', 'Willkommen bei der Schweizerischen Post', 'Willkommen, {{email}}! Ihr Konto wurde erfolgreich erstellt. Sie können nun alle Dienste der Schweizerischen Post nutzen.'),
    (3, 'fr', 'Bienvenue à La Poste Suisse', 'Bienvenue, {{email}}! Votre compte a été créé avec succès. Vous pouvez maintenant utiliser tous les services de La Poste Suisse.'),
    (3, 'it', 'Benvenuti alla Posta Svizzera', 'Benvenuto/a, {{email}}! Il vostro conto è stato creato con successo. Potete ora utilizzare tutti i servizi della Posta Svizzera.'),
    (3, 'en', 'Welcome to Swiss Post', 'Welcome, {{email}}! Your account has been successfully created. You can now use all Swiss Post services.');

