ALTER TABLE pedidos drop constraint fk_pedido_cliente_id;
ALTER TABLE pedidos add constraint fk_pedido_cliente_id foreign key (cliente_id) references clientes(id) ON DELETE CASCADE;
