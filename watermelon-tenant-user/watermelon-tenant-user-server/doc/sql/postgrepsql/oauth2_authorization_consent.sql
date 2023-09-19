DROP TABLE IF EXISTS oauth2_authorization_consent;
CREATE TABLE oauth2_authorization_consent
(
    registered_client_id varchar(100)  NOT NULL,
    principal_name       varchar(200)  NOT NULL,
    authorities          varchar(1000) NOT NULL
)
;

ALTER TABLE oauth2_authorization_consent ADD CONSTRAINT oauth2_authorization_consent_pkey PRIMARY KEY (registered_client_id, principal_name);
