CREATE TABLE node_access
(
    id              UUID PRIMARY KEY   DEFAULT gen_random_uuid(),

    node_id         UUID NOT NULL REFERENCES nodes (id) ON DELETE CASCADE,

    user_id         UUID REFERENCES users (id) ON DELETE CASCADE,
    role_id         UUID REFERENCES roles (id) ON DELETE CASCADE,

    granted_role_id UUID NOT NULL REFERENCES roles (id) ON DELETE CASCADE,

    inherit         BOOLEAN NOT NULL DEFAULT false,

    created_by      UUID NOT NULL REFERENCES users (id),
    created_at      TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT chk_node_access_target CHECK (
        (user_id IS NOT NULL AND role_id IS NULL) OR
        (user_id IS NULL AND role_id IS NOT NULL)
    )
);