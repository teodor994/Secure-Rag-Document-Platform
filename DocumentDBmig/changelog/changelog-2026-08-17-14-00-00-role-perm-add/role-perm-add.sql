CREATE TABLE roles (
       id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       code VARCHAR(50) NOT NULL UNIQUE,
       name VARCHAR(100) NOT NULL
);

INSERT INTO roles (code, name) VALUES
('ADMIN', 'Admin'),
('USER', 'User'),
('VIEWER', 'Viewer');

CREATE TABLE permissions (
     id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     code        VARCHAR(50) NOT NULL UNIQUE,
     description VARCHAR(255)
);

INSERT INTO permissions (code, description) VALUES
('LIST', 'View the tree documents'),
('READ', 'Read the contents of a file'),
('WRITE', 'Create, modify and delete the content');


CREATE TABLE role_permissions (
      role_id       UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
      permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
      PRIMARY KEY (role_id, permission_id)
);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.code = 'ADMIN' AND p.code IN ('LIST', 'READ', 'WRITE');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.code = 'USER' AND p.code IN ('LIST', 'READ');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.code = 'VIEWER' AND p.code = 'LIST';


CREATE TABLE oidc_group_role_mappings (
      id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      oidc_group VARCHAR(255) NOT NULL,
      role_id    UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
      UNIQUE (oidc_group, role_id)
);

INSERT INTO oidc_group_role_mappings (oidc_group, role_id)
SELECT 'grp-admins', id FROM roles WHERE code = 'ADMIN';

INSERT INTO oidc_group_role_mappings (oidc_group, role_id)
SELECT 'grp-users', id FROM roles WHERE code = 'USER';

INSERT INTO oidc_group_role_mappings (oidc_group, role_id)
SELECT 'grp-users-2', id FROM roles WHERE code = 'USER';

INSERT INTO oidc_group_role_mappings (oidc_group, role_id)
SELECT 'grp-viewers', id FROM roles WHERE code = 'VIEWER';