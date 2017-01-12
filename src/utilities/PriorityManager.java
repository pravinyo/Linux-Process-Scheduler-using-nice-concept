/**
 * 
 */
package utilities;

/**
 * @author Pravin Tripathi
 *
 */
public class PriorityManager {
	private int oldNice;
	private int newNice;
	public void setOldNiceValue(int oldNice)
	{
		this.oldNice=oldNice;
	}
	public int getNewNiceValue(int processType)
	{
		if(processType==0)//critical process
		{
			if(this.oldNice>-20 && this.oldNice<0)
				this.newNice=--this.oldNice;
		}
		else if(processType==1)//real-time process
		{
			if(this.oldNice>-5 && this.oldNice<10)
				this.newNice=--this.oldNice;
			else
				this.newNice=this.oldNice+=2;
		}
		else //user process
		{
			if(this.oldNice>10 && this.oldNice<19)
				this.newNice=++this.oldNice;
			else
				this.newNice=--this.oldNice;
		}
		
		return this.newNice;
	}

}
