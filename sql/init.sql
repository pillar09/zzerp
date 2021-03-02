INSERT INTO zzerp.category (title,parent) VALUES ('其它',0),('手持GPS',0),('倒车摄像头',0),('车载DVD',0);

INSERT INTO zzerp.good (num,category_id,price,description,model,title,id,state) VALUES('46878789',1,60.00,'GPS上架','703全功能','7INCH GPS',1,1);


INSERT INTO bill (title,num,state,create_user,salesman,create_time) VALUES('MY FIRST','2222',1,(SELECT id FROM account WHERE username='zz'),
(SELECT id FROM account WHERE username='zz'),now());

SELECT * FROM bill_good WHERE bill_id = 1

select * from account

UPDATE account SET full_name='李平' WHERE id = 1


update bill set create_user = 1, salesman = 2 where id> 0


SELECT b.id,b.num,b.create_user,b.salesman, a1.full_name AS create_user_full_name
FROM bill b, account a1, account a2
WHERE b.create_user = a1.id AND b.salesman = a2.id AND b.id = 2 