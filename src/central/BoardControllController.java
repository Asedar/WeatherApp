package central;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import board.IBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class BoardControllController implements Initializable{
	IBoard board;

    @FXML
    private TextField intervalRate;

    @FXML
    private Button changeInterval;

    @FXML
    private Button toggleStatus;
    
    public void getBoard(IBoard board)
    {
    	this.board = board;   	
    }

    @FXML
    void changeIntervalHandler(ActionEvent event) throws NumberFormatException, RemoteException {
    	if(!intervalRate.getText().isEmpty())
    	{
    		board.setUpdateInterval(Integer.parseInt(intervalRate.getText()));
    	}
    	
    }
    
    @FXML
    void toggleStatusHandler(ActionEvent event) throws RemoteException {
    	board.toggle();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		
	}

}
