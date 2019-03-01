package central;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


import board.BoardData;
import board.IBoard;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Central implements ICentrala, Initializable {	

    @FXML
    private TableView<TableInfo> boardView;
    @FXML
    private TableColumn<TableInfo, String> name;
    @FXML
    private TableColumn<TableInfo, String> stat;
    @FXML
    private TableColumn<TableInfo, Float> temp;
    @FXML
    private TableColumn<TableInfo, Float> wind;
    @FXML
    private TableColumn<TableInfo, Float> p;
		
	ArrayList<IBoard> boards = new ArrayList<IBoard>();
	ObservableList<TableInfo> tbi = FXCollections.observableArrayList();
	
	Thread updateData = null;
	Boolean run = true;
	int wait = 5;
	
	@FXML
	void boardClickedHandler(MouseEvent event) throws IOException 
	{
		 if(boardView.getSelectionModel().getSelectedIndex() >= 0)
	    	{
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/central/BoardControll.fxml"));
	    		Stage addAssignedScreen = new Stage();
	        	Parent root = (Parent) loader.load();
	        	BoardControllController ctrl = loader.getController();
	        
	        	ctrl.getBoard(boards.get(boardView.getSelectionModel().getSelectedIndex()));
	    		
	    		Scene scene = new Scene(root);
	    		addAssignedScreen.setScene(scene);
	    		addAssignedScreen.setTitle("Modyfikuj");
	    		addAssignedScreen.setResizable(false);
	    		addAssignedScreen.initModality(Modality.APPLICATION_MODAL);
	    		addAssignedScreen.show();    		
	    	}
    }
	
	public Central() throws RemoteException 
	{
		super();		
	}

	@Override
	public int register(IBoard s) throws RemoteException 
	{
		boards.add(s);
		s.setUpdateInterval(2000);
		tbi.add(new TableInfo(s.getBoardData().temperature, s.getBoardData().wind, s.getBoardData().precipation, s.getBoardData().status, "Board" + Integer.toString(boards.size())));
		return boards.size();		
	}

	@Override
	public int unregister(int id) throws RemoteException 
	{
		boards.remove(id);
		return id;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try 
		{
			UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("Central", this);
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
		
		name.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("name"));
		stat.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("status"));
		temp.setCellValueFactory(new PropertyValueFactory<TableInfo, Float>("tmp"));
		wind.setCellValueFactory(new PropertyValueFactory<TableInfo, Float>("wind"));
		p.setCellValueFactory(new PropertyValueFactory<TableInfo, Float>("p"));
		boardView.setItems(tbi);
		
		updateData = new Thread(() ->
		{
			while(run)
			{
				if(boards.size() > 0)
				{
					for(int x = 0; x < boards.size(); x++)
					{
						try 
						{
							tbi.get(x).setStatus(boards.get(x).getBoardData().status);
							tbi.get(x).setTmp(boards.get(x).getBoardData().temperature);
							tbi.get(x).setWind(boards.get(x).getBoardData().wind);
							tbi.get(x).setP(boards.get(x).getBoardData().precipation);
						} 
						catch (RemoteException e) 
						{
							e.printStackTrace();
						}
					}
				}
				try 
				{
					TimeUnit.SECONDS.sleep(wait);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		});
		updateData.start();
	}

	public class TableInfo
	{
		FloatProperty tmp = new SimpleFloatProperty();
		FloatProperty wind = new SimpleFloatProperty();
		FloatProperty p = new SimpleFloatProperty();
		StringProperty status = new SimpleStringProperty();
		StringProperty name = new SimpleStringProperty();
		
		TableInfo(float tmp, float wind, float p, String status, String name)
		{
			this.tmp.set(tmp);
			this.wind.set(wind);
			this.p.set(p);
			this.status.set(status);
			this.name.set(name);
		}
		
		public FloatProperty tmpProperty() { return tmp; }
		public FloatProperty windProperty() { return wind; }
		public FloatProperty pProperty() { return p; }
		public StringProperty statusProperty() { return status; }
		public StringProperty nameProperty() { return name; }
		
		public void setTmp(float tmp) { this.tmp.set(tmp); }
		public void setWind(float wind) { this.wind.set(wind); }
		public void setP(float p) { this.p.set(p); }
		public void setStatus(String status) { this.status.set(status); }
		public void setName(String name) { this.name.set(name); }
	}
}
