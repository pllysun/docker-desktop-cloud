create table if not exists label_table
(
    label_id   int         not null
    primary key,
    label_name varchar(50) null
    );

create table if not exists log_type_table
(
    log_type_id   int         not null
    primary key,
    log_type_name varchar(50) null
    );

create table if not exists occupation_table
(
    occupation_id   int         not null
    primary key,
    occupation_name varchar(50) null
    );

create table if not exists permissions_table
(
    permissions_id   int         not null
    primary key,
    permissions_name varchar(20) null
    );

create table if not exists personalise_table
(
    personalise_id   int          not null comment '个性化id'
    primary key,
    personalise_name varchar(255) null comment '个性化内容',
    personalise_type int          null comment '个性化类别（1：应用场景，2：软件）',
    occupation_id    int          null comment '对应的职位ID'
    );

create table if not exists recommended_table
(
    recommended_id        int auto_increment
    primary key,
    recommended_cpu       int null,
    recommended_memory    int null,
    recommended_data_disk int null
);

create table if not exists recommended_occupation
(
    recommended_id int null,
    occupation_id  int null,
    constraint recommended_occupation_ibfk_1
    foreign key (recommended_id) references recommended_table (recommended_id),
    constraint recommended_occupation_ibfk_2
    foreign key (occupation_id) references occupation_table (occupation_id)
    );

create index occupation_id
    on recommended_occupation (occupation_id);

create index recommended_id
    on recommended_occupation (recommended_id);

create table if not exists role_table
(
    role_id   int         not null
    primary key,
    role_name varchar(20) null
    );

create table if not exists role_permissions
(
    role_id        int null,
    permissions_id int null,
    constraint role_permissions_ibfk_1
    foreign key (role_id) references role_table (role_id),
    constraint role_permissions_ibfk_2
    foreign key (permissions_id) references permissions_table (permissions_id)
    );

create index permissions_id
    on role_permissions (permissions_id);

create index role_id
    on role_permissions (role_id);

create table if not exists scenario_table
(
    scenario_id   int         not null
    primary key,
    scenario_name varchar(50) null
    );

create table if not exists user_table
(
    user_id            int                                not null
    primary key,
    username           varchar(50)                        null,
    password           varchar(50)                        null,
    email              varchar(100)                       null,
    phone              varchar(20)                        null,
    occupation_id      int      default 1                 null,
    role_id            int      default 3                 null,
    number_of_desktops int      default 0                 null,
    create_time        datetime default CURRENT_TIMESTAMP null,
    update_time        datetime default CURRENT_TIMESTAMP null,
    user_head          varchar(100)                       null,
    name               varchar(255)                       null,
    constraint user_table_ibfk_1
    foreign key (role_id) references role_table (role_id),
    constraint user_table_ibfk_2
    foreign key (occupation_id) references occupation_table (occupation_id)
    );

create table if not exists date_value
(
    date_time       datetime default CURRENT_TIMESTAMP null,
    image_use_count int                                null,
    date_value_id   int auto_increment
    primary key,
    user_id         int                                null,
    desk_count      int                                null,
    desk_use        int                                null,
    constraint date_value_user_table_user_id_fk
    foreign key (user_id) references user_table (user_id)
    );

create index date_value_date_value_id_index
    on date_value (date_value_id);

create index date_value_user_id_index
    on date_value (user_id);

create table if not exists image_table
(
    image_id        varchar(100)  not null
    primary key,
    image_name      varchar(50)   null,
    image_system    varchar(50)   null,
    source          int           null,
    image_introduce varchar(100)  null,
    user_id         int           null,
    image_remark    varchar(128)  null,
    image_use_count int default 0 null,
    constraint user_id
    foreign key (user_id) references user_table (user_id)
    );

create table if not exists image_label
(
    image_id        varchar(100)  null,
    label_id        int           null,
    recommended_id  int           null,
    use_count       int default 0 null,
    image_remark    varchar(128)  null comment '镜像名称',
    image_introduce varchar(128)  null,
    image_photo     int           null,
    constraint image_label_ibfk_1
    foreign key (image_id) references image_table (image_id),
    constraint image_label_ibfk_2
    foreign key (label_id) references label_table (label_id),
    constraint image_label_recommended_table_recommended_id_fk
    foreign key (recommended_id) references recommended_table (recommended_id)
    );

create index image_id
    on image_label (image_id);

create index image_label_recommended_id_index
    on image_label (recommended_id);

create index label_id
    on image_label (label_id);

create table if not exists log_table
(
    log_id      int auto_increment
    primary key,
    user_id     int         null,
    log_time    datetime    null,
    log_type_id int         null,
    log_content varchar(50) null,
    user_ip     int         null,
    constraint log_table_ibfk_1
    foreign key (user_id) references user_table (user_id),
    constraint log_table_ibfk_2
    foreign key (log_type_id) references log_type_table (log_type_id)
    );

create index log_type_id
    on log_table (log_type_id);

create index user_id
    on log_table (user_id);

create table if not exists network_table
(
    network_id   varchar(100)                       not null
    primary key,
    network_name varchar(50)                        null,
    pod_selector varchar(50)                        null,
    user_ip      varchar(50)                        null,
    pod_count    int      default 0                 null,
    create_time  datetime default CURRENT_TIMESTAMP null,
    user_id      int                                null,
    constraint network_table_user_table_user_id_fk
    foreign key (user_id) references user_table (user_id)
    );

create index network_table_user_id_index
    on network_table (user_id);

create table if not exists pod_controller_table
(
    pod_controller_id       int auto_increment
    primary key,
    pod_controller_name     varchar(50)                        null,
    container_state         varchar(50)                        null,
    container_name          varchar(20)                        null,
    image_id                varchar(100)                       null,
    user_id                 int                                null,
    network_id              varchar(100)                       null,
    ip_address              varchar(50)                        null,
    pod_controller_cpu      int                                null,
    pod_controller_memory   int                                null,
    pod_controller_dataDisk int                                null,
    create_time             datetime default CURRENT_TIMESTAMP null,
    update_time             datetime default CURRENT_TIMESTAMP null,
    pod_controller_version  varchar(50)                        null,
    pod_port                int                                null,
    constraint pod_controller_table_ibfk_1
    foreign key (image_id) references image_table (image_id),
    constraint pod_controller_table_ibfk_2
    foreign key (user_id) references user_table (user_id),
    constraint pod_controller_table_ibfk_3
    foreign key (network_id) references network_table (network_id)
    );

create index image_id
    on pod_controller_table (image_id);

create index network_id
    on pod_controller_table (network_id);

create index user_id
    on pod_controller_table (user_id);

create table if not exists scenario_user
(
    scenario_id int null,
    user_id     int null,
    constraint scenario_user_ibfk_1
    foreign key (scenario_id) references scenario_table (scenario_id),
    constraint scenario_user_ibfk_2
    foreign key (user_id) references user_table (user_id)
    );

create index scenario_id
    on scenario_user (scenario_id);

create index user_id
    on scenario_user (user_id);

create index occupation_id
    on user_table (occupation_id);

create index role_id
    on user_table (role_id);

