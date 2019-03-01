package board;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sensor.Sensor;

public class BoardMain extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/board/BoardGUI.fxml"));
    	Parent root = (Parent) loader.load();		
		Scene scene = new Scene(root);
		Board c = loader.getController();
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("Sensor");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
		    c.close();
		    Platform.exit();
		});
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}

}
