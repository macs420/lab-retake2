import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static BookManager bookManager;
    private static ObservableList<Book> booksObservableList;

    public static void main(String[] args) {
        List<Book> initialBooks = new ArrayList<>();
        initialBooks.add(new Book("Book1", "Author1", "ISBN001", 2001));
        initialBooks.add(new Book("Book2", "Author2", "ISBN002", 2002));
        initialBooks.add(new Book("Book3", "Author3", "ISBN003", 2003));
        initialBooks.add(new Book("Book4", "Author4", "ISBN004", 2004));
        initialBooks.add(new Book("Book5", "Author5", "ISBN005", 2005));

        bookManager = new BookManager(initialBooks);
        booksObservableList = FXCollections.observableArrayList(initialBooks);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Book Manager");

        ListView<Book> listView = new ListView<>(booksObservableList);
        listView.setPrefSize(400, 200);

        Button addButton = new Button("Add Book");
        Button removeButton = new Button("Remove Book");
        Button updateButton = new Button("Update Book");

        VBox vBox = new VBox(10, listView, addButton, removeButton, updateButton);
        vBox.setPadding(new Insets(10));

        addButton.setOnAction(e -> showAddBookDialog());
        removeButton.setOnAction(e -> showRemoveBookDialog(listView.getSelectionModel().getSelectedItem()));
        updateButton.setOnAction(e -> showUpdateBookDialog(listView.getSelectionModel().getSelectedItem()));

        primaryStage.setScene(new Scene(vBox));
        primaryStage.show();
    }

    private void showAddBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add Book");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField isbnField = new TextField();
        TextField yearField = new TextField();

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("Year:"), 0, 3);
        grid.add(yearField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Book(titleField.getText(), authorField.getText(), isbnField.getText(), Integer.parseInt(yearField.getText()));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(book -> {
            bookManager.addBook(book);
            booksObservableList.add(book);
        });
    }

    private void showRemoveBookDialog(Book book) {
        if (book != null) {
            bookManager.removeBook(book);
            booksObservableList.remove(book);
        }
    }

    private void showUpdateBookDialog(Book oldBook) {
        if (oldBook != null) {
            Dialog<Book> dialog = new Dialog<>();
            dialog.setTitle("Update Book");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField titleField = new TextField(oldBook.getTitle());
            TextField authorField = new TextField(oldBook.getAuthor());
            TextField isbnField = new TextField(oldBook.getIsbn());
            TextField yearField = new TextField(String.valueOf(oldBook.getYear()));

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Author:"), 0, 1);
            grid.add(authorField, 1, 1);
            grid.add(new Label("ISBN:"), 0, 2);
            grid.add(isbnField, 1, 2);
            grid.add(new Label("Year:"), 0, 3);
            grid.add(yearField, 1, 3);

            dialog.getDialogPane().setContent(grid);

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    return new Book(titleField.getText(), authorField.getText(), isbnField.getText(), Integer.parseInt(yearField.getText()));
                }
                return null;
            });

            dialog.showAndWait().ifPresent(newBook -> {
                bookManager.updateBook(oldBook, newBook);
                booksObservableList.set(booksObservableList.indexOf(oldBook), newBook);
            });
        }
    }
}
