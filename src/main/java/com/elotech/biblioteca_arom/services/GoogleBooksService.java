package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.clients.GoogleBooksClient;
import com.elotech.biblioteca_arom.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Serviço responsável por buscar livros na API do Google Books e processar os resultados.
 */
@Service
public class GoogleBooksService {

    private final GoogleBooksClient googleBooksClient;

    /**
     * Construtor do serviço que injeta o cliente do Google Books.
     * O GoogleBooksClient é utilizado para realizar chamadas à API do Google Books.
     *
     * @param googleBooksClient o cliente Feign que faz a comunicação com a API do Google Books
     */
    @Autowired
    public GoogleBooksService(GoogleBooksClient googleBooksClient) {
        this.googleBooksClient = googleBooksClient;
    }

    /**
     * Realiza uma busca de livros na API do Google Books com base em um termo de consulta.
     * O método faz uma chamada à API e processa a resposta para retornar uma lista de objetos Book.
     *
     * @param query o termo de busca para encontrar livros (exemplo: título, autor, etc.)
     * @return Uma lista de objetos Book que correspondem ao termo de busca fornecido
     */
    public List<Book> searchBooks(String query) {
        Map<String, Object> response = googleBooksClient.searchBooks(query);
        return parseBooksFromResponse(response);
    }

    /**
     * Processa a resposta da API do Google Books e converte os dados para uma lista de objetos Book.
     * O método extrai informações como título, autor, ISBN, data de publicação e categoria.
     *
     * @param response um mapa que contém a resposta da API do Google Books
     * @return Uma lista de objetos Book com os dados extraídos da resposta
     */
    private List<Book> parseBooksFromResponse(Map<String, Object> response) {
        List<Book> books = new ArrayList<>();

        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
        if (items != null) {
            for (Map<String, Object> item : items) {
                Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
                Book book = new Book();

                book.setTitle((String) volumeInfo.get("title"));

                List<String> authors = (List<String>) volumeInfo.get("authors");
                if (authors != null && !authors.isEmpty()) {
                    book.setAuthor(authors.get(0));
                }

                List<Map<String, Object>> identifiers = (List<Map<String, Object>>) volumeInfo.get("industryIdentifiers");
                if (identifiers != null && !identifiers.isEmpty()) {
                    book.setIsbn((String) identifiers.get(0).get("identifier"));
                }

                book.setPublicationDate((String) volumeInfo.get("publishedDate"));

                List<String> categories = (List<String>) volumeInfo.get("categories");
                if (categories != null && !categories.isEmpty()) {
                    book.setCategory(categories.get(0));
                }

                books.add(book);
            }
        }

        return books;
    }
}
