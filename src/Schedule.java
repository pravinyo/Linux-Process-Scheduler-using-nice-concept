import java.util.Random;

import utilities.PriorityManager;
import utilities.QuantumManager;

/**
 * @author Pravin Tripathi
 *
 */
class schedule
{
	private int countOfProcess;
	private static final int processType=2;
	private static final int niceValue=3;
	private static final int remainingTime=4;
	private static final int SubArrayIndex=0;
	private static final int SubArrayPriority=1;
	
	private int weightTimeData[],turnAroundTimeData[];
	
	
	public schedule(int processData[][])
	{
		this.countOfProcess=processData.length;
		this.weightTimeData=computeRoundRobinAlgorithm(processData);
		displayData(processData);
	}
	
	
	/*
	 * computeRoundRobin function will calculate wait time for each process
	 * input:
	 * 		list of burst time for each process
	 * 		time quantum for each process
	 * Output:
	 * 		wait time for each process 
	 */
	private int[] computeRoundRobinAlgorithm(int processData[][])
	{
		int i,j,k;
		int quantumTime,remainingTimeForCompletion;
		int weightTimeData[]=new int[this.countOfProcess];
		int temporaryDataStore[][]=new int[this.countOfProcess][5];
		
		/**
		* We assume initially all process are active process.
		* structure of active array => process ID,priority of the process;
		*/
		
		// holds data of active process
		int activeProcessData[][]=new int[this.countOfProcess][2];
	
		// holds data of expired process or process whose time is out
		int timeOutProcessData[][]=new int[this.countOfProcess][2];
		
		for(i=0;i<this.countOfProcess;i++) {
			
			//store the process data in temporary data store
			temporaryDataStore[i]=processData[i];
			
			//set weight time of initially to zero
			weightTimeData[i]=0;
		}
			
		
		// create instance of Quantum Manager,Priority Manager and random Number generator
		QuantumManager quantumManager = new QuantumManager();
		Random rand = new Random();
		PriorityManager priorityManager = new PriorityManager();
		
			int activeProcessIndex=0,expireProcessIndex=0;
			for(i=0;i<this.countOfProcess;i++)
			{
				//set the nice value
				quantumManager.setNice(temporaryDataStore[i][niceValue]);
				
				//get new computed quantum value (that much time CPU has for process)
				quantumTime=(int)quantumManager.getNewQuantumValue();
				
				//check whether remainingTime > quantumTime
				if(temporaryDataStore[i][remainingTime]>quantumTime)
				{
					//update the remaningTime
					temporaryDataStore[i][remainingTime]-=quantumTime;
					
					for(j=0;j<this.countOfProcess;j++)
					  if((j!=i)&&(temporaryDataStore[j][remainingTime]!=0))
						  weightTimeData[j]+=quantumTime;
				}
				else
				{
					for(j=0;j<this.countOfProcess;j++)
						if((j!=i)&&(temporaryDataStore[j][remainingTime]!=0))
							weightTimeData[j]+=temporaryDataStore[i][remainingTime];
					
					//burst time equals zero or process completed it's process
					temporaryDataStore[i][remainingTime]=0; 
				}
				
				
				/**
				*	Code for separation of process according to their status
				*	Once one of the queue is completed we will swap the array
				*/
				
				//check remaining time is none zero
				if(temporaryDataStore[i][remainingTime]!=0) 
				{
					priorityManager.setOldNiceValue(temporaryDataStore[i][niceValue]);
					if(rand.nextInt(10)<5)
					{
						/**
						* Assuming this process is non IO process 
						* so we can safely put it back in Active queue
						*/
						activeProcessData[activeProcessIndex++][SubArrayIndex]=i;// i index where process is located
						temporaryDataStore[i][niceValue]=priorityManager.getNewNiceValue(temporaryDataStore[i][processType]);
						
						// new computed priority
						activeProcessData[activeProcessIndex-1][SubArrayPriority]=temporaryDataStore[i][processType]+temporaryDataStore[i][niceValue];
					}
					else
					{
						
						/**
						* We assume that random value if greater than 5 comes that is a condition for 
						* process requesting some IO operation so we need to put it in expire queue
						*/
						timeOutProcessData[expireProcessIndex++][SubArrayIndex]=i;
						
						temporaryDataStore[i][niceValue]=priorityManager.getNewNiceValue(temporaryDataStore[i][processType]);
						// new computed priority
						timeOutProcessData[expireProcessIndex-1][SubArrayPriority]=temporaryDataStore[i][niceValue]+temporaryDataStore[i][processType];
					}
				}
			}
			
			remainingTimeForCompletion=0;
			for(k=0;k<this.countOfProcess;k++)
				// add burst time
				remainingTimeForCompletion+=temporaryDataStore[k][remainingTime];
			
			//selection of array
			int[][] sendForExecution=new int[activeProcessData.length][2];
			
		// selecting active queue
		int sendForExecutionIndex;
		
		/**
		 * After executing one cycle now check for any incomplete process.
		 * If found then that process will be present either in active array or expire array .
		 * we will select active array as it has all the process which are in runnable state.
		 * before that we will sort the process according to their priority high priority process will be select first as
		 * it will be most important process compared to rest in the active queue.
		 */
		
		while(remainingTimeForCompletion!=0)
		{
			// sort the active process array in descending order of their priority
			sort(activeProcessData,activeProcessData.length,1);
			
			//copy the active process array in to sendForExeution array as it will forward the data of process for next execution
			System.arraycopy(activeProcessData,0,sendForExecution,0,activeProcessData.length);
			
			//reset all the indexes 
			activeProcessIndex=expireProcessIndex=0;
			sendForExecutionIndex=0;
			
			
			if(sendForExecution.length>0 && sendForExecutionIndex<=sendForExecution.length)
			{
				for(i=0;i<this.countOfProcess;i++)
				{
				
					quantumManager.setNice(temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][niceValue]);
					// compute the new quantum size
					quantumTime=(int)quantumManager.getNewQuantumValue();
					
					//check remainingTime > quantumTime
					if(temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][remainingTime]>quantumTime)
					{
						//execute and update remaining time
						temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][remainingTime]-=quantumTime;
						for(j=0;j<this.countOfProcess;j++)
						  if((j!=sendForExecution[sendForExecutionIndex][SubArrayIndex])&&(temporaryDataStore[j][remainingTime]!=0))
							  weightTimeData[j]+=quantumTime;
					}
					else
					{
						for(j=0;j<this.countOfProcess;j++)
							if((j!=sendForExecution[sendForExecutionIndex][SubArrayIndex])&&(temporaryDataStore[j][remainingTime]!=0))
								weightTimeData[j]+=temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][remainingTime];
						
						 //remaining time equals zero or process completed it's process
						temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][remainingTime]=0;
					}
					/**
					*	Code for separation of process according to their status
					*	Once one of the queue is completed we will swap the array
					*/
					if(temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][remainingTime]!=0) //check remaining time is none zero
					{
						priorityManager.setOldNiceValue(temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][niceValue]);
						
						if(rand.nextInt(10)<5)
						{
							/**
							* Assuming this process is non IO process 
							* so we can safely put is back in Active queue
							*/
							activeProcessData[activeProcessIndex++][SubArrayIndex]=sendForExecution[sendForExecutionIndex][SubArrayIndex];// i index where process is located
							temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][niceValue]=priorityManager.getNewNiceValue(temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][processType]);
							activeProcessData[activeProcessIndex-1][SubArrayPriority]=temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][niceValue]+temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][processType];
							// new computed priority
						}
						else
						{
							/**
							* We assume that random value if greater than 5 comes that is a condition for 
							* process requesting some IO operation so we need to put it in expire queue
							*/
							timeOutProcessData[expireProcessIndex++][SubArrayIndex]=sendForExecution[sendForExecutionIndex][SubArrayIndex];
							temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][niceValue]=priorityManager.getNewNiceValue(temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][processType]);
							timeOutProcessData[expireProcessIndex-1][SubArrayPriority]=temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][niceValue]+temporaryDataStore[sendForExecution[sendForExecutionIndex][SubArrayIndex]][processType];
							// new computed priority
						}
					}
				}
				sendForExecutionIndex++;
			}
			else
			{
				// swap array between active and expire
				System.arraycopy(timeOutProcessData,0,activeProcessData,0,timeOutProcessData.length);
			}
			
			remainingTimeForCompletion=0;

			for(k=0;k<this.countOfProcess;k++) 
				//add remaining time
				remainingTimeForCompletion+=temporaryDataStore[k][remainingTime];

			//selection of array
		}
		return weightTimeData;
	}
	/**
	*	sort function make the ordering according to their priority
	*	lower priority process will come first to execute
	*/
	
	private static void sort(int[][] a, int n, int c) 
	{
	    int key, j;
	    for (int i = 1; i < n; i++){
		key = a[i][c];
		int[] keyRow = a[i];
		j = i - 1;
		while ((j >= 0) && (a[j][c] < key)){
		    a[j+1] = a[j];
		    j = j - 1;
		}
		a[j+1] = keyRow;
	    }
	}

	/*
	 * displayData() function will calculate turn around time and display the average waiting time
	 * Input:
	 * 		list of burst time
	 * Output:
	 * 		none;
	 */
	void displayData(int processData[][])
	{
		int i,j;
		float sum_weightTime=0,sum_turnAroundTime=0;
		this.turnAroundTimeData=new int[processData.length]; 
		for(i=0;i<this.countOfProcess;i++)
			this.turnAroundTimeData[i]=this.weightTimeData[i]+processData[i][1];
		
		System.out.println("\nProcessID\t\tBT\tWT\tTAT\n");
		
		for(j=0;j<this.countOfProcess;j++)
		{
			System.out.println("ID:"+processData[j][0]+"\t\t\t"+processData[j][1]+"\t"+this.weightTimeData[j]+"\t"+this.turnAroundTimeData[j]);
			sum_weightTime+=this.weightTimeData[j];
			sum_turnAroundTime+=this.turnAroundTimeData[j];
		}
		System.out.println("\nAverage wating time "+(sum_weightTime/this.countOfProcess)+"\nAverage turn around time "+(sum_turnAroundTime/this.countOfProcess));
	}
}