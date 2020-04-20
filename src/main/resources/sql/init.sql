CREATE TABLE IF NOT EXISTS categories(
    ID integer primary key auto_increment,
    title varchar(255) not null,
    description text,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tags(
    ID integer primary key auto_increment,
    title varchar(255) not null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bankDetails(
    ID integer primary key auto_increment,
    title varchar(255) not null,
    bic varchar(10) not null,
    iban varchar(20) not null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS persons(
    ID integer primary key auto_increment,
    title varchar(255) default '',
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    birthDate date default null,
    profilePic blob default null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts(
    ID integer primary key auto_increment,
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

CREATE TABLE IF NOT EXISTS standingOrders(
    ID integer primary key auto_increment,
    title varchar(255) not null,
    start DATE NOT NULL,
    days integer default 0,
    months integer default 0,
    amount double default 0.0,
    category integer default 0,
    bankDetail integer default 0,
    account integer NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key(category) references categories(ID) on delete cascade on update cascade,
    foreign key(bankDetail) references bankDetails(ID) on delete cascade on update cascade,
    foreign key(account) references accounts(ID) on delete cascade on update cascade
);

CREATE TABLE IF NOT EXISTS standingOrders_tags(
    ID integer primary key auto_increment,
    standingOrder integer not null,
    tag integer not null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key(standingOrder) references standingOrders(ID) on delete cascade on update cascade,
    foreign key(tag) references tags(ID) on delete cascade on update cascade
);

CREATE TABLE IF NOT EXISTS transactions(
    ID integer primary key auto_increment,
    title varchar(255) not null,
    description text,
    value double not null,
    date DATE NOT NULL,
    system boolean DEFAULT false,
    category integer default 0,
    bankDetail integer default 0,
    account integer not null,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key(category) references categories(ID) on delete cascade on update cascade,
    foreign key(bankDetail) references bankDetails(ID) on delete cascade on update cascade,
    foreign key(account) references accounts(ID) on delete cascade on update cascade
);