-- Flyway migration V1: Create emergency_requests table
CREATE TABLE IF NOT EXISTS emergency_requests (
    id VARCHAR(64) PRIMARY KEY,
    caller_id VARCHAR(100),
    emergency_type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    address VARCHAR(255),
    landmark VARCHAR(255),
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'RECEIVED',
    assigned_vehicle_id VARCHAR(64),
    assigned_assignment_id VARCHAR(64),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_emergency_requests_status ON emergency_requests(status);
CREATE INDEX IF NOT EXISTS idx_emergency_requests_created_at ON emergency_requests(created_at);
