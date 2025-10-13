CREATE TABLE pedidos_recurrentes (
    id bigint not null auto_increment,
    cliente_id bigint not null,
    dia_entrega VARCHAR(15),

    primary key (id),
    constraint fk_pedidos_recurrentes_cliente_id
        foreign key (cliente_id)
        references clientes(id)
);