package controllers;

import repositories.LibraryCardRepository;
import javax.inject.Inject;

public class LibraryCardController {

    public @Inject
    LibraryCardRepository libraryCardRepository;

    public LibraryCardController(LibraryCardRepository libraryCardRepository) {
        this.libraryCardRepository = libraryCardRepository;
    }
}