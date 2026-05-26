CREATE TABLE IF NOT EXISTS users
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    username VARCHAR(255) UNIQUE NOT NULL,

    mobile VARCHAR(20) UNIQUE,

    location VARCHAR(255),

    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',

    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);