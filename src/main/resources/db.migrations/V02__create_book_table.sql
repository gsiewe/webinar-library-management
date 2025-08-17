CREATE TABLE ${schema-name}.book (
     id VARCHAR(36) PRIMARY KEY,
     isbn VARCHAR(20) NOT NULL UNIQUE,
     title VARCHAR(255) NOT NULL,
     author VARCHAR(255) NOT NULL,
     publisher VARCHAR(255),
     publication_year INTEGER,
     location VARCHAR(100),
     status VARCHAR(20) NOT NULL,
     last_status_change TIMESTAMP,
     last_borrower_id VARCHAR(36),
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);