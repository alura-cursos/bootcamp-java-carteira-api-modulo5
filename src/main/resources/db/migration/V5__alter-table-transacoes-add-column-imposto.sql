alter table transacoes add imposto decimal(19,2);
update transacoes set imposto = ((preco * quantidade) * 15 / 100) where tipo = 'VENDA' and ((preco * quantidade) > 20000);
update transacoes set imposto = 0 where imposto is null;