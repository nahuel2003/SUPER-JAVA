drop database if exists doctormuelas;
create database doctormuelas character set utf8mb4;
use doctormuelas;
create table registro(
	userID int auto_increment primary key,
    usuario varchar(12) not null unique,
    nombre varchar(20) not null,
    apellido varchar(20) not null,
    dni int(12) not null unique,
    numero_telefonico int(13) unique,
    clave varchar(25) not null,
    tipo enum('cliente', 'administrador')
);