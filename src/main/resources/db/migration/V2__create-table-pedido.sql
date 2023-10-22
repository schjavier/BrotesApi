

create table pedidos(
    id bigint not null auto_increment,
    cliente_id bigint not null,
    precio_total float not null,
    fecha datetime not null,

    primary key (id),
    constraint fk_pedido_cliente_id foreign key (cliente_id) references clientes(id)



);