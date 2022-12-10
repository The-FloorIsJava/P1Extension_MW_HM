--drop table user_table  ;
--drop table reimbursement_ticket ;

create table user_table(
user_name varchar(255) primary key,
password varchar(255)not null,
position varchar(255)default 'employee'
);

insert into user_table values ('andrew','and','manager');
insert into user_table values ('harsh','har','employee');
insert into user_table values ('charles','cha','employee');
insert into user_table values ('ted','ted','manager');

select * from user_table;


create table reimbursement_ticket(
ticket_id serial primary key, /* Changed from numeric to serial - MW */
user_name varchar(255),
status varchar(255) default 'pending',
description varchar (255),
amount numeric,

foreign key(user_name) references user_table(user_name)
);

insert into reimbursement_ticket values ('harsh',1,4,'denied','not reimbursed',500);
insert into reimbursement_ticket values ('ted',4,1,'approved','reimbursed',600);
insert into reimbursement_ticket values ('charles',3,3,'pending','not reimbursed',800);
insert into reimbursement_ticket values ('andrew',2,2,'pending','reimbursed',400);

select * from reimbursement_ticket;

select * from user_table join reimbursement_ticket on user_table.user_name = reimbursement_ticket.user_name ;

drop table user_table  ;
drop table reimbursement_ticket ;

/*
multiplicity - relations between numbers of items
one-to-many - one customer has many cart items - 90% of all the multiplicity relationships
many-to-many - many type of coffee could be owned by many customers - this is managed by a intermediary table with two foreign keys - 
sometimes referred to as a junction table - eg a single customer might have many cart items, but a single type of coffee may belong to many carts
one-to-one - one customer may have one rewards account
normalization is the process of eliminating redundant data by splitting data into multiple tables while still maintaining all relationships.
the ideal, which is called 3rd normal form: we want to have a single primary key, and we want all values in a table to be directly (not transitively)
related to that primary key.
*/