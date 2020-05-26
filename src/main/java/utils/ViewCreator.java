package utils;

import repositories.ViewRepository;

import javax.inject.Inject;
import java.sql.SQLException;

public class ViewCreator {

    public @Inject
    ViewRepository viewRepository;

    public ViewCreator(ViewRepository viewRepository) {
        this.viewRepository = viewRepository;
    }

    public void createViewForBookAuthor() {
        try {
            viewRepository.createView();
        } catch (SQLException e) {
            System.out.println("Couldn't create view for books list" + e.getMessage());
            e.printStackTrace();
        }
    }
}
