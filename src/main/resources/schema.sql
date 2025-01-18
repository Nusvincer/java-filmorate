CREATE TABLE IF NOT EXISTS ratings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(320) NOT NULL,
    login VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT unique_email UNIQUE (email),
    CONSTRAINT unique_login UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS films (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    rating_id INT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_film_rating FOREIGN KEY (rating_id) REFERENCES ratings (id)
);

CREATE TABLE IF NOT EXISTS genres (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
    film_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_likes_film_id ON likes (film_id);
CREATE INDEX idx_likes_user_id ON likes (user_id);

CREATE TABLE IF NOT EXISTS friends (
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);