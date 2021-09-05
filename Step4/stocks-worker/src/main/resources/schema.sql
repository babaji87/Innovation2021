DROP TABLE if exists `stocksdata`;
CREATE TABLE stocksdata (
    id bigint NOT NULL DEFAULT nextval('stocksdata_id_seq2'::regclass),
    company_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    latest_price double precision NOT NULL,
    latest_time character varying(255) COLLATE pg_catalog."default",
    primary_exchange character varying(255) COLLATE pg_catalog."default" NOT NULL,
    symbol character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT stocksdata_pkey2 PRIMARY KEY (id)
);