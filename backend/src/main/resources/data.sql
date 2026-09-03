-- Seed reference data (dentists, treatments)
INSERT IGNORE INTO dentists (id, name, specialization, consultation_fee) VALUES
    (1, 'Dr. Silva', 'Orthodontist', 500.00),
    (2, 'Dr. Perera', 'Periodontist', 800.00);

INSERT IGNORE INTO treatments (id, name, fee) VALUES
    (1, 'Root Canal', 5000.00),
    (2, 'Tooth Extraction', 3000.00),
    (3, 'Dental Cleaning', 2000.00);
