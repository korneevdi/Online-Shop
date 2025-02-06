create schema if not exists catalogue;

create table catalogue.t_product
(
    id            serial primary key,
    c_title       varchar(50) not null check (length(trim(c_title)) >= 2),
    c_description varchar(1000)
);

create table catalogue.t_product_image
(
    id            serial primary key,
    product_id    integer not null,
    image_url     varchar(255),
    foreign key (product_id) references catalogue.t_product(id) on delete cascade
);