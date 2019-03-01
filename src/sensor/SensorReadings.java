package sensor;

import java.io.Serializable;

public class SensorReadings implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public char category; // 't' - temperature, 'w' - wind, 'p' - precipitation
	public float value; // wartoœæ, której jednostka zale¿y od kategorii	
	
	void setCategory(char category)
	{
		this.category = category;
	}
}
