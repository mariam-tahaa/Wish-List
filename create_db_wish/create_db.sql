ALTER USER Iwish QUOTA UNLIMITED ON USERS;


SELECT tablespace_name, bytes, max_bytes
FROM user_ts_quotas;

-- Set the schema context
ALTER SESSION SET CURRENT_SCHEMA =  Iwish;
grant CREATE TABLE to Iwish;
grant CREATE SEQUENCE to Iwish; 
grant CREATE VIEW to Iwish;
grant CREATE TRIGGER to Iwish;  
grant CREATE PROCEDURE to Iwish;
grant CREATE ANY CONTEXT to Iwish; 
--grant quota unlimited on USERS to Iwish;
-- Create sequences for primary keys
CREATE SEQUENCE app_user_seq;
CREATE SEQUENCE gift_seq;
CREATE SEQUENCE friend_req_seq;
CREATE SEQUENCE notification_seq;
CREATE SEQUENCE contribution_seq;

-- 1. app_user
CREATE  TABLE app_user (
    user_id      NUMBER PRIMARY KEY,
    user_name         VARCHAR2(100) NOT NULL,
    mail         VARCHAR2(100) NOT NULL UNIQUE,
    pass         VARCHAR2(255) NOT NULL
) TABLESPACE USERS;

-- Trigger to auto-increment user_id
CREATE OR REPLACE TRIGGER app_user_before_insert
BEFORE INSERT ON app_user
FOR EACH ROW
BEGIN
    IF :NEW.user_id IS NULL THEN
        SELECT app_user_seq.NEXTVAL INTO :NEW.user_id FROM dual;
    END IF;
END;



-- 2. gift
CREATE TABLE gift (
    gift_id        NUMBER  PRIMARY KEY,
    gift_name      VARCHAR2(255) NOT NULL,   
    price          NUMBER(10, 2) NOT NULL,       
    status         VARCHAR2(20) DEFAULT 'Incomplete' CHECK (status IN ('Incomplete', 'Completed')),
    owner_user_id  NUMBER NOT NULL,
    CONSTRAINT fk_gift_owner FOREIGN KEY (owner_user_id) REFERENCES app_user(user_id) ON DELETE CASCADE
)TABLESPACE USERS;

CREATE OR REPLACE TRIGGER gift_before_insert
BEFORE INSERT ON gift
FOR EACH ROW
BEGIN
    IF :NEW.gift_id IS NULL THEN
        SELECT gift_seq.NEXTVAL INTO :NEW.gift_id FROM dual;
    END IF;
END;



-- 3. friend_request
CREATE TABLE friend_request (
    req_id       NUMBER  PRIMARY KEY,
    sender_id    NUMBER NOT NULL,
    receiver_id  NUMBER NOT NULL,
    status       VARCHAR2(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED')),
    CONSTRAINT fk_req_sender FOREIGN KEY (sender_id) REFERENCES app_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_req_receiver FOREIGN KEY (receiver_id) REFERENCES app_user(user_id) ON DELETE CASCADE,
    CONSTRAINT chk_no_self_req CHECK (sender_id != receiver_id)
)TABLESPACE USERS;
-- Trigger to auto-increment req_id
CREATE OR REPLACE TRIGGER friend_request_before_insert
BEFORE INSERT ON friend_request
FOR EACH ROW
BEGIN
    IF :NEW.req_id IS NULL THEN
        SELECT friend_req_seq.NEXTVAL INTO :NEW.req_id FROM dual;
    END IF;
END;

-- 4. friendship
CREATE TABLE friendship (
    user_id    NUMBER NOT NULL,
    friend_id  NUMBER NOT NULL,
    CONSTRAINT pk_friendship PRIMARY KEY (user_id, friend_id),
    CONSTRAINT fk_friend_u1 FOREIGN KEY (user_id) REFERENCES app_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_friend_u2 FOREIGN KEY (friend_id) REFERENCES app_user(user_id) ON DELETE CASCADE
   -- CONSTRAINT chk_friend_order CHECK (user_id < friend_id)
)TABLESPACE USERS;


-- 5. notification
CREATE TABLE notification (
    not_id      NUMBER PRIMARY KEY,
    user_id     NUMBER NOT NULL,
    content     VARCHAR2(4000) NOT NULL,
    not_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status      VARCHAR2(20) DEFAULT 'UNREAD' CHECK (status IN ('UNREAD', 'READ')),
    CONSTRAINT fk_not_user FOREIGN KEY (user_id) REFERENCES app_user(user_id) ON DELETE CASCADE
)TABLESPACE USERS;
CREATE OR REPLACE TRIGGER notification_before_insert
BEFORE INSERT ON notification 
FOR EACH ROW
BEGIN
    IF :NEW.not_id IS NULL THEN
        SELECT notification_seq.NEXTVAL INTO :NEW.not_id FROM dual;
    END IF;
END;

-- 6. contribution
CREATE TABLE contribution (
    contribution_id NUMBER  PRIMARY KEY,
    contributor_id  NUMBER NOT NULL,
    gift_id         NUMBER NOT NULL,     
    percentage      NUMBER(5, 2) NOT NULL CHECK (percentage > 0 AND percentage <= 100),
    CONSTRAINT fk_cont_user FOREIGN KEY (contributor_id) REFERENCES app_user(user_id),    
    CONSTRAINT fk_cont_gift FOREIGN KEY (gift_id) REFERENCES gift(gift_id) ON DELETE CASCADE
)TABLESPACE USERS;

CREATE OR REPLACE TRIGGER contribution_before_insert
BEFORE INSERT ON contribution  
FOR EACH ROW
BEGIN       
    IF :NEW.contribution_id IS NULL THEN
        SELECT contribution_seq.NEXTVAL INTO :NEW.contribution_id FROM dual;
    END IF;
END;    
 



