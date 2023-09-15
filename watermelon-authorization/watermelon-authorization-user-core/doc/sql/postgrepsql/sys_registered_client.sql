DROP TABLE IF EXISTS sys_registered_client;
CREATE TABLE sys_registered_client
(
    id                            varchar(64)  NOT NULL,
    client_id                     varchar(100) NOT NULL,
    client_id_issued_at           timestamp(6),
    client_secret                 varchar(200),
    client_secret_expires_at      timestamp(6),
    client_name                   varchar(200) NOT NULL,
    client_authentication_methods jsonb,
    authorization_grant_types     jsonb,
    redirect_uris                 jsonb,
    post_logout_redirect_uris     jsonb,
    scopes                        jsonb,
    client_settings               json,
    token_settings                json
)
;

-- ----------------------------
-- Records of sys_registered_client
-- ----------------------------
INSERT INTO sys_registered_client VALUES ('1702591381795115010', 'device-messaging-client', NULL, NULL, NULL, 'a8513cd1-ad98-4817-9c60-58e7712af873', '[none]', '[refresh_token, urn:ietf:params:oauth:grant-type:device_code]', '[]', '[]', '[message.read, message.write]', '{settings.client.require-proof-key: false, settings.client.require-authorization-consent: false}', '{settings.token.access-token-format: {value: self-contained}, settings.token.reuse-refresh-tokens: true, settings.token.device-code-time-to-live: PT5M, settings.token.access-token-time-to-live: PT5M, settings.token.refresh-token-time-to-live: PT1H, settings.token.id-token-signature-algorithm: RS256, settings.token.authorization-code-time-to-live: PT5M}');
INSERT INTO sys_registered_client VALUES ('1702591023249235970', 'messaging-client', NULL, '{noop}secret', NULL, '9069244b-ca7c-4788-93f1-64f23e0b2250', '[client_secret_basic]', '[refresh_token, client_credentials, authorization_code, password]', '[http://127.0.0.1:8080/authorized, http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc]', '[http://127.0.0.1:8080/logged-out]', '[openid, profile, message.read, message.write]', '{settings.client.require-proof-key: false, settings.client.require-authorization-consent: true}', '{settings.token.access-token-format: {value: self-contained}, settings.token.reuse-refresh-tokens: true, settings.token.device-code-time-to-live: PT5M, settings.token.access-token-time-to-live: PT5M, settings.token.refresh-token-time-to-live: PT1H, settings.token.id-token-signature-algorithm: RS256, settings.token.authorization-code-time-to-live: PT5M}');

-- ----------------------------
-- Primary Key structure for table sys_registered_client
-- ----------------------------
ALTER TABLE sys_registered_client ADD CONSTRAINT sys_registered_client_pkey PRIMARY KEY (id);
