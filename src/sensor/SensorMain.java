package sensor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SensorMain extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sensor/SensorGUI.fxml"));
    	Parent root = (Parent) loader.load();
		Sensor c = loader.getController();
		Scene scene = new Scene(root);
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
