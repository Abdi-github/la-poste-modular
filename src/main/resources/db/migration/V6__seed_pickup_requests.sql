-- =====================================================
-- V6__seed_pickup_requests.sql
-- Seed pickup requests for testing the new Pickups UI
-- =====================================================

INSERT INTO pickup_request (customer_id, pickup_street, pickup_zip_code, pickup_city, preferred_date, preferred_time_from, preferred_time_to, status, created_at) VALUES
(1, 'Bahnhofstrasse 12',   '8001', 'Zürich',    '2026-04-10', '08:00', '12:00', 'PENDING',   now()),
(2, 'Bundesplatz 3',       '3003', 'Bern',      '2026-04-11', '09:00', '11:00', 'REQUESTED', now()),
(3, 'Rue du Rhône 42',     '1204', 'Genève',    '2026-04-12', '10:00', '14:00', 'CONFIRMED', now()),
(4, 'Piazza Grande 7',     '6600', 'Locarno',   '2026-04-13', '08:30', '10:30', 'ASSIGNED',  now()),
(5, 'Kramgasse 49',        '3011', 'Bern',      '2026-04-14', '13:00', '17:00', 'PICKED_UP', now()),
(1, 'Limmatquai 78',       '8001', 'Zürich',    '2026-04-15', NULL,    NULL,    'CANCELLED', now()),
(2, 'Marktgasse 22',       '3011', 'Bern',      '2026-04-16', '07:00', '09:00', 'PENDING',   now()),
(3, 'Avenue de la Gare 15','1003', 'Lausanne',  '2026-04-17', '11:00', '15:00', 'PENDING',   now()),
(6, 'Freiestrasse 89',     '4001', 'Basel',     '2026-04-18', '09:00', '12:00', 'REQUESTED', now()),
(7, 'Obere Bahnhofstr. 5', '9000', 'St. Gallen','2026-04-19', '14:00', '16:00', 'PENDING',   now());

