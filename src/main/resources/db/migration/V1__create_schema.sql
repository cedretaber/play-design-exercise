CREATE TABLE "shelves"
  ( "id" bigserial PRIMARY KEY
  , "name" TEXT NOT NULL
  );

CREATE TABLE "books"
  ( "id" bigserial PRIMARY KEY
  , "name" TEXT NOT NULL
  , "shelf_id" bigint NOT NULL REFERENCES "shelves" ("id")
  );