package board;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sensor.ISensor;

public interface IBoard extends Remote{
	Boolean register(ISensor s, char category) throws RemoteException; 
    Boolean unregister(ISensor s) throws RemoteException;
    void toggle() throws RemoteException; // prze��cznik w��cz/wy��cz zbierania i wy�wietlania danych na tablicy
    void setUpdateInterval(long milisec) throws RemoteException;
    BoardData getBoardData() throws RemoteException;

}
