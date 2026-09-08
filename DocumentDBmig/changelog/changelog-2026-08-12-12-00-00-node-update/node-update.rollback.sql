ALTER TABLE node_content
    ADD COLUMN storage_key VARCHAR(255),
ADD COLUMN created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP;

-- Repopulare tabela storage_key cu ce era inainte
UPDATE node_content
SET storage_key = 'docs/test1/test2/doc1.md'
WHERE node_id = (
    SELECT id
    FROM nodes
    WHERE name LIKE 'doc1.md'
);

UPDATE node_content
SET storage_key = 'docs/test1/test2/doc2.txt'
WHERE node_id = (
    SELECT id
    FROM nodes
    WHERE name LIKE 'doc2.txt'
);

UPDATE node_content
SET storage_key = 'docs/test1/test2/test4/doc3.md'
WHERE node_id = (
    SELECT id
    FROM nodes
    WHERE name LIKE 'doc3.md'
);

ALTER TABLE node_content
DROP COLUMN IF EXISTS binary_content,
DROP COLUMN IF EXISTS text_content;


ALTER TABLE nodes
DROP COLUMN IF EXISTS path;


ALTER TABLE nodes
    ADD COLUMN updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP;