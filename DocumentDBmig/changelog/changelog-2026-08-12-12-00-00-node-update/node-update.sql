ALTER TABLE nodes
    ADD COLUMN path TEXT;
ALTER TABLE nodes
    DROP COLUMN IF EXISTS updated_at;

-- Populare rand de Path in nodes !!--------------
UPDATE nodes
SET path = '/test1'
WHERE name LIKE 'test1';

UPDATE nodes
SET path = '/test1/test2'
WHERE name LIKE 'test2';

UPDATE nodes
SET path = '/test1/test2/doc1.md'
WHERE name LIKE 'doc1.md';

UPDATE nodes
SET path = '/test1/test2/doc2.txt'
WHERE name LIKE 'doc2.txt';

UPDATE nodes
SET path = '/test1/test2/test4'
WHERE name LIKE 'test4';

UPDATE nodes
SET path = '/test1/test2/test4/doc3.md'
WHERE name LIKE 'doc3.md';

UPDATE nodes
SET path = '/test1/test3'
WHERE name LIKE 'test3';
---------------------------------------------------

ALTER TABLE node_content
DROP COLUMN IF EXISTS storage_key,
DROP COLUMN IF EXISTS created_at;

ALTER TABLE node_content
ADD COLUMN binary_content BYTEA,
ADD COLUMN text_content TEXT;

-- Populare doc1.md
UPDATE node_content
SET
    text_content   = '# Proiect ModernApp' || chr(10) || 'Acesta este primul document Markdown.',
    binary_content = convert_to('# Proiect ModernApp' || chr(10) || 'Acesta este primul document Markdown.', 'UTF8')
WHERE node_id = (
    SELECT id
    FROM nodes
    WHERE name LIKE 'doc1.md'
);

-- Populare doc2.txt
UPDATE node_content
SET
    text_content   = 'Acesta este al doilea document, care este text',
    binary_content = convert_to('Acesta este al doilea document, care este text', 'UTF8')
WHERE node_id = (
    SELECT id
    FROM nodes
    WHERE name LIKE 'doc2.txt'
);

-- Populare doc3.md
UPDATE node_content
SET
    text_content   = '# Ghid test4' || chr(10) || 'Documentatie si pasi de configurare.',
    binary_content = convert_to('# Ghid test4' || chr(10) || 'Documentatie si pasi de configurare.', 'UTF8')
WHERE node_id = (
    SELECT id
    FROM nodes
    WHERE name LIKE 'doc3.md'
);