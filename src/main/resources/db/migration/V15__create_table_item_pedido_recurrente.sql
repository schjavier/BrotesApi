create table item_pedido_recurrente (
    id bigint not null auto_increment,
    cantidad int not null,
    pedido_recurrente_id bigint not null,
    producto_id bigint not null,

    primary key(id),

    constraint fk_item_pedido_recurrente_id foreign key (pedido_recurrente_id)
        references pedidos_recurrentes(id) on delete cascade,

    constraint fk_item_pedido_producto_id foreign key (producto_id) references productos(id)


);