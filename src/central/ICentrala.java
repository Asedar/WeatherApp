package central;

import java.rmi.Remote;
import java.rmi.RemoteException;

import board.IBoard;

public interface ICentrala extends Remote{
	int register(IBoard s) throws RemoteException; // zwraca numer przypisany do zarejestrowanej tablicy lub 0, jeœli operacja siê nie uda³a
    int unregister(int id) throws RemoteException; // zwraca numer wyrejestrowanej tablicy lub 0, jeœli operacja siê nie uda³a

}
