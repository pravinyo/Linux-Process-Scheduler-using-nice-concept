/**
 * 
 */
package utilities;

/**
 * @author Pravin Tripathi
 *
 */
public class QuantumManager {
	int nice;
	double quanta;
	/**
	*	input  get the current nice value of the process
	*	output none
	*/
	public void  setNice(int nice)
	{
		this.nice=nice;
	}
	/**	compute the new value of time slice for each nice value
	*	Input none
	*	Output quantum value
	*/
	public double getNewQuantumValue()
	{
		this.quanta=1024/Math.pow(1.25f,this.nice);
		
		return this.quanta;
	}

}
