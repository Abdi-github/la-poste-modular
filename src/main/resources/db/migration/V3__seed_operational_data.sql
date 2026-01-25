-- ===========================================================
-- V3__seed_operational_data.sql
-- La Poste Modular — Seed Parcels, Tracking, Deliveries,
--                     Notification Logs & Preferences
-- ===========================================================

-- ════════════════════════════════════════════════════════
-- 1. PARCELS (15 parcels across all statuses)
-- ════════════════════════════════════════════════════════
INSERT INTO parcel (id, tracking_number, status, type, weight_kg, length_cm, width_cm, height_cm, price,
                    sender_customer_id, origin_branch_id,
                    sender_name, sender_street, sender_zip_code, sender_city, sender_phone,
                    recipient_name, recipient_street, recipient_zip_code, recipient_city, recipient_phone,
                    created_at) VALUES
-- DELIVERED parcels
(1,  'LP-100001A-CH', 'DELIVERED',        'STANDARD',   1.2, 30, 20, 15, 7.40,  1, 1, 'Stefan Widmer',    'Bahnhofstrasse 10',  '8001', 'Zürich',    '+41 76 200 0001', 'Anna Meier',       'Kramgasse 5',       '3000', 'Bern',       '+41 76 300 0001', now() - interval '10 days'),
(2,  'LP-100002B-CH', 'DELIVERED',        'REGISTERED', 2.5, 40, 30, 20, 12.50, 2, 3, 'Isabelle Martin',  'Rue du Mont-Blanc 8','1201', 'Genève',    '+41 76 200 0002', 'Pierre Laurent',   'Rue de Bourg 12',   '1003', 'Lausanne',   '+41 76 300 0002', now() - interval '8 days'),
(3,  'LP-100003C-CH', 'DELIVERED',        'EXPRESS',    0.8, 25, 15, 10, 18.00, 3, 4, 'Giovanni Ferrari', 'Via della Posta 5',  '6900', 'Lugano',    '+41 76 200 0003', 'Marco Bianchi',    'Via Nassa 3',        '6900', 'Lugano',     '+41 76 300 0003', now() - interval '7 days'),
-- IN_TRANSIT parcels
(4,  'LP-100004D-CH', 'IN_TRANSIT',       'STANDARD',   3.0, 50, 35, 25, 11.00, 4, 1, 'Claudia Baumann',  'Limmatquai 22',      '8001', 'Zürich',    '+41 76 200 0004', 'Thomas Weber',     'Hauptstrasse 15',   '4001', 'Basel',      '+41 76 300 0004', now() - interval '3 days'),
(5,  'LP-100005E-CH', 'IN_TRANSIT',       'BULKY',      8.5, 80, 60, 50, 37.00, 1, 5, 'Stefan Widmer',    'Bahnhofstrasse 10',  '8001', 'Zürich',    '+41 76 200 0001', 'Felix Brunner',    'Pilatusstrasse 20', '6003', 'Luzern',     '+41 76 300 0005', now() - interval '2 days'),
(6,  'LP-100006F-CH', 'IN_TRANSIT',       'REGISTERED', 1.8, 35, 25, 18, 11.10, 5, 3, 'Pierre Roux',      'Place du Molard 3',  '1201', 'Genève',    '+41 76 200 0005', 'Sophie Dumont',    'Av. de la Gare 8',  '1003', 'Lausanne',   '+41 76 300 0006', now() - interval '1 day'),
-- OUT_FOR_DELIVERY parcels
(7,  'LP-100007G-CH', 'OUT_FOR_DELIVERY', 'STANDARD',   1.5, 30, 22, 12, 8.00,  2, 2, 'Isabelle Martin',  'Rue du Mont-Blanc 8','1201', 'Genève',    '+41 76 200 0002', 'Ursula Hofer',     'Junkerngasse 10',   '3000', 'Bern',       '+41 76 300 0007', now() - interval '1 day'),
(8,  'LP-100008H-CH', 'OUT_FOR_DELIVERY', 'EXPRESS',    0.5, 20, 15, 10, 18.00, 3, 4, 'Giovanni Ferrari', 'Via della Posta 5',  '6900', 'Lugano',    '+41 76 200 0003', 'Luca Conti',       'Via Pessina 12',     '6900', 'Lugano',     '+41 76 300 0008', now() - interval '12 hours'),
-- CREATED (new, waiting for pickup)
(9,  'LP-100009I-CH', 'CREATED',          'STANDARD',   2.0, 35, 25, 20, 9.00,  4, 1, 'Claudia Baumann',  'Limmatquai 22',      '8001', 'Zürich',    '+41 76 200 0004', 'Beat Schmid',      'Centralbahnplatz 1','4001', 'Basel',      '+41 76 300 0009', now() - interval '6 hours'),
(10, 'LP-10000AJ-CH', 'CREATED',          'REGISTERED', 1.0, 28, 18, 14, 9.50,  5, 7, 'Pierre Roux',      'Place du Molard 3',  '1201', 'Genève',    '+41 76 200 0005', 'Hans Keller',      'Multergasse 25',    '9000', 'St. Gallen', '+41 76 300 0010', now() - interval '3 hours'),
(11, 'LP-10000BK-CH', 'CREATED',          'EXPRESS',    0.3, 20, 12, 8,  18.00, 1, 1, 'Stefan Widmer',    'Bahnhofstrasse 10',  '8001', 'Zürich',    '+41 76 200 0001', 'Lisa Frei',        'Freiestrasse 45',   '8032', 'Zürich',     '+41 76 300 0011', now() - interval '1 hour'),
-- CANCELLED
(12, 'LP-10000CL-CH', 'CANCELLED',        'STANDARD',   1.5, 30, 20, 15, 8.00,  2, 3, 'Isabelle Martin',  'Rue du Mont-Blanc 8','1201', 'Genève',    '+41 76 200 0002', 'André Dupont',     'Rue de Carouge 30', '1205', 'Genève',     '+41 76 300 0012', now() - interval '5 days'),
-- PICKED_UP
(13, 'LP-10000DM-CH', 'PICKED_UP',        'BULKY',      12.0,100, 80, 60, 44.00,3, 4, 'Giovanni Ferrari', 'Via della Posta 5',  '6900', 'Lugano',    '+41 76 200 0003', 'Roberto Sala',     'Via Canova 8',       '6900', 'Lugano',     '+41 76 300 0013', now() - interval '4 days'),
-- RETURNED
(14, 'LP-10000EN-CH', 'RETURNED',         'STANDARD',   2.2, 35, 25, 18, 9.40,  4, 2, 'Claudia Baumann',  'Limmatquai 22',      '8001', 'Zürich',    '+41 76 200 0004', 'Markus Lehmann',   'Kramgasse 50',      '3011', 'Bern',       '+41 76 300 0014', now() - interval '6 days'),
-- Another DELIVERED
(15, 'LP-10000FO-CH', 'DELIVERED',        'STANDARD',   1.0, 25, 18, 12, 7.00,  5, 7, 'Pierre Roux',      'Place du Molard 3',  '1201', 'Genève',    '+41 76 200 0005', 'Sandra Berger',    'Spisergasse 10',    '9000', 'St. Gallen', '+41 76 300 0015', now() - interval '9 days');
SELECT setval('parcel_id_seq', 15);


-- ════════════════════════════════════════════════════════
-- 2. TRACKING RECORDS (one per parcel)
-- ════════════════════════════════════════════════════════
INSERT INTO tracking_record (id, tracking_number, current_status, current_branch_id, estimated_delivery, created_at) VALUES
(1,  'LP-100001A-CH', 'DELIVERED',        2,    now() - interval '9 days',  now() - interval '10 days'),
(2,  'LP-100002B-CH', 'DELIVERED',        NULL, now() - interval '6 days',  now() - interval '8 days'),
(3,  'LP-100003C-CH', 'DELIVERED',        4,    now() - interval '6 days',  now() - interval '7 days'),
(4,  'LP-100004D-CH', 'ARRIVED_AT_SORTING', 8,  now() + interval '2 days',  now() - interval '3 days'),
(5,  'LP-100005E-CH', 'DEPARTED_SORTING', 8,    now() + interval '3 days',  now() - interval '2 days'),
(6,  'LP-100006F-CH', 'ARRIVED_AT_BRANCH', 3,   now() + interval '1 day',   now() - interval '1 day'),
(7,  'LP-100007G-CH', 'OUT_FOR_DELIVERY', 2,    now(),                       now() - interval '1 day'),
(8,  'LP-100008H-CH', 'OUT_FOR_DELIVERY', 4,    now(),                       now() - interval '12 hours'),
(9,  'LP-100009I-CH', 'REGISTERED',       1,    now() + interval '5 days',  now() - interval '6 hours'),
(10, 'LP-10000AJ-CH', 'REGISTERED',       7,    now() + interval '4 days',  now() - interval '3 hours'),
(11, 'LP-10000BK-CH', 'REGISTERED',       1,    now() + interval '1 day',   now() - interval '1 hour'),
(12, 'LP-10000CL-CH', 'REGISTERED',       3,    NULL,                        now() - interval '5 days'),
(13, 'LP-10000DM-CH', 'PICKED_UP',        4,    now() + interval '3 days',  now() - interval '4 days'),
(14, 'LP-10000EN-CH', 'RETURNED',         2,    NULL,                        now() - interval '6 days'),
(15, 'LP-10000FO-CH', 'DELIVERED',        7,    now() - interval '8 days',  now() - interval '9 days');
SELECT setval('tracking_record_id_seq', 15);


-- ════════════════════════════════════════════════════════
-- 3. TRACKING EVENTS (realistic lifecycle per parcel)
-- ════════════════════════════════════════════════════════
INSERT INTO tracking_event (tracking_number, event_type, branch_id, location, description_key, event_timestamp, scanned_by_employee_id, created_at) VALUES
-- Parcel 1: Full lifecycle → DELIVERED
('LP-100001A-CH', 'REGISTERED',          1, 'Zürich Sihlpost',        'tracking.event.REGISTERED',          now() - interval '10 days',              'EMP-CLK01', now() - interval '10 days'),
('LP-100001A-CH', 'PICKED_UP',           1, 'Zürich Sihlpost',        'tracking.event.PICKED_UP',           now() - interval '10 days' + interval '2 hours', 'EMP-CRR01', now() - interval '10 days'),
('LP-100001A-CH', 'ARRIVED_AT_SORTING',  8, 'Sortierzentrum Biel',    'tracking.event.ARRIVED_AT_SORTING',  now() - interval '9 days',               'EMP-CLK02', now() - interval '9 days'),
('LP-100001A-CH', 'DEPARTED_SORTING',    8, 'Sortierzentrum Biel',    'tracking.event.DEPARTED_SORTING',    now() - interval '9 days' + interval '4 hours',  'EMP-CLK02', now() - interval '9 days'),
('LP-100001A-CH', 'ARRIVED_AT_BRANCH',   2, 'Bern Schanzenpost',      'tracking.event.ARRIVED_AT_BRANCH',   now() - interval '9 days' + interval '8 hours',  'EMP-CLK01', now() - interval '9 days'),
('LP-100001A-CH', 'OUT_FOR_DELIVERY',    2, 'Bern',                   'tracking.event.OUT_FOR_DELIVERY',    now() - interval '9 days' + interval '10 hours', 'EMP-CRR04', now() - interval '9 days'),
('LP-100001A-CH', 'DELIVERED',           2, 'Kramgasse 5, 3000 Bern', 'tracking.event.DELIVERED',           now() - interval '9 days' + interval '12 hours', 'EMP-CRR04', now() - interval '9 days'),

-- Parcel 2: Full lifecycle → DELIVERED
('LP-100002B-CH', 'REGISTERED',          3, 'Genève',                 'tracking.event.REGISTERED',          now() - interval '8 days',               'EMP-CRR02', now() - interval '8 days'),
('LP-100002B-CH', 'PICKED_UP',           3, 'Genève',                 'tracking.event.PICKED_UP',           now() - interval '7 days' + interval '6 hours',  'EMP-CRR02', now() - interval '7 days'),
('LP-100002B-CH', 'ARRIVED_AT_SORTING',  8, 'Sortierzentrum Biel',    'tracking.event.ARRIVED_AT_SORTING',  now() - interval '7 days',               'EMP-CLK02', now() - interval '7 days'),
('LP-100002B-CH', 'DEPARTED_SORTING',    8, 'Sortierzentrum Biel',    'tracking.event.DEPARTED_SORTING',    now() - interval '6 days' + interval '6 hours',  'EMP-CLK02', now() - interval '6 days'),
('LP-100002B-CH', 'OUT_FOR_DELIVERY',    NULL, 'Lausanne',            'tracking.event.OUT_FOR_DELIVERY',    now() - interval '6 days' + interval '10 hours', NULL,        now() - interval '6 days'),
('LP-100002B-CH', 'DELIVERED',           NULL, 'Rue de Bourg 12, 1003 Lausanne', 'tracking.event.DELIVERED', now() - interval '6 days' + interval '13 hours', NULL,       now() - interval '6 days'),

-- Parcel 3: Express → DELIVERED (fast)
('LP-100003C-CH', 'REGISTERED',          4, 'Lugano',                 'tracking.event.REGISTERED',          now() - interval '7 days',               'EMP-CRR03', now() - interval '7 days'),
('LP-100003C-CH', 'PICKED_UP',           4, 'Lugano',                 'tracking.event.PICKED_UP',           now() - interval '7 days' + interval '1 hour',   'EMP-CRR03', now() - interval '7 days'),
('LP-100003C-CH', 'OUT_FOR_DELIVERY',    4, 'Lugano',                 'tracking.event.OUT_FOR_DELIVERY',    now() - interval '7 days' + interval '3 hours',  'EMP-CRR03', now() - interval '7 days'),
('LP-100003C-CH', 'DELIVERED',           4, 'Via Nassa 3, 6900 Lugano','tracking.event.DELIVERED',           now() - interval '7 days' + interval '5 hours',  'EMP-CRR03', now() - interval '7 days'),

-- Parcel 4: IN_TRANSIT → at sorting center
('LP-100004D-CH', 'REGISTERED',          1, 'Zürich Sihlpost',        'tracking.event.REGISTERED',          now() - interval '3 days',               'EMP-CRR01', now() - interval '3 days'),
('LP-100004D-CH', 'PICKED_UP',           1, 'Zürich Sihlpost',        'tracking.event.PICKED_UP',           now() - interval '3 days' + interval '3 hours',  'EMP-CRR01', now() - interval '3 days'),
('LP-100004D-CH', 'ARRIVED_AT_SORTING',  8, 'Sortierzentrum Biel',    'tracking.event.ARRIVED_AT_SORTING',  now() - interval '2 days',               'EMP-CLK02', now() - interval '2 days'),

-- Parcel 5: IN_TRANSIT → departed sorting
('LP-100005E-CH', 'REGISTERED',          5, 'Basel',                  'tracking.event.REGISTERED',          now() - interval '2 days',               'EMP-CLK02', now() - interval '2 days'),
('LP-100005E-CH', 'PICKED_UP',           5, 'Basel',                  'tracking.event.PICKED_UP',           now() - interval '2 days' + interval '4 hours',  'EMP-CLK02', now() - interval '2 days'),
('LP-100005E-CH', 'ARRIVED_AT_SORTING',  8, 'Sortierzentrum Biel',    'tracking.event.ARRIVED_AT_SORTING',  now() - interval '1 day',                'EMP-CLK02', now() - interval '1 day'),
('LP-100005E-CH', 'DEPARTED_SORTING',    8, 'Sortierzentrum Biel',    'tracking.event.DEPARTED_SORTING',    now() - interval '1 day' + interval '6 hours',   'EMP-CLK02', now() - interval '1 day'),

-- Parcel 6: IN_TRANSIT → arrived at destination branch
('LP-100006F-CH', 'REGISTERED',          3, 'Genève',                 'tracking.event.REGISTERED',          now() - interval '1 day',                'EMP-CRR02', now() - interval '1 day'),
('LP-100006F-CH', 'PICKED_UP',           3, 'Genève',                 'tracking.event.PICKED_UP',           now() - interval '1 day' + interval '2 hours',   'EMP-CRR02', now() - interval '1 day'),
('LP-100006F-CH', 'ARRIVED_AT_BRANCH',   3, 'Genève',                 'tracking.event.ARRIVED_AT_BRANCH',   now() - interval '12 hours',             'EMP-CRR02', now() - interval '12 hours'),

-- Parcel 7: OUT_FOR_DELIVERY
('LP-100007G-CH', 'REGISTERED',          2, 'Bern Schanzenpost',      'tracking.event.REGISTERED',          now() - interval '1 day',                'EMP-CLK01', now() - interval '1 day'),
('LP-100007G-CH', 'PICKED_UP',           2, 'Bern',                   'tracking.event.PICKED_UP',           now() - interval '22 hours',             'EMP-CLK01', now() - interval '22 hours'),
('LP-100007G-CH', 'ARRIVED_AT_SORTING',  8, 'Sortierzentrum Biel',    'tracking.event.ARRIVED_AT_SORTING',  now() - interval '18 hours',             'EMP-CLK02', now() - interval '18 hours'),
('LP-100007G-CH', 'DEPARTED_SORTING',    8, 'Sortierzentrum Biel',    'tracking.event.DEPARTED_SORTING',    now() - interval '14 hours',             'EMP-CLK02', now() - interval '14 hours'),
('LP-100007G-CH', 'ARRIVED_AT_BRANCH',   2, 'Bern Schanzenpost',      'tracking.event.ARRIVED_AT_BRANCH',   now() - interval '10 hours',             'EMP-CLK01', now() - interval '10 hours'),
('LP-100007G-CH', 'OUT_FOR_DELIVERY',    2, 'Bern',                   'tracking.event.OUT_FOR_DELIVERY',    now() - interval '6 hours',              'EMP-CRR04', now() - interval '6 hours'),

-- Parcel 8: EXPRESS OUT_FOR_DELIVERY
('LP-100008H-CH', 'REGISTERED',          4, 'Lugano',                 'tracking.event.REGISTERED',          now() - interval '12 hours',             'EMP-CRR03', now() - interval '12 hours'),
('LP-100008H-CH', 'PICKED_UP',           4, 'Lugano',                 'tracking.event.PICKED_UP',           now() - interval '10 hours',             'EMP-CRR03', now() - interval '10 hours'),
('LP-100008H-CH', 'OUT_FOR_DELIVERY',    4, 'Lugano',                 'tracking.event.OUT_FOR_DELIVERY',    now() - interval '4 hours',              'EMP-CRR03', now() - interval '4 hours'),

-- Parcel 9: CREATED (just registered)
('LP-100009I-CH', 'REGISTERED',          1, 'Zürich Sihlpost',        'tracking.event.REGISTERED',          now() - interval '6 hours',              'EMP-CLK01', now() - interval '6 hours'),

-- Parcel 10: CREATED
('LP-10000AJ-CH', 'REGISTERED',          7, 'St. Gallen',             'tracking.event.REGISTERED',          now() - interval '3 hours',              'EMP-CLK02', now() - interval '3 hours'),

-- Parcel 11: CREATED (express)
('LP-10000BK-CH', 'REGISTERED',          1, 'Zürich Sihlpost',        'tracking.event.REGISTERED',          now() - interval '1 hour',               'EMP-CRR01', now() - interval '1 hour'),

-- Parcel 12: CANCELLED
('LP-10000CL-CH', 'REGISTERED',          3, 'Genève',                 'tracking.event.REGISTERED',          now() - interval '5 days',               'EMP-CRR02', now() - interval '5 days'),

-- Parcel 13: PICKED_UP (bulky, in transit)
('LP-10000DM-CH', 'REGISTERED',          4, 'Lugano',                 'tracking.event.REGISTERED',          now() - interval '4 days',               'EMP-CRR03', now() - interval '4 days'),
('LP-10000DM-CH', 'PICKED_UP',           4, 'Lugano',                 'tracking.event.PICKED_UP',           now() - interval '3 days' + interval '6 hours',  'EMP-CRR03', now() - interval '3 days'),

-- Parcel 14: RETURNED (delivery failed then returned)
('LP-10000EN-CH', 'REGISTERED',          2, 'Bern Schanzenpost',      'tracking.event.REGISTERED',          now() - interval '6 days',               'EMP-CLK01', now() - interval '6 days'),
('LP-10000EN-CH', 'PICKED_UP',           2, 'Bern',                   'tracking.event.PICKED_UP',           now() - interval '5 days' + interval '6 hours',  'EMP-CLK01', now() - interval '5 days'),
('LP-10000EN-CH', 'ARRIVED_AT_SORTING',  8, 'Sortierzentrum Biel',    'tracking.event.ARRIVED_AT_SORTING',  now() - interval '5 days',               'EMP-CLK02', now() - interval '5 days'),
('LP-10000EN-CH', 'DEPARTED_SORTING',    8, 'Sortierzentrum Biel',    'tracking.event.DEPARTED_SORTING',    now() - interval '4 days' + interval '6 hours',  'EMP-CLK02', now() - interval '4 days'),
('LP-10000EN-CH', 'ARRIVED_AT_BRANCH',   2, 'Bern',                   'tracking.event.ARRIVED_AT_BRANCH',   now() - interval '4 days',               'EMP-CLK01', now() - interval '4 days'),
('LP-10000EN-CH', 'OUT_FOR_DELIVERY',    2, 'Bern',                   'tracking.event.OUT_FOR_DELIVERY',    now() - interval '4 days' + interval '4 hours',  'EMP-CRR04', now() - interval '4 days'),
('LP-10000EN-CH', 'DELIVERY_FAILED',     2, 'Kramgasse 50, 3011 Bern','tracking.event.DELIVERY_FAILED',     now() - interval '4 days' + interval '6 hours',  'EMP-CRR04', now() - interval '4 days'),
('LP-10000EN-CH', 'RETURNED',            2, 'Bern Schanzenpost',      'tracking.event.RETURNED',            now() - interval '3 days',               'EMP-CLK01', now() - interval '3 days'),

-- Parcel 15: Full lifecycle → DELIVERED
('LP-10000FO-CH', 'REGISTERED',          7, 'St. Gallen',             'tracking.event.REGISTERED',          now() - interval '9 days',               'EMP-CLK02', now() - interval '9 days'),
('LP-10000FO-CH', 'PICKED_UP',           7, 'St. Gallen',             'tracking.event.PICKED_UP',           now() - interval '9 days' + interval '3 hours',  'EMP-CLK02', now() - interval '9 days'),
('LP-10000FO-CH', 'ARRIVED_AT_SORTING',  8, 'Sortierzentrum Biel',    'tracking.event.ARRIVED_AT_SORTING',  now() - interval '8 days' + interval '6 hours',  'EMP-CLK02', now() - interval '8 days'),
('LP-10000FO-CH', 'DEPARTED_SORTING',    8, 'Sortierzentrum Biel',    'tracking.event.DEPARTED_SORTING',    now() - interval '8 days' + interval '12 hours', 'EMP-CLK02', now() - interval '8 days'),
('LP-10000FO-CH', 'ARRIVED_AT_BRANCH',   7, 'St. Gallen',             'tracking.event.ARRIVED_AT_BRANCH',   now() - interval '8 days',               'EMP-CLK02', now() - interval '8 days'),
('LP-10000FO-CH', 'OUT_FOR_DELIVERY',    7, 'St. Gallen',             'tracking.event.OUT_FOR_DELIVERY',    now() - interval '8 days' + interval '2 hours',  'EMP-CLK02', now() - interval '8 days'),
('LP-10000FO-CH', 'DELIVERED',           7, 'Spisergasse 10, 9000 St. Gallen', 'tracking.event.DELIVERED',  now() - interval '8 days' + interval '4 hours',  'EMP-CLK02', now() - interval '8 days');


-- ════════════════════════════════════════════════════════
-- 4. DELIVERY ROUTES & SLOTS
-- ════════════════════════════════════════════════════════
-- Route 1: Bern route — COMPLETED (employee Daniel Meier / id 10, branch 2)
INSERT INTO delivery_route (id, route_code, branch_id, assigned_employee_id, status, date, created_at) VALUES
(1, 'RT-BRN001', 2, 10, 'COMPLETED', (now() - interval '9 days')::date, now() - interval '9 days');

INSERT INTO delivery_slot (id, route_id, tracking_number, sequence_order, status, recipient_signature, created_at) VALUES
(1, 1, 'LP-100001A-CH', 1, 'DELIVERED', 'A. Meier', now() - interval '9 days'),
(2, 1, 'LP-10000EN-CH', 2, 'FAILED',    NULL,        now() - interval '9 days');

-- Route 2: Zürich route — IN_PROGRESS (employee Peter Keller / id 4, branch 1)
INSERT INTO delivery_route (id, route_code, branch_id, assigned_employee_id, status, date, created_at) VALUES
(2, 'RT-ZRH001', 1, 4, 'IN_PROGRESS', CURRENT_DATE, now() - interval '6 hours');

INSERT INTO delivery_slot (id, route_id, tracking_number, sequence_order, status, created_at) VALUES
(3, 2, 'LP-100004D-CH', 1, 'PENDING',    now() - interval '6 hours'),
(4, 2, 'LP-100005E-CH', 2, 'PENDING',    now() - interval '6 hours'),
(5, 2, 'LP-100009I-CH', 3, 'PENDING',    now() - interval '6 hours');

-- Route 3: Lugano route — IN_PROGRESS (employee Luca Rossi / id 6, branch 4)
INSERT INTO delivery_route (id, route_code, branch_id, assigned_employee_id, status, date, created_at) VALUES
(3, 'RT-LUG001', 4, 6, 'IN_PROGRESS', CURRENT_DATE, now() - interval '4 hours');

INSERT INTO delivery_slot (id, route_id, tracking_number, sequence_order, status, created_at) VALUES
(6, 3, 'LP-100008H-CH', 1, 'PENDING',    now() - interval '4 hours'),
(7, 3, 'LP-100003C-CH', 2, 'DELIVERED',  now() - interval '4 hours');

-- Route 4: Bern route — PLANNED for tomorrow (employee Anna Weber / id 7, branch 2)
INSERT INTO delivery_route (id, route_code, branch_id, assigned_employee_id, status, date, created_at) VALUES
(4, 'RT-BRN002', 2, 7, 'PLANNED', (CURRENT_DATE + interval '1 day')::date, now() - interval '2 hours');

INSERT INTO delivery_slot (id, route_id, tracking_number, sequence_order, status, created_at) VALUES
(8,  4, 'LP-100007G-CH', 1, 'PENDING', now() - interval '2 hours'),
(9,  4, 'LP-100006F-CH', 2, 'PENDING', now() - interval '2 hours');

SELECT setval('delivery_route_id_seq', 4);
SELECT setval('delivery_slot_id_seq', 9);


-- ════════════════════════════════════════════════════════
-- 5. NOTIFICATION PREFERENCES (one per customer)
-- ════════════════════════════════════════════════════════
INSERT INTO notification_preference (customer_id, email_enabled, sms_enabled, in_app_enabled, preferred_locale) VALUES
(1, true,  false, true,  'de'),
(2, true,  false, true,  'fr'),
(3, true,  true,  true,  'it'),
(4, true,  false, false, 'de'),
(5, true,  true,  true,  'fr');


-- ════════════════════════════════════════════════════════
-- 6. NEW NOTIFICATION TEMPLATES (PARCEL_SCANNED, DELIVERY_COMPLETED)
-- ════════════════════════════════════════════════════════
INSERT INTO notification_template (id, template_code, type, event_type) VALUES
(4, 'PARCEL_SCANNED_EMAIL',        'EMAIL', 'PARCEL_SCANNED'),
(5, 'DELIVERY_COMPLETED_EMAIL',    'EMAIL', 'DELIVERY_COMPLETED');
SELECT setval('notification_template_id_seq', 5);

INSERT INTO notification_template_translation (template_id, locale, subject, body) VALUES
-- PARCEL_SCANNED
(4, 'de', 'Statusaktualisierung für Ihr Paket', 'Ihr Paket mit der Sendungsnummer {{trackingNumber}} wurde gescannt: {{eventType}}. Verfolgen Sie den Status unter post.ch/tracking.'),
(4, 'fr', 'Mise à jour du statut de votre colis', 'Votre colis avec le numéro de suivi {{trackingNumber}} a été scanné : {{eventType}}. Suivez son statut sur post.ch/tracking.'),
(4, 'it', 'Aggiornamento stato del vostro pacco', 'Il vostro pacco con il numero di tracking {{trackingNumber}} è stato scansionato: {{eventType}}. Seguite lo stato su post.ch/tracking.'),
(4, 'en', 'Status update for your parcel', 'Your parcel with tracking number {{trackingNumber}} has been scanned: {{eventType}}. Track its status at post.ch/tracking.'),
-- DELIVERY_COMPLETED
(5, 'de', 'Zustellung abgeschlossen', 'Die Zustellung Ihres Pakets mit der Sendungsnummer {{trackingNumber}} wurde abgeschlossen. Vielen Dank für Ihr Vertrauen in die Schweizerische Post.'),
(5, 'fr', 'Livraison terminée', 'La livraison de votre colis avec le numéro de suivi {{trackingNumber}} est terminée. Merci de votre confiance en La Poste Suisse.'),
(5, 'it', 'Consegna completata', 'La consegna del vostro pacco con il numero di tracking {{trackingNumber}} è stata completata. Grazie per la vostra fiducia nella Posta Svizzera.'),
(5, 'en', 'Delivery completed', 'The delivery of your parcel with tracking number {{trackingNumber}} has been completed. Thank you for trusting Swiss Post.');


-- ════════════════════════════════════════════════════════
-- 7. HISTORICAL NOTIFICATION LOGS
-- ════════════════════════════════════════════════════════
INSERT INTO notification_log (recipient_email, type, status, subject, body, reference_id, event_type, created_at) VALUES
-- Parcel created notifications
('stefan.widmer@example.ch',    'EMAIL', 'SENT', 'Ihr Paket wurde registriert',   'Sehr geehrte/r Kunde/in, Ihr Paket mit der Sendungsnummer LP-100001A-CH wurde erfolgreich registriert.', 'LP-100001A-CH', 'PARCEL_CREATED',   now() - interval '10 days'),
('isabelle.martin@example.ch',  'EMAIL', 'SENT', 'Votre colis a été enregistré',  'Cher/Chère client(e), votre colis avec le numéro de suivi LP-100002B-CH a été enregistré avec succès.',  'LP-100002B-CH', 'PARCEL_CREATED',   now() - interval '8 days'),
('giovanni.ferrari@example.ch', 'EMAIL', 'SENT', 'Il vostro pacco è stato registrato', 'Gentile cliente, il vostro pacco con il numero di tracking LP-100003C-CH è stato registrato con successo.', 'LP-100003C-CH', 'PARCEL_CREATED', now() - interval '7 days'),
('claudia.baumann@example.ch',  'EMAIL', 'SENT', 'Ihr Paket wurde registriert',   'Sehr geehrte/r Kunde/in, Ihr Paket mit der Sendungsnummer LP-100004D-CH wurde erfolgreich registriert.', 'LP-100004D-CH', 'PARCEL_CREATED',   now() - interval '3 days'),
('pierre.roux@example.ch',      'EMAIL', 'SENT', 'Votre colis a été enregistré',  'Cher/Chère client(e), votre colis avec le numéro de suivi LP-10000AJ-CH a été enregistré avec succès.',  'LP-10000AJ-CH', 'PARCEL_CREATED',   now() - interval '3 hours'),
-- Parcel delivered notifications
('stefan.widmer@example.ch',    'EMAIL', 'SENT', 'Ihr Paket wurde zugestellt',    'Ihr Paket mit der Sendungsnummer LP-100001A-CH wurde erfolgreich zugestellt.',                            'LP-100001A-CH', 'PARCEL_DELIVERED',  now() - interval '9 days'),
('isabelle.martin@example.ch',  'EMAIL', 'SENT', 'Votre colis a été livré',       'Votre colis avec le numéro de suivi LP-100002B-CH a été livré avec succès.',                              'LP-100002B-CH', 'PARCEL_DELIVERED',  now() - interval '6 days'),
('giovanni.ferrari@example.ch', 'EMAIL', 'SENT', 'Il vostro pacco è stato consegnato', 'Il vostro pacco con il numero di tracking LP-100003C-CH è stato consegnato con successo.',           'LP-100003C-CH', 'PARCEL_DELIVERED',  now() - interval '7 days'),
-- Scan updates
('claudia.baumann@example.ch',  'EMAIL', 'SENT', 'Statusaktualisierung für Ihr Paket', 'Ihr Paket mit der Sendungsnummer LP-100004D-CH wurde gescannt: ARRIVED_AT_SORTING.',               'LP-100004D-CH', 'PARCEL_SCANNED',    now() - interval '2 days'),
('stefan.widmer@example.ch',    'EMAIL', 'SENT', 'Statusaktualisierung für Ihr Paket', 'Ihr Paket mit der Sendungsnummer LP-100005E-CH wurde gescannt: DEPARTED_SORTING.',                  'LP-100005E-CH', 'PARCEL_SCANNED',    now() - interval '1 day'),
-- Welcome notification
('stefan.widmer@example.ch',    'EMAIL', 'SENT', 'Willkommen bei der Schweizerischen Post', 'Willkommen, stefan.widmer@example.ch! Ihr Konto wurde erfolgreich erstellt.',                  '1',             'USER_CREATED',      now() - interval '30 days'),
('isabelle.martin@example.ch',  'EMAIL', 'SENT', 'Bienvenue à La Poste Suisse',   'Bienvenue, isabelle.martin@example.ch! Votre compte a été créé avec succès.',                             '2',             'USER_CREATED',      now() - interval '28 days');

