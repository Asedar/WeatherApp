package board;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import central.ICentrala;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sensor.ISensor;

public class Board implements IBoard, Initializable{
	private ArrayList<ISensor> sensors = new ArrayList<ISensor>();
	BoardData data = new BoardData();
	Boolean t = false;
	Boolean w = false;
	Boolean p = false;
	AtomicBoolean run = new AtomicBoolean();
	long interval = 4000;
	ICentrala central;
	Thread collectData = null;
	

    @FXML
    private Label name;
    @FXML
    private Label temperature;
    @FXML
    private Label wind;
    @FXML
    private Label precipation;
    @FXML
    private Label status;

	@Override
	public Boolean register(ISensor s, char category) throws RemoteException {
		switch(category)
		{
		case 't':
		{
			if(!t)
			{
				sensors.add(s);
				t = true;
				return true;
			}
			return false;
		}
		case 'w':
		{
			if(!w)
			{
				sensors.add(s);
				w = true;
				return true;
			}
			return false;
		}
		case 'p':
		{
			if(!p)
			{
				sensors.add(s);
				p = true;
				return true;
			}
			return false;
		}
		default:
			return false;
		}
		
	}

	@Override
	public Boolean unregister(ISensor s) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toggle() throws RemoteException {
		if(data.status.equals("ON"))
		{
			run.set(false);
			collectData = null;
			data.status = "OFF";
			
			Platform.runLater(() ->
			{
				status.setText("OFF");
			});
			return;
		}
		if(data.status.equals("OFF"))
		{			
			Platform.runLater(() ->
			{
				status.setText("ON");
			});			
			data.status = "ON";
			run.set(true);
			return;
		}
	}

	@Override
	public void setUpdateInterval(long milisec) throws RemoteException {
		interval = milisec;		
	}
	
	@Override
	public BoardData getBoardData() throws RemoteException {
		return data;		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		data.status = "ON";
		run.set(true);
		try 
		{
			UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry();
			central = (ICentrala) registry.lookup("Central");
			int x = central.register(this);
			
			registry.rebind("Board" + Integer.toString(x), this);
			name.setText("Tablica " + Integer.toString(x));
		} 
		catch (RemoteException | NotBoundException e) 
		{
			e.printStackTrace();
		}

		collectData = new Thread(()->
		{
			while(true)
			{
				if(run.get())
				{
					if(p || w || t)
					{
						for(int x = 0; x < sensors.size(); x++)
						{
							try 
							{
								switch(sensors.get(x).getSensorReadings().category)
								{
								case 't':
								{
									data.temperature = sensors.get(x).getSensorReadings().value;
									System.out.println(data.temperature);
									Platform.runLater(() ->
									{
										temperature.setText(Float.toString(data.temperature));
									});
									break;
								}
								case 'w':
								{
									data.wind = sensors.get(x).getSensorReadings().value;								
									Platform.runLater(() ->
									{
										wind.setText(Float.toString(data.wind));
									});
									break;
								}
								case 'p':
								{
									data.precipation = sensors.get(x).getSensorReadings().value;
									Platform.runLater(() ->
									{
										precipation.setText(Float.toString(data.precipation));
									});
									break;
								}
								}
							} 
							catch (RemoteException e) 
							{
								e.printStackTrace();
							}
						}
						
					}
					try 
					{
						Thread.sleep(interval);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
				else
				{
					try 
					{
						Thread.sleep(interval);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
			}			
		});
		collectData.start();
	}
	
	public void close()
	{
		run.set(false);
		collectData = null;
	}
}
