package board;

import java.io.Serializable;

public class BoardData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public float temperature;
	public float wind;
	public float precipation;
	public String status;
	public int interval;
}
