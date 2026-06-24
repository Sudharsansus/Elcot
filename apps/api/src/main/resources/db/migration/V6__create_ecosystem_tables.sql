-- V6: Ecosystem tables (business_connect, talent_connect, freelancer_registry)
CREATE TABLE IF NOT EXISTS business_connect (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), company_name VARCHAR(255) NOT NULL, company_name_tamil VARCHAR(300),
    gst_number VARCHAR(20), registration_number VARCHAR(50), entity_type VARCHAR(50), sub_sector VARCHAR(50),
    employee_count INTEGER DEFAULT 0, turnover DECIMAL(15,2), district VARCHAR(100), address TEXT,
    contact_person VARCHAR(200), contact_email VARCHAR(255), contact_phone VARCHAR(15),
    website_url VARCHAR(500), status VARCHAR(30) DEFAULT 'ACTIVE', created_at TIMESTAMPTZ DEFAULT NOW(), updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE TABLE IF NOT EXISTS talent_connect (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), full_name VARCHAR(200) NOT NULL, full_name_tamil VARCHAR(300),
    email VARCHAR(255), mobile_number VARCHAR(15), skills TEXT[], experience_years INTEGER,
    sub_sector VARCHAR(50), portfolio_url VARCHAR(500), district VARCHAR(100), status VARCHAR(30) DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ DEFAULT NOW(), updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE TABLE IF NOT EXISTS freelancer_registry (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), full_name VARCHAR(200) NOT NULL, full_name_tamil VARCHAR(300),
    email VARCHAR(255), mobile_number VARCHAR(15), specialization VARCHAR(100), skills TEXT[],
    hourly_rate DECIMAL(10,2), availability_status VARCHAR(30), district VARCHAR(100), status VARCHAR(30) DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ DEFAULT NOW(), updated_at TIMESTAMPTZ DEFAULT NOW()
);
