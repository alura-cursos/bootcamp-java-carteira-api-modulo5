create table perfis(
	id bigint not null auto_increment primary key,
	nome varchar(100) not null
);

create table perfis_usuarios(
	perfil_id bigint not null,
	usuario_id bigint not null,
	
	primary key (perfil_id, usuario_id),
	foreign key (perfil_id) references perfis(id),
	foreign key (usuario_id) references usuarios(id)
);

insert into perfis values(1, 'ROLE_ADMIN');
insert into perfis values(2, 'ROLE_COMUM');

insert into perfis_usuarios(perfil_id, usuario_id) select 2,id from usuarios;
