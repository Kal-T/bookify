INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Basic Package SG', 'Singapore', 5, 50.00, NULL);
INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Standard Package SG', 'Singapore', 10, 100.00, NULL);
INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Premium Package SG', 'Singapore', 20, 200.00, NULL);
INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Basic Package MY', 'Malaysia', 5, 40.00, NULL);
INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Standard Package MY', 'Malaysia', 10, 80.00, NULL);
INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Premium Package MY', 'Malaysia', 20, 160.00, NULL);
INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Basic Package MM', 'Myanmar', 5, 30.00, NULL);
INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Standard Package MM', 'Myanmar', 10, 60.00, NULL);
INSERT INTO packages (name, country, credits, price, expiry_date) VALUES ('Premium Package MM', 'Myanmar', 20, 120.00, NULL);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('1 hr Yoga Class SG', 'Singapore', 1, '2024-11-16 08:00:00', '2024-11-16 09:00:00', 10);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('2 hr Pilates Class SG', 'Singapore', 2, '2024-11-16 10:00:00', '2024-11-16 12:00:00', 8);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('1.5 hr Zumba Class SG', 'Singapore', 1, '2024-11-16 13:00:00', '2024-11-16 14:30:00', 12);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('1 hr Yoga Class MY', 'Malaysia', 1, '2024-11-16 08:00:00', '2024-11-16 09:00:00', 15);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('2 hr Pilates Class MY', 'Malaysia', 2, '2024-11-16 10:00:00', '2024-11-16 12:00:00', 10);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('1.5 hr Zumba Class MY', 'Malaysia', 1, '2024-11-16 13:00:00', '2024-11-16 14:30:00', 8);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('1 hr Yoga Class MM', 'Myanmar', 1, '2024-11-16 08:00:00', '2024-11-16 09:00:00', 20);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('2 hr Pilates Class MM', 'Myanmar', 2, '2024-11-16 10:00:00', '2024-11-16 12:00:00', 15);

INSERT INTO classes (name, country, credits_required, schedule_time, end_time, available_slots)
VALUES ('1.5 hr Zumba Class MM', 'Myanmar', 1, '2024-11-16 13:00:00', '2024-11-16 14:30:00', 12);
