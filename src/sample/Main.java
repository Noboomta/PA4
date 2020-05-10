package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    HBox hBox3;
    ProgressBar bar;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = initComponent();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Pane initComponent(){
        FlowPane myPane = new FlowPane();
        myPane.setVgap(10);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        HBox hBox1 = new HBox();
        hBox1.setSpacing(10);
        HBox hBox2 = new HBox();
        hBox2.setSpacing(10);
        hBox3 = new HBox();
        hBox3.setSpacing(10);
        Button downloadButton = new Button("Download");
        Button clearButton = new Button("Clear");
        Label allProgressLabel = new Label("All Progress");
        Label ThreadProgressLabel = new Label("Thread Progress");
        bar = new ProgressBar();
        TextField urlField = new TextField();
        hBox1.getChildren().addAll(urlField, downloadButton, clearButton);
        hBox2.getChildren().addAll(allProgressLabel, bar);
        hBox3.getChildren().addAll(ThreadProgressLabel);
        List<ProgressBar> barList = new ArrayList<>();
        AllTask allTask = new AllTask();


        downloadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                File saveFile = fileChooser.showSaveDialog(new Stage());
                if(saveFile!=null){
                    long length = 0;
                    long lenEach = 0;
                    long start = 0;
                    try {
                        length = fileSize(new URL(urlField.getText()));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if(length!=0){
                        lenEach = (length/5)+10;
                    }
                    for(int i=0;i<5;i++){
                        Task task = null;
                        if(i==4){
                            start-=lenEach;
                            lenEach = length-start;
                        }
                        try {
                            task = new DownloadTask(new URL(urlField.getText()), saveFile, start, lenEach);
                            start+=lenEach;
                            allTask.addTask(task);
                        } catch (MalformedURLException e) {
                            System.out.println("errrrrr in loop main");
                        }
                        task.progressProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                                bar.setProgress(allTask.getAllProgress());

                            }
                        });
                        ProgressBar eachBar = new ProgressBar();
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

    public long fileSize(URL url){
        long length = 0;
        try {
            URLConnection connection = url.openConnection( );
            length = connection.getContentLengthLong( );
        } catch (MalformedURLException ex) {
            // URL constructor may throw this
        } catch (IOException ioe) {
            // getContentLengthLong may throw IOException
        }
        return length;
    }


}
