create database order_system;
use order_system;

drop table if exists dishes;
create table dishes (
    dishId int primary key auto_increment,
    name varchar(50),
    price int  -- 以分为单位, 使用 int 表示. 尽量不要用 float double, 会存在误差
);

drop table if exists user;
create table user (
    userId int primary key auto_increment,
    password varchar(50),
    isAdmin int  -- 是否是管理员, 0 表示不是管理员, 1 表示是管理员
    name varchar(50) unique,
);

drop table if exists order_user;
create table order_user (
    orderId int primary key auto_increment,
    userId int,    -- 这个用户 id 需要和 user 表中的 userId 具有关联关系
    time datetime, -- 下单时间
    isDone int,    -- 1 表示订单完结, 0 表示订单未完结.
    foreign key(userId) references user(userId)
);

drop table if exists order_dish;
create table order_dish (
    orderId int,   -- orderId 也和 order_user 表的 orderId 字段有外键关系
    dishId int,    -- dishId 也和 dishes 表中的 dishId 存在外键关系
    foreign key(orderId) references order_user(orderId),
    foreign key(dishId) references dishes(dishId)
);



update dishes set price = 36 where dishId = 2;
update dishes set price = 36 where dishId = 3;
update dishes set price = 38 where dishId = 4;
update dishes set price = 38 where dishId = 5;
update dishes set price = 38 where dishId = 6;
update dishes set price = 38 where dishId = 7;
update dishes set price = 45 where dishId = 8;
update dishes set price = 45 where dishId = 9;
