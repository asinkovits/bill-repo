create table bill (
    id varchar(36),
    name varchar(255),
    value varchar(16)
);

create table bill_file (
    bill_id varchar(36),
    file_name varchar(255),
    data blob
);