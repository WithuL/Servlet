use ordersystem;

drop table if exists dishes;

create table dishes (
dishid int primary key auto_increment,
name varchar(50),
price int
);

drop table if exists user;
create table user (
userid int primary key auto_increment,
name varchar(50) unique,
password varchar(50),
isAdmin int
);

drop table if exists orderuser;
create table orderuser(
orderid int primary key auto_increment,
userid int,
time datetime,
isdone int,
foreign key(userid) references user(userid)
);

drop table if exists orderdish;
create table orderdish(
orderid int,
dishid int,
foreign key(orderid) references orderuser(orderid),
foreign key(dishid) references dishes(dishid)
);

insert into dishes values(
null, '宫保鸡丁', 2800)