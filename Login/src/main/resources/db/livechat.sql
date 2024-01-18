drop database if exists ;
create database livechat;
use livechat;
create table login
    userId varchar(20),
    userName varchar(20),
    passWord varchar(100),
    email varchar(50);