package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Class extends the Application.
 */
public class Main extends Application {
    HBox hBox3;
    ProgressBar bar;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = initComponent();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * initComponent method to return Pane.
     * @return Pane
     */
    public Pane initComponent() {
        FlowPane myPane = new FlowPane();
        myPane.setPadding(new Insets(20));
        myPane.setVgap(10);

        Label allProgressLabel = new Label("All Progress");
        Label ThreadProgressLabel = new Label("Thread Progress");
        Label urlToDownload = new Label("URL to Download");
        Label onBarNumber = new Label("");

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        HBox hBox1 = new HBox();
        hBox1.setSpacing(10);
        hBox1.setAlignment(Pos.CENTER);

        HBox hBox2 = new HBox();
        hBox2.setSpacing(10);
        hBox2.setAlignment(Pos.CENTER);

        hBox3 = new HBox();
        hBox3.setSpacing(10);
        hBox3.setAlignment(Pos.CENTER);

        Button downloadButton = new Button("Download");
        Button clearButton = new Button("Clear");

        bar = new ProgressBar();
        bar.setMinWidth(300);

        TextField urlField = new TextField();
        hBox1.getChildren().addAll(urlToDownload, urlField, downloadButton, clearButton);

        StackPane stackPane = new StackPane(bar, onBarNumber);
        hBox2.getChildren().addAll(allProgressLabel, stackPane);

        hBox3.getChildren().addAll(ThreadProgressLabel);
        List<ProgressBar> barList = new ArrayList<>();
        AllTask allTask = new AllTask();

        downloadButton.setOnAction(new EventHandler<ActionEvent>() {
            private long length;

            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                File saveFile = fileChooser.showSaveDialog(new Stage());
                if (saveFile != null) {
                    allProgressLabel.setText(saveFile.getName());
                    length = 0;
                    long lenEach = 0;
                    long start = 0;
                    try {
                        this.length = fileSize(new URL(urlField.getText()));
                    } catch (MalformedURLException e) {
                        popUP("No File");
                    }
                    if (length != 0) {
                        lenEach = (length / 5) + 10;
                    }
                    for (int i = 0; i < 5; i++) {
                        Task task = null;
                        if (i == 4) {
                            start -= lenEach;
                            lenEach = length - start;
                        }
                        try {
                            task = new DownloadTask(new URL(urlField.getText()), saveFile, start, lenEach);
                            start += lenEach;
                            allTask.addTask(task);
                        } catch (MalformedURLException e) {
                            popUP("url incorrect!");
                            break;
                        }
                        task.progressProperty().addListener(new ChangeListener<Number>() {
                            // class to set the progresses.
                            @Override
                            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                                bar.setProgress(allTask.getAllProgress());
                                onBarNumber.setText(String.format("%,d/%,d", (long) (allTask.getAllProgress() * length), length));
                            }
                        });
                        ProgressBar eachBar = new ProgressBar();
                        eachBar.setMaxWidth(50);
                        eachBar.progressProperty().bind(task.progressProperty());
                        barList.add(eachBar);
                        hBox3.getChildren().add(eachBar);
                        Thread thread = new Thread(task);
                        thread.start();
                    }
                }
            }

        });

        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            //handle class of clear button
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(barList);
                hBox3.getChildren().removeAll(barList);
                bar.setProgress(0);
            }
        });

        vBox.getChildren().addAll(hBox1, hBox2, hBox3);
        myPane.getChildren().addAll(vBox);

        return myPane;
    }

    /**
     * Method to get the File size.
     * @param url
     * @return long of length
     */
    public long fileSize(URL url) {
        long length = 0;
        try {
            URLConnection connection = url.openConnection();
            length = connection.getContentLengthLong();
        } catch (MalformedURLException ex) {
            popUP("url incorrect!");
        } catch (IOException ioe) {
            popUP("url incorrect!");
        }
        return length;
    }

    /**
     * Popup method to alert the error.
     * @param err String to popup.
     */
    public void popUP(String err) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert Dialog!!");
        alert.setHeaderText("Something Error");
        alert.setContentText(err);
        alert.showAndWait();
    }

}
