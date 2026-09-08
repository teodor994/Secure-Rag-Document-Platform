CREATE TABLE nodes (
       id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       parent_id   UUID REFERENCES nodes(id) ON DELETE CASCADE,
       type        VARCHAR NOT NULL,
       name        VARCHAR NOT NULL,
       created_by  UUID REFERENCES users(id),
       created_at  TIMESTAMP NOT NULL DEFAULT now(),
       updated_at  TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE node_content (
       node_id     UUID PRIMARY KEY REFERENCES nodes(id) ON DELETE CASCADE,
       storage_key VARCHAR NOT NULL,
       mime_type   VARCHAR,
       size_bytes  BIGINT,
       created_at  TIMESTAMP NOT NULL DEFAULT now()
);