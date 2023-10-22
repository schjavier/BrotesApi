

create table productos(
    id bigint not null auto_increment,
    nombre varchar(30) not null,
    precio float not null,
    categoria_id bigint not null,

    primary key (id),
    constraint fk_productos_categoria_id foreign key (categoria_id) references categoria(id)



);