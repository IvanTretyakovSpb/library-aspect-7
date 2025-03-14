package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.entity.BookEntity;
import ru.gb.springdemo.exception.NotFoundEntityException;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    // Метод для сопоставления и перевода BookEntity в Book
    static Book mapping(BookEntity bookEntity) {
        return new Book(bookEntity.getId(), bookEntity.getName());
    }

    // Метод для сопоставления и перевода Book в BookEntity
    static BookEntity mapping(Book book) {
        return new BookEntity(book.getId(), book.getName());
    }

    public Book getByID(Long id) {
        return mapping(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("Book with id = " + id + " not found")));
    }

    public List<Book> getAll() {
        return bookRepository.findAll().stream()
                .map(BookService::mapping)
                .toList();
    }

    public Book create(Book book) {
        return mapping(bookRepository.save(mapping(book)));
    }

    public Book update(Long id, Book book) {
        // проверяем, существует ли книга с данным ID
        Book updatedBook = getByID(id);
        // обновляем поля существующей книги
        updatedBook.setName(book.getName());
        // сохраняем и возвращаем обновленную книгу
        return mapping(bookRepository.save(mapping(updatedBook)));
    }

    public void deleteById(Long id) {
        // проверяем, существует ли книга с данным ID
        getByID(id);
        // если да, то удаляем ее (иначе выбрасывается исключение NotFoundEntityException("Book not found"))
        bookRepository.deleteById(id);
    }
}
