CREATE SCHEMA zzerp
  AUTHORIZATION postgres;
GRANT ALL ON SCHEMA zzerp TO postgres;
COMMENT ON SCHEMA zzerp IS '中智ERP';

-------------------------------------------------------------------[Good]START
-- Table: zzerp.attribute

-- DROP TABLE zzerp.attribute;

CREATE TABLE zzerp.attribute
(
  id serial NOT NULL,
  title text NOT NULL,
  CONSTRAINT pk_attribute PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.attribute OWNER TO postgres;
COMMENT ON TABLE zzerp.attribute IS '商品属性';

-- Table: zzerp.category

-- DROP TABLE zzerp.category;

CREATE TABLE zzerp.category
(
  title text NOT NULL,
  parent integer,
  description text,
  id serial NOT NULL,
  CONSTRAINT pk_category PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.category OWNER TO postgres;
COMMENT ON TABLE zzerp.category IS '类目';

-- Table: zzerp.good

-- DROP TABLE zzerp.good;

CREATE TABLE zzerp.good
(
  num text, -- 编号
  category_id integer,
  price numeric(10,2) NOT NULL,
  description text,
  model text,
  purchase_price numeric(10,2), -- 采购价格
  title text,
  id serial NOT NULL,
  state integer,
  spec text,
  factory_model text,
  reserve integer NOT NULL DEFAULT 0,
  CONSTRAINT pk_good PRIMARY KEY (id),
  CONSTRAINT fk_category FOREIGN KEY (category_id)
      REFERENCES zzerp.category (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.good OWNER TO postgres;
COMMENT ON TABLE zzerp.good IS '商品';
COMMENT ON COLUMN zzerp.good.num IS '编号';
COMMENT ON COLUMN zzerp.good.purchase_price IS '采购价格';

-- Table: zzerp.variety

-- DROP TABLE zzerp.variety;

CREATE TABLE zzerp.variety
(
  attribute_id integer NOT NULL,
  id serial NOT NULL,
  title text,
  CONSTRAINT pk_variety PRIMARY KEY (id),
  CONSTRAINT fk_attribute FOREIGN KEY (attribute_id)
      REFERENCES zzerp.attribute (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.variety OWNER TO postgres;
COMMENT ON TABLE zzerp.variety IS '类型';

-- Table: zzerp.good_variety

-- DROP TABLE zzerp.good_variety;

CREATE TABLE zzerp.good_variety
(
  good_id integer NOT NULL,
  variety_id integer NOT NULL,
  CONSTRAINT pk_good_variety PRIMARY KEY (good_id, variety_id),
  CONSTRAINT fk_good FOREIGN KEY (good_id)
      REFERENCES zzerp.good (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_variety FOREIGN KEY (variety_id)
      REFERENCES zzerp.variety (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.good_variety OWNER TO postgres;
COMMENT ON TABLE zzerp.good_variety IS '商品属性关系绑定';
---------------------------------------------------------------------[Good]END

----------------------------------------------------------------[Account]START
-- Table: zzerp.capacity

-- DROP TABLE zzerp.capacity;

CREATE TABLE zzerp.capacity
(
  id serial NOT NULL,
  privilege text,
  description text,
  post text,
  CONSTRAINT pk_capacity PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.capacity OWNER TO postgres;
COMMENT ON TABLE zzerp.capacity IS '角色';
-- Table: zzerp.account

-- DROP TABLE zzerp.account;

CREATE TABLE zzerp.account
(
  id serial NOT NULL,
  username text NOT NULL,
  cipher text, -- 密码
  capacity_id integer,
  description text,
  phone_num text,
  mail text,
  state integer NOT NULL DEFAULT 0,
  full_name text,
  male boolean NOT NULL DEFAULT true,
  num text,
  CONSTRAINT pk_account PRIMARY KEY (id),
  CONSTRAINT fk_capacity FOREIGN KEY (capacity_id)
      REFERENCES zzerp.capacity (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT u UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.account OWNER TO postgres;
COMMENT ON TABLE zzerp.account IS '账户';
COMMENT ON COLUMN zzerp.account.cipher IS '密码';


------------------------------------------------------------------[Account]END


-------------------------------------------------------------------[Bill]START
-- Table: zzerp.bill

-- DROP TABLE zzerp.bill;

CREATE TABLE zzerp.bill
(
  title text,
  num text,
  state integer,
  create_user integer NOT NULL,
  amount numeric(10,2) NOT NULL DEFAULT 0,
  salesman integer,
  order_time timestamp without time zone,
  pay_time timestamp without time zone,
  accept_time timestamp without time zone,
  create_time timestamp without time zone,
  id serial NOT NULL,
  address text,
  consignee text,
  zip_code text,
  buyer_note text,
  salesman_note text,
  country text,
  delay_limit integer,
  phone_num text,
  freight numeric(10,2),
  buyer text,
  logistics text,
  CONSTRAINT pk_bill PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.bill OWNER TO postgres;
COMMENT ON TABLE zzerp.bill IS '订单';

-- Table: zzerp.bill_good

-- DROP TABLE zzerp.bill_good;

CREATE TABLE zzerp.bill_good
(
  id serial NOT NULL,
  bill_id integer NOT NULL,
  good_id integer NOT NULL,
  quantity integer NOT NULL DEFAULT 1,
  price numeric(10,2) NOT NULL DEFAULT 0,
  CONSTRAINT pk_bill_good PRIMARY KEY (id),
  CONSTRAINT fk_bill FOREIGN KEY (bill_id)
      REFERENCES zzerp.bill (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_good FOREIGN KEY (good_id)
      REFERENCES zzerp.good (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zzerp.bill_good OWNER TO postgres;
COMMENT ON TABLE zzerp.bill_good IS '订单商品关系绑定';


---------------------------------------------------------------------[Bill]END