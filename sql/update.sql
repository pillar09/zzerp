ALTER TABLE bill ADD COLUMN pay_with text;
ALTER TABLE bill ADD COLUMN deliver_with text;
ALTER TABLE bill ADD COLUMN waybill_num text;
ALTER TABLE bill ADD COLUMN deliver_time timestamp without time zone;

--1.0α
ALTER TABLE bill ADD COLUMN address2 text;
ALTER TABLE bill ADD COLUMN province text;
ALTER TABLE bill ADD COLUMN tel_num text;
ALTER TABLE bill ADD COLUMN fax_num text;
ALTER TABLE bill ADD COLUMN city text;
ALTER TABLE bill ADD COLUMN link text;
ALTER TABLE bill ADD COLUMN mail text;
ALTER TABLE bill ADD COLUMN buyer_phone_num text;
ALTER TABLE bill ADD COLUMN consignor integer;
ALTER TABLE bill ADD FOREIGN KEY (consignor) REFERENCES account (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
--1.0α1
ALTER TABLE bill ADD COLUMN limit_time timestamp without time zone;
ALTER TABLE bill ADD COLUMN amount_declared numeric(10,2); -- 申报金额
ALTER TABLE bill ADD COLUMN declaration text; -- 申报内容
ALTER TABLE bill ADD COLUMN expect_deliver_with text; -- 客户要求发货方式
ALTER TABLE bill ADD COLUMN store_num text; -- 店铺号
ALTER TABLE bill ADD COLUMN last_edit_user integer; -- 最后编辑
ALTER TABLE bill ADD FOREIGN KEY (consignor) REFERENCES account (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE bill ADD COLUMN buyer_wangwang text; -- 买家在线IDID
ALTER TABLE bill ADD COLUMN commission_rate numeric(5,2); 
ALTER TABLE bill ADD COLUMN expected_arrival timestamp without time zone;
--1.0α2
ALTER TABLE bill ADD COLUMN buyer_tel_num text;
--1.0α3
UPDATE zzerp.bill SET state=9 WHERE state=10;
INSERT into account(id,username,cipher,capacity_id,description,state) values(0,'',0,(select id from zzerp.capacity where privilege='ROLE_ADMIN'),'',0);
UPDATE bill SET consignor = 0 WHERE consignor is null;

ALTER TABLE bill
  ALTER COLUMN salesman SET NOT null,
  ALTER COLUMN salesman SET DEFAULT 0,
  ALTER COLUMN consignor SET NOT null,
  ALTER COLUMN consignor SET DEFAULT 0,
  ALTER COLUMN freight DROP DEFAULT,
  ALTER COLUMN freight DROP NOT null;
--1.0α4
--1.0α6
ALTER TABLE bill ADD COLUMN submit_time timestamp without time zone;
--1.0α8
ALTER TABLE bill RENAME COLUMN receipt_time TO accept_time;
--1.0α9(2013-01-19)
CREATE TABLE zzerp.pack
(
  bill_id integer NOT NULL,
  length integer,
  width integer,
  height integer,
  weight numeric(5,2),
  volume_rate integer,
  id serial NOT NULL,
  CONSTRAINT pk_pack PRIMARY KEY (id),
  CONSTRAINT fk_bill FOREIGN KEY (bill_id)
      REFERENCES zzerp.bill (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.pack OWNER TO postgres;
COMMENT ON TABLE zzerp.pack IS '包裹';

--1.0α11(2013-02-02)
ALTER TABLE bill ADD COLUMN edit_time timestamp without time zone;
ALTER TABLE bill ADD COLUMN total_weitht numeric(5,2); 
ALTER TABLE bill ADD COLUMN total_volume_weight numeric(5,2); 

--1.0α12(2013-03-17)
ALTER TABLE bill RENAME COLUMN actual_deliver_with TO expect_deliver_with;

CREATE TABLE zzerp.warehousing
(
  id serial NOT NULL,
  warehouse_date timestamp without time zone NOT NULL,
  warehouser integer NOT NULL,
  memo text,
  CONSTRAINT pk_warehousing PRIMARY KEY (id),
  CONSTRAINT fk_warehouser FOREIGN KEY (warehouser)
      REFERENCES zzerp.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.warehousing OWNER TO postgres;
COMMENT ON TABLE zzerp.warehousing IS '入库';

CREATE TABLE zzerp.warehousing_good
(
  id serial NOT NULL,
  warehousing_id integer NOT NULL,
  good_id integer NOT NULL,
  quantity integer NOT NULL,
  CONSTRAINT pk_warehousing_good PRIMARY KEY (id),
  CONSTRAINT fk_warehousing_good FOREIGN KEY (good_id)
      REFERENCES zzerp.good (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_warehousing_id FOREIGN KEY (warehousing_id)
      REFERENCES zzerp.warehousing (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.warehousing_good OWNER TO postgres;
COMMENT ON TABLE zzerp.warehousing_good IS '入库商品及数量';

--1.0α15(2013-05-17)
ALTER TABLE pack ADD COLUMN barcode text;

--1.0α16(2013-05-25)
ALTER TABLE pack ADD COLUMN deliver_with text;
ALTER TABLE pack ADD COLUMN waybill_num text;
ALTER TABLE pack ADD COLUMN freight numeric(10,2);

--1.0α17(2013-06-01)
ALTER TABLE pack 
	ALTER COLUMN length TYPE numeric(5,2) ,
	ALTER COLUMN width TYPE numeric(5,2) ,
	ALTER COLUMN height TYPE numeric(5,2) ;
	
--1.0α18(2013-06-08)
ALTER TABLE pack ADD COLUMN state integer;
ALTER TABLE pack ADD COLUMN logistics text;--快递代理

--1.0α20(2013-07-20)
ALTER TABLE bill ADD COLUMN issue integer;
update bill set issue=1 where state=-9;
update bill set state=9 where state=-9;

--1.0α21(2013-07-27)
--fix
ALTER TABLE warehousing ADD COLUMN create_time timestamp without time zone;

--1.0α22(2013-08-03)
ALTER TABLE bill RENAME COLUMN total_weitht TO total_weight;

--1.0α25(2013-11-05)
ALTER TABLE zzerp.bill
  ALTER COLUMN amount DROP NOT null;

--1.0α26(2013-11-25)
alter table warehousing_good add column reserve integer DEFAULT NULL,
add column ordered integer DEFAULT NULL;

--1.0α28(2014-04-20)
alter table bill_good add column reserve integer DEFAULT NULL,
add column reserve_time timestamp without time zone DEFAULT NULL,
add column ordered integer DEFAULT NULL;
alter table warehousing_good add column reserve_time timestamp without time zone DEFAULT NULL;
--update bill_good bg set reserve_time=(select deliver_time from bill b where b.id=bg.bill_id);
--update warehousing_good wg set reserve_time=(select warehouse_date from warehousing w where w.id=wg.warehousing_id);

--1.0α31(2014-08-01)
CREATE TABLE zzerp.declaration
(
  declaration_id serial NOT NULL,
  bill_id integer NOT NULL,
  quantity integer NOT NULL DEFAULT 1,
  price numeric(10,2) NOT NULL DEFAULT 0,
  declaration text,
  CONSTRAINT declaration_id PRIMARY KEY (declaration_id)
);
COMMENT ON TABLE zzerp.declaration IS '申报内容';

--1.0α32(2014-08-10)
alter table declaration add column weight integer NOT NULL default 0, add column declaration_en text, add column currency text default 'USD';

--1.0α33(2014-08-27)
alter table zzerp.declaration add column code text DEFAULT NULL;
alter table zzerp.bill add column weight_declared numeric(5,2);
alter table zzerp.bill ALTER column weight_declared TYPE numeric(10,0);


CREATE TABLE zzerp.config
(
  term character varying(255) NOT NULL,
  define text,
  description character varying(255) DEFAULT NULL::character varying,
  category character varying(255) DEFAULT NULL::character varying,
  CONSTRAINT config_pkey PRIMARY KEY (term)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.config OWNER TO postgres;

INSERT INTO zzerp.config (term, define, description, category) VALUES ('server_host', 'http://172.16.30.197:8480/', '服务器地址', NULL);
INSERT INTO zzerp.config (term, define, description, category) VALUES ('uploaded_file_dir', '/uploaded/', '上传文件的目录', NULL);

--1.0
alter table zzerp.pack add column deliver_time timestamp without time zone;