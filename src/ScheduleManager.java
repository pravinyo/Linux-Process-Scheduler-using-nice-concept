import java.util.Random;
import java.util.Scanner;

/**
 * @author Pravin Tripathi
 *
 */
class scheduleManager
{
	public static void main(String args[])
	{
		Scanner scan=new Scanner(System.in);
		int i,n;
		
		//take count of process as input
		System.out.println("Enter number of process:");
		n=scan.nextInt();
		
		// ensuring n is not greater than 140
		n=n<140?n:140;
		
		
		/**
		 * 2D array processData holds following data associated with the process
		 * 	-Id of the Process(Unique for each process)
		 * 	-burst Time , It is the amount of time process need from CPU
		 * 	-Type of Process, It Indicates process type like critical or normal process,etc.
		 * 		we had assumed two types of process:
		 * 			1=>	Critical or System Process
		 * 			2=> Normal Process
		 * 	-Nice Value, It shows niceness of the process
		 * 	-Remaining Time, It is the time process need for completing it's intended task
		 */
		 int processData[][]=new int[n][5];
		 Random random = new Random();
		 
		 
		 // get individual process data
		 System.out.println("Process Type\n1 = Critical Process\n2 = Normal Process");
		for(i=0;i<processData.length;i++)
		{
			System.out.println("Enter brust Time,Process Type for \t"+(i+1)+"\n");
			
			//processId
			processData[i][0]=i+1;
			
			//burstTime
			processData[i][1]=scan.nextInt();
			
			//processType
			processData[i][2]=scan.nextInt();
			
			//niceValueSettings
			processData[i][3]=(processData[i][2]==1)?(random.nextInt(40)-19):(random.nextInt(20)+20);
			
			//remainingTime
			processData[i][4]=processData[i][1];
		}
		
		// scanner is no longer need 
		scan.close();
		
		//start the scheduling activity
		new schedule(processData);
	}
}
