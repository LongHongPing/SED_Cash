-- create database
create database vsse;
-- create table
create table tsets(
	stag varchar(255) not null,
	t varchar(10000) not null
)default charset=utf8;

create table xsets(
	xset varchar(10000) not null
)default charset=utf8;