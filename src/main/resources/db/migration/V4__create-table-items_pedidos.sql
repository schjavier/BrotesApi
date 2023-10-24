

create table items_pedido(
    id bigint not null auto_increment,
    cantidad int not null,
    pedido_id bigint not null,
    producto_id bigint not null,

    primary key (id),
    constraint fk_items_pedido_pedido_id foreign key (pedido_id) references pedidos(id),
    constraint fk_items_pedido_producto_id foreign key (producto_id) references productos(id)


);