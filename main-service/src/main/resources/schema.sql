drop table if exists users, locations, participation_requests, events, categories;

create table if not exists users
(
    id    bigint generated by default as identity
        primary key,
    email varchar(255),
    name  varchar(255)
);

alter table users
    owner to main;


create table if not exists locations
(
    id  bigint generated by default as identity
        primary key,
    lat real,
    lon real
);

alter table locations
    owner to main;

create table if not exists categories
(
    id           bigint generated by default as identity
        primary key,
    name         varchar(255)
        constraint uk_t8o6pivur7nn124jehx7cygw5
            unique
);

alter table categories
    owner to main;

create table if not exists events
(
    id                 bigint generated by default as identity
        primary key,
    annotation         varchar(2000),
    confirmed_requests integer,
    created_on         timestamp,
    description        varchar(7000),
    event_date         timestamp,
    paid               boolean default false,
    participant_limit  integer default 0,
    request_moderation boolean default true,
    state              varchar(255),
    title              varchar(120),
    published_on       timestamp,
    category_id        bigint,
    views              integer
        constraint fko6mla8j1p5bokt4dxrlmgwc28
            references categories,
    user_id            bigint
        constraint fkat8p3s7yjcp57lny4udqvqncq
            references users,
    location_id        bigint
        constraint fk7a9tiyl3gaugxrtjc2m97awui
            references locations
);

alter table events
    owner to main;

create table if not exists participation_requests
(
    id           bigint generated by default as identity
        primary key,
    created      timestamp,
    state        varchar(255),
    event_id     bigint
        constraint fkhn1n2kxqm6ptxjslwc3rvdd92
            references events,
    requester_id bigint
        constraint fk9ijgw51yrcw1mcq5irnky1h8w
            references users
);

alter table participation_requests
    owner to main;
