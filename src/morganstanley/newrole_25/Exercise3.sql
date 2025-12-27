

-- Write an SQL query that returns the latest book published by each author given:
-- books(author, pubDate, title)

  SELECT b.author, title from books b,
    (select author, max(pubdate) as latest_book from books group by author) AS recent_books
      where b.author = recent_books.author
      AND b.pubdate = recent_books.latest_book
