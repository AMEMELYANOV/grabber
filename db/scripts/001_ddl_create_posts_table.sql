CREATE TABLE IF NOT EXISTS posts (
  id SERIAL PRIMARY KEY,
  title VARCHAR NOT NULL,
  link VARCHAR NOT NULL UNIQUE,
  description VARCHAR NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

COMMENT ON TABLE posts IS 'Объявления';
COMMENT ON COLUMN posts.id IS 'Идентификатор объявления';
COMMENT ON COLUMN posts.title IS 'Заголовок объявления';
COMMENT ON COLUMN posts.link IS 'Ссылка объявления';
COMMENT ON COLUMN posts.description IS 'Описания объявления';
COMMENT ON COLUMN posts.created IS 'Дата и время создания объявления';