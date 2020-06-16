CREATE SCHEMA IF NOT EXISTS currency_rates;

CREATE SEQUENCE currency_rates.queries_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE currency_rates.currencies_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE currency_rates.currency_rates_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS currency_rates.queries
(
    id         BIGINT PRIMARY KEY,
    date       TIMESTAMP WITH TIME ZONE,
    base       CHARACTER(15),
    target     CHARACTER(15),
    created_on TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS currency_rates.currency_rates
(
    id           BIGINT PRIMARY KEY,
    current_rate DECIMAL(10, 2),
    average      DECIMAL(10, 2),
    trend        VARCHAR,
    base         CHARACTER(15),
    target       CHARACTER(15),
    query_id     BIGINT
);

CREATE TABLE IF NOT EXISTS currency_rates.currencies
(
    id   BIGINT PRIMARY KEY,
    code CHARACTER(15) UNIQUE
);

ALTER TABLE currency_rates.currency_rates
    ADD FOREIGN KEY (query_id)
        REFERENCES currency_rates.queries (id);

