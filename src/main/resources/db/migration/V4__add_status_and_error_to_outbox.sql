alter table outbox add column status varchar(20) default 'PENDING';
alter table outbox add column error_message text;

update outbox set status = 'PROCESSED' where processed=true;
update outbox set status = 'PENDING' where processed = false;

alter table outbox drop column processed;