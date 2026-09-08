ALTER TABLE nodes ADD COLUMN processing_status VARCHAR(20) DEFAULT 'PENDING';

CREATE TABLE IF NOT EXISTS vector_store (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    content text,
    metadata jsonb,
    embedding vector(768)
);