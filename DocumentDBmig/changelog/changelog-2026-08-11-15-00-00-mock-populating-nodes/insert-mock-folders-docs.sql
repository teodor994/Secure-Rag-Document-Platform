WITH
-- Folderul radacina
t1 AS (
INSERT INTO nodes (parent_id, type, name, created_by, created_at, updated_at)
VALUES (NULL, 'folder', 'test1', (SELECT id FROM users WHERE email = 'a.a@a.a'), now(), now())
    RETURNING id
    ),
-- test2, copil al lui test1
    t2 AS (
INSERT INTO nodes (parent_id, type, name, created_by, created_at, updated_at)
SELECT id, 'folder', 'test2', (SELECT id FROM users WHERE email = 'a.a@a.a'), now(), now()
FROM t1
    RETURNING id
    ),
-- test3, copil al lui test1 (ramane gol)
    t3 AS (
INSERT INTO nodes (parent_id, type, name, created_by, created_at, updated_at)
SELECT id, 'folder', 'test3', (SELECT id FROM users WHERE email = 'a.a@a.a'), now(), now()
FROM t1
    RETURNING id
    ),
-- doc1.md, copil al lui test2
    doc1 AS (
INSERT INTO nodes (parent_id, type, name, created_by, created_at, updated_at)
SELECT id, 'document', 'doc1.md', (SELECT id FROM users WHERE email = 'b.b.b@b.b'), now(), now()
FROM t2
    RETURNING id
    ),
-- doc2.txt, copil al lui test2
    doc2 AS (
INSERT INTO nodes (parent_id, type, name, created_by, created_at, updated_at)
SELECT id, 'document', 'doc2.txt', (SELECT id FROM users WHERE email = 'b.b@b.b'), now(), now()
FROM t2
    RETURNING id
    ),
-- test4, copil al lui test2
    t4 AS (
INSERT INTO nodes (parent_id, type, name, created_by, created_at, updated_at)
SELECT id, 'folder', 'test4', (SELECT id FROM users WHERE email = 'a.a@a.a'), now(), now()
FROM t2
    RETURNING id
    ),
-- doc3.md, copil al lui test4
    doc3 AS (
INSERT INTO nodes (parent_id, type, name, created_by, created_at, updated_at)
SELECT id, 'document', 'doc3.md', (SELECT id FROM users WHERE email = 'b.b@b.b'), now(), now()
FROM t4
    RETURNING id
    )

-- populam node_content doar pentru documente (nu pentru foldere)
INSERT INTO node_content (node_id, storage_key, mime_type, size_bytes, created_at)
SELECT id, 'docs/test1/test2/doc1.md', 'text/markdown', 1024, now() FROM doc1
UNION ALL
SELECT id, 'docs/test1/test2/doc2.txt', 'text/plain', 512, now() FROM doc2
UNION ALL
SELECT id, 'docs/test1/test2/test4/doc3.md', 'text/markdown', 2048, now() FROM doc3;

-- test1/                          (folder, creat de a.a@a.a)
-- ├── test2/                      (folder, creat de a.a@a.a)
-- │   ├── doc1.md                 (document, creat de b.b@b.b)
-- │   ├── doc2.txt                (document, creat de b.b@b.b)
-- │   └── test4/                  (folder, creat de a.a@a.a)
-- │       └── doc3.md             (document, creat de b.b@b.b)
-- └── test3/                      (folder gol, creat de a.a@a.a)
--
-- c@c.c → niciun nod asociat