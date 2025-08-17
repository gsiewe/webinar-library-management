CREATE TABLE ${schema-name}.book (
     id VARCHAR(36) PRIMARY KEY,
     loan_id VARCHAR(36) NOT NULL UNIQUE,
     user_id VARCHAR(36) NOT NULL,
     user_email VARCHAR(255),
     book_id VARCHAR(36) NOT NULL,
     isbn VARCHAR(20) NOT NULL,
     book_title VARCHAR(255),
     borrow_date DATE NOT NULL,
     due_date DATE NOT NULL,
     return_date DATE,
     status VARCHAR(20) NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);