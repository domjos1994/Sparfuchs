CREATE TABLE IF NOT EXISTS categories(
    ID integer primary key autoincrement,
    title varchar(255) not null,
    description text,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tags(
    ID integer primary key autoincrement,
    title varchar(255) not null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bankDetails(
    ID integer primary key autoincrement,
    title varchar(255) not null,
    bic varchar(10) not null,
    iban varchar(20) not null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS persons(
    ID integer primary key autoincrement,
    title varchar(255) default '',
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    birthDate date default null,
    profilePic blob default null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts(
    ID integer primary key autoincrement,
    title varchar(255) not null,
    description text,
    isCash boolean default false,
    start double not null,
    bankDetail integer default 0,
    person integer not null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key(bankDetail) references bankDetails(ID) on delete cascade on update cascade,
    foreign key(person) references persons(ID) on delete cascade on update cascade
);

CREATE TABLE IF NOT EXISTS transactions(
    ID integer primary key autoincrement,
    title varchar(255) not null,
    description text,
    value double not null,
    bankDetail integer default 0,
    account integer not null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key(bankDetail) references bankDetails(ID) on delete cascade on update cascade,
    foreign key(account) references accounts(ID) on delete cascade on update cascade
);