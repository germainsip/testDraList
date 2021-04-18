package org.example;

import com.jfoenix.controls.JFXTextArea;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import org.example.Candidat;
import org.example.ListOrganizerGe;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class MalisteController implements Initializable {
    public TableView<Candidat> candidatTable;
    public ObservableList<Candidat> list = FXCollections.observableArrayList();
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    public Button affiche;
    public JFXTextArea afficheZone;
    public TableColumn nomCol;
    public TableColumn prenomCol;
    public TextField nomField;
    public TextField prenomField;
    public Button addBtn;
    public Label nom;
    public Label prenom;
    public Button rem;
    public TableColumn suppCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //candidatTable.getColumns().add(createCol("Prénom", Candidat::firstNameProperty, 150));
        //candidatTable.getColumns().add(createCol("Nom", Candidat::lastNameProperty, 150));
        nomCol.setCellValueFactory(new PropertyValueFactory<Candidat, String>("lastName"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Candidat, String>("firstName"));
        list.add(new Candidat("Jacob", "Smith"));
        list.add( new Candidat("Isabella", "Johnson"));
        list.add( new Candidat("Ethan", "Williams"));
        list.add( new Candidat("Michael", "Brown"));

        /*candidatTable.getItems().addAll(
                new Candidat("Jacob", "Smith"),
                new Candidat("Isabella", "Johnson"),
                new Candidat("Ethan", "Williams"),
                new Candidat("Emma", "Jones"),
                new Candidat("Michael", "Brown")
        );
        */

        suppCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<Candidat, String>, TableCell<Candidat, String>> cellFactory =
                new Callback<TableColumn<Candidat, String>, TableCell<Candidat, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Candidat, String> param)
                    {
                        final TableCell<Candidat, String> cell = new TableCell<Candidat, String>()
                        {

                            final Button btn = new Button("X");


                            {
                                btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                                btn.setOnAction(event -> {
                                    candidatTable.getItems().remove(getIndex());
                                });
                            }

                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        suppCol.setCellFactory(cellFactory);

        candidatTable.setItems(list);

        candidatTable.setRowFactory(tv -> {
            TableRow<Candidat> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Candidat draggedCandidat = candidatTable.getItems().remove(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = candidatTable.getItems().size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    candidatTable.getItems().add(dropIndex, draggedCandidat);

                    event.setDropCompleted(true);
                    candidatTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }

            });

            return row;
        });
    }

    private TableColumn<Candidat, String> createCol(String title,
                                                    Function<Candidat, ObservableValue<String>> mapper, double size) {

        TableColumn<Candidat, String> col = new TableColumn<>(title);
        col.setCellValueFactory(cellData -> mapper.apply(cellData.getValue()));
        col.setPrefWidth(size);

        return col;
    }

    public void loadlist(ActionEvent actionEvent) {
        afficheZone.clear();
        System.out.println(
                candidatTable.getItems());
        for (Candidat cand : candidatTable.getItems()) {
            afficheZone.appendText(cand.getFirstName() + "\n");
        }
    }

    public void addToList(ActionEvent actionEvent) {
        Candidat candidat = new Candidat(prenomField.getText(),nomField.getText());
        list.add(candidat);
    }

    public void remFromList(ActionEvent actionEvent) {

    }
}
