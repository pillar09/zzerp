SET search_path TO zzerp;

﻿INSERT INTO zzerp.category (title,parent) VALUES ('GPS',0),('摄像头',0);
INSERT INTO zzerp.attribute (id,title,category_id) VALUES (1,'尺寸',1);
INSERT INTO zzerp.attribute (id,title,category_id) VALUES (2,'主机方案',1);

UPDATE zzerp.category SET title = '摄像头' WHERE id = 2;

INSERT INTO zzerp.variety (id,attribute_id,title) VALUES(1,1,'4.3INCH');
INSERT INTO zzerp.variety (id,attribute_id,title) VALUES(2,1,'5.0INCH');
INSERT INTO zzerp.variety (id,attribute_id,title) VALUES(3,1,'7INCH');

INSERT INTO zzerp.variety (id,attribute_id,title) VALUES(4,2,'安卓');
INSERT INTO zzerp.variety (id,attribute_id,title) VALUES(5,2,'Windows');
INSERT INTO zzerp.variety (id,attribute_id,title) VALUES(6,2,'苹果系统');
INSERT INTO zzerp.variety (id,attribute_id,title) VALUES(7,2,'塞班');

SELECT * FROM  zzerp.variety

INSERT INTO zzerp.good_variety (good_id,variety_id)

INSERT INTO zzerp.good (num,category_id,price,description,model,title,id,state) VALUES('46878789',1,60.00,'GPS上架','703全功能','7INCH GPS',1,1);
INSERT INTO zzerp.good_variety(good_id,variety_id) VALUES(1,3);
INSERT INTO zzerp.good_variety(good_id,variety_id) VALUES(1,5);

SELECT * FROM zzerp.good_variety
 
SELECT * FROM zzerp.attribute where id = 2

DELETE FROM zzerp.attribute where id = 7


SELECT g.num,c.title,g.price,g.description,model,g.id,g.state,g.spec FROM good g, category c where g.category_id = c.id

SELECT a1.username as create_user, a2.username as salesman, b.create_time FROM bill b, account a1,account a2 where b.create_user = a1.id and b.salesman = a2.id 

SELECT * FROM bill b, account a1, account a2 WHERE 

INSERT INTO bill (title,num,state,create_user,salesman,create_time) VALUES('MY FIRST','2222',1,(SELECT id FROM account WHERE username='zz'),
(SELECT id FROM account WHERE username='zz'),now());

UPDATE bill SET salesman = (SELECT id FROM account WHERE username='sale1')

SELECT * FROM bill_good WHERE bill_id = 1

select * from account

UPDATE account SET full_name='李平' WHERE id = 1


update bill set create_user = 1, salesman = 2 where id> 0


SELECT b.id,b.num,b.create_user,b.salesman, a1.full_name AS create_user_full_name
FROM bill b, account a1, account a2
WHERE b.create_user = a1.id AND b.salesman = a2.id AND b.id = 2 