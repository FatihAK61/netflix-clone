-- public.videos definition

-- Drop table

-- DROP TABLE public.videos;

CREATE TABLE public.videos
(
    id          int8           NOT NULL,
    created_at  timestamptz(6) NOT NULL,
    description varchar(4000)  NULL,
    duration    int4           NULL,
    poster      varchar(255)   NULL,
    published   bool           NOT NULL,
    rating      varchar(255)   NULL,
    src         varchar(255)   NULL,
    title       varchar(255)   NOT NULL,
    updated_at  timestamptz(6) NOT NULL,
    "year"      int4           NULL,
    CONSTRAINT videos_pkey PRIMARY KEY (id)
);

-- public.video_categories definition

-- Drop table

-- DROP TABLE public.video_categories;

CREATE TABLE public.video_categories
(
    video_id int8         NOT NULL,
    category varchar(255) NULL,
    CONSTRAINT fkqrdtbwe1eikatbh870575knpc FOREIGN KEY (video_id) REFERENCES public.videos (id)
);

-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users
(
    id                          int8           NOT NULL,
    active                      bool           NOT NULL,
    created_at                  timestamptz(6) NOT NULL,
    email                       varchar(255)   NOT NULL,
    email_verified              bool           NOT NULL,
    full_name                   varchar(255)   NOT NULL,
    "password"                  varchar(255)   NOT NULL,
    password_reset_token        varchar(255)   NULL,
    password_reset_token_expiry timestamptz(6) NULL,
    "role"                      varchar(255)   NOT NULL,
    updated_at                  timestamptz(6) NOT NULL,
    verification_token          varchar(255)   NULL,
    verification_token_expiry   timestamptz(6) NULL,
    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT uk7lga138i06veb3enx41uhe5tb UNIQUE (verification_token),
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY
                                        ((ARRAY ['USER'::character varying, 'ADMIN'::character varying, 'SUPERADMIN'::character varying, 'MANAGER'::character varying, 'NOTHING'::character varying, 'BITCH'::character varying])::text[])))
);

-- public.user_watchlist definition

-- Drop table

-- DROP TABLE public.user_watchlist;

CREATE TABLE public.user_watchlist
(
    user_id  int8 NOT NULL,
    video_id int8 NOT NULL,
    CONSTRAINT user_watchlist_pkey PRIMARY KEY (user_id, video_id),
    CONSTRAINT fk1a2sf5ha20f8a3sqgo3n4h3w6 FOREIGN KEY (user_id) REFERENCES public.users (id),
    CONSTRAINT fkb42iy6ra9e1l349ros4f3x18s FOREIGN KEY (video_id) REFERENCES public.videos (id)
);
