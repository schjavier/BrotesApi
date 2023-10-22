

create table clientes(
    id bigint not null auto_increment,
    nombre varchar(25) not null,
    direccion varchar(50) not null,
    telefono varchar(50) not null,

    primary key (id)


);