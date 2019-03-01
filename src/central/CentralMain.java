package central;

import board.Board;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CentralMain extends Application 
{
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/central/CentralGUI.fxml"));
    	Parent root = (Parent) loader.load();		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("Sensor");
		primaryStage.setResizable(false);
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
	
}
