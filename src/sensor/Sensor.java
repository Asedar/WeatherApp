package sensor;

import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import board.IBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class Sensor implements ISensor, Initializable{
	
	private int intervalTime = 2;
	SensorReadings r = new SensorReadings();
	Thread generateReadings = null;
	Boolean run = true;
	Registry registry;
	IBoard board;
	float reading;
	
	@FXML
    private ChoiceBox<String> chooseBoard;
    @FXML
    private Button register;
    @FXML
    private ChoiceBox<String> chooseType;
    @FXML
    private Label status;
    @FXML
    private TextField interval;
    @FXML
    private Button changeInterval;
    @FXML
    private Button findBoards;
    
    @FXML
    void findBoardsHandler(ActionEvent event) throws AccessException, RemoteException 
    {
    	chooseBoard.getItems().clear();
    	chooseBoard.getItems().addAll(registry.list());
    	chooseBoard.getItems().remove(0);
    	chooseBoard.getSelectionModel().selectFirst();
    }
   
    @FXML
    void changeIntervalHandler(ActionEvent event) 
    {
    	if(!interval.getText().isEmpty())
    	{
    		intervalTime = Integer.parseInt(interval.getText());
    	}
    }	
    
    @FXML
    void registerHandler(ActionEvent event) throws AccessException, RemoteException, NotBoundException 
    {
    	switch(chooseType.getSelectionModel().getSelectedItem())
    	{
    	case "Wiatr":
    	{
    		r.category = 'w';
    		break;
    	}
    	case "Opady":
    	{
    		r.category = 'p';
    		break;
    	}
    	case "Temperatura":
    	{
    		r.category = 't';
    		break;
    	}
    	}
    	
    	board = (IBoard) registry.lookup(chooseBoard.getSelectionModel().getSelectedItem());
    	if(board.register(this, r.category))
    	{
    		status.setText("Zarejestrowany");
    		findBoards.setDisable(true);
    		register.setDisable(false);
    	}
    	else
    	{
    		status.setText("brak miejsc na czujniki");
    	}
    	
    	generateReadings = new Thread(()->
		{
			while(run)
			{
				Random rand = new Random();
				if(r.category == 't')
				{
					reading = rand.nextInt(120) - 60;
				}
				else if(r.category == 'w' || r.category == 'p')
				{
					reading = rand.nextInt(120);
				}
				
				r.value = reading;
				try 
				{
					TimeUnit.SECONDS.sleep(intervalTime);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		});
    	generateReadings.start();
    	register.setDisable(true);
    }

	@Override
	public SensorReadings getSensorReadings() 
	{
		return r;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{

		DecimalFormat format = new DecimalFormat( "#.0" );
		interval.setTextFormatter( new TextFormatter<>(c ->
		{
		    if ( c.getControlNewText().isEmpty() )
		    {
		        return c;
		    }

		    ParsePosition parsePosition = new ParsePosition( 0 );
		    Object object = format.parse( c.getControlNewText(), parsePosition );

		    if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
		    {
		        return null;
		    }
		    else
		    {
		        return c;
		    }
		}));
		
		chooseType.getItems().addAll("Wiatr", "Opady", "Temperatura");
		chooseType.getSelectionModel().selectFirst();
					
		try 
		{
			UnicastRemoteObject.exportObject(this, 0);
			registry = LocateRegistry.getRegistry();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
	}
	public void close()
	{
		run = false;
		generateReadings = null;
	}

}
