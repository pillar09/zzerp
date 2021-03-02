insert into zzerp.capacity (privilege,description) values('ROLE_USER','普通用户');
insert into zzerp.capacity (privilege,description) values('ROLE_ADMIN','管理员');
insert into zzerp.capacity (privilege,description,post) values('ROLE_COST','采购员','采购员');

insert into zzerp.account(username,cipher,capacity_id,description,state) values('zz',1,(select id from zzerp.capacity where privilege='ROLE_ADMIN'),'管理员账户',0);
insert into zzerp.account(username,cipher,capacity_id,description,state) values('sale1',1,(select id from zzerp.capacity where privilege='ROLE_USER'),'销售',0);

--用户登录
SELECT username,cipher,state=0 AS enabled FROM zzerp.account WHERE username='zz'

--用户授权
SELECT u.username, ur.privilege FROM zzerp.account u, zzerp.capacity ur 
WHERE u.capacity_id = ur.id AND u.username ='zz'