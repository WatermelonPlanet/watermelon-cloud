
DROP TABLE IF EXISTS oauth2_authorization;
CREATE TABLE oauth2_authorization
(
    id                            varchar(100) NOT NULL,
    registered_client_id          varchar(100) NOT NULL,
    principal_name                varchar(200) NOT NULL,
    authorization_grant_type      varchar(100) NOT NULL,
    authorized_scopes             varchar(1000) DEFAULT NULL,
    attributes                    bytea,
    state                         varchar(500)  DEFAULT NULL:: character varying,
    authorization_code_value      bytea,
    authorization_code_issued_at  timestamp(6),
    authorization_code_expires_at timestamp(6),
    authorization_code_metadata   bytea,
    access_token_value            bytea,
    access_token_issued_at        timestamp(6),
    access_token_expires_at       timestamp(6),
    access_token_metadata         bytea,
    access_token_type             varchar(100)  DEFAULT NULL,
    access_token_scopes           varchar(1000) DEFAULT NULL,
    oidc_id_token_value           bytea,
    oidc_id_token_issued_at       timestamp(6),
    oidc_id_token_expires_at      timestamp(6),
    oidc_id_token_metadata        bytea,
    refresh_token_value           bytea,
    refresh_token_issued_at       timestamp(6),
    refresh_token_expires_at      timestamp(6),
    refresh_token_metadata        bytea,
    user_code_value               bytea,
    user_code_issued_at           timestamp(6),
    user_code_expires_at          timestamp(6),
    user_code_metadata            bytea,
    device_code_value             bytea,
    device_code_issued_at         timestamp(6),
    device_code_expires_at        timestamp(6),
    device_code_metadata          bytea
)
;
ALTER TABLE oauth2_authorization ADD CONSTRAINT oauth2_authorization_pkey PRIMARY KEY (id);
