-- 1. Insert LANDLORDS
INSERT INTO [dbo].[LANDLORDS] ([NIF], [ID_CARD_NUMBER], [FULL_NAME], [BIRTH_DATE], [ORIGINAL_ADDRESS], [NIB])
VALUES
    ('696.609.103', '29384750', 'Cristela Santos', '1968-03-22', NULL, 'PT50002200003426584958622'),
    ('123.445.249', '29384759', 'Horácio Lima',    '1983-07-01', NULL, 'PT50002200003426584958600');

-- 2. Insert UNITS
INSERT INTO [dbo].[UNITS] (
    [ID],
    [LANDLORD_NIF],
    [LANDLORD_ID_CARD_NUMBER],
    [LANDLORD_FULL_NAME],
    [STREET],
    [POSTAL_CODE],
    [ARTICLE],
    [REGISTER_NUMBER],
    [TOWN],
    [FRACTION],
[TYPOLOGY]
)
VALUES
    (
    'leirinhas',
    '696.609.103', '29384750', 'Cristela Santos',
    'Rua dos alperces', '3800-123', '1234', '4444', 'Aveiro', '1ºDTO', 'T3'
    ),
    (
    '01',
    '123.445.249', '29384759', 'Horácio Lima',
    'Travessa das Leirinhas', '3810-001', '9999', '9999', 'Aveiro', '', 'T2'
    );