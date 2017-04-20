# Linux-Process-Scheduler using nice concept

#  Aim:
  To design an algorithm to implement Linux Priority Based Process Scheduling.
# Problem Statement:
  The aim is to develop an algorithm which shall demonstrate the working of Linux Process Scheduling. This algorithm is based on the “NICE” Concept. The Linux Scheduler is a preemptive, priority based algorithm with two separate priority ranges: a real-time range from 0 to 99 and a nice value ranging from 100 to 140. These two ranges map into a global priority scheme whereby numerically lower values indicate higher priorities.
# Requirements:	
      Operating System – Ubuntu 16.04
			Language – Java.
# Theory:
  The Linux Scheduler is a preemptive, priority based algorithm with two separate priority ranges: a real-time range from 0 to 99 and a nice value ranging from 100 to 140. These two ranges map into a global priority scheme whereby numerically lower values indicate higher priorities. Unlike schedulers for many other systems, the Linux Scheduler assigns higher-priority tasks longer time quanta and lower-priority tasks shorter time quanta. Because of the unique nature of the scheduler, this is appropriate for Linux. 
  
            The relationship between priorities and time-slice length.
            for picture(http://image.slidesharecdn.com/lecture16cpuscheduling-140716044212-phpapp02/95/cpu-scheduling-in-os-15-638.jpg?cb=1405485785)
            
A runnable task is considered eligible for execution on the CPU so long as it has time remaining in its time slice. When a task has exhausted its time slice it is considered expired and is not eligible for execution again until all other tasks have also exhausted their time quanta. The kernel maintains a list of all runnable tasks in a runqueue data structure. Because of its support for SMP, each process maintains its own runqueue and schedules itself independently. Each runqueue contains two priority arrays- active and expired. The active array contains all tasks with time remaining in their time slices, and the expired array contains all expired tasks. Each of these priority arrays includes a list of tasks indexed according to priority (see below link for picture).  
 
      List of tasks indexed according to priority.
      for picture(http://image.slidesharecdn.com/lecture16cpuscheduling-140716044212-phpapp02/95/cpu-scheduling-in-os-17-638.jpg?cb=1405485785)
      
The scheduler chooses the task with the highest priority from the active array for execution on the CPU. On multiprocessor machines, this means that each processor is scheduling the highest priority task from its own runqueue structure. When all tasks have exhausted their time slices (that is, the active array is empty), the two priority arrays are exchanged as the expired array becomes the active array and vice-versa.
Tasks are assigned dynamic priorities that are based on the nice value plus or minus a value up to the value 5. Whether a value is added to or subtracted from a task’s nice value depends on the interactivity of the task. A task’s interactivity is determined by how long it has been sleeping while waiting for I/O. Tasks that are more interactive typically have longer sleep times and therefore are more likely to have adjustments closer to -5, as the scheduler favors such interactive tasks. Conversely, tasks with shorter sleep times are often CPU-bound and thus will have their priorities lowered.
A task’s dynamic priority is recalculated when the task has exhausted its time quantum and is to be moved to the expired array. Thus, when the two arrays are exchanged, all tasks in the new active array have been assigned new priorities and corresponding time slices.
# Analysis:
  The designed algorithms is a cumulative approach of Round Robin and Priority Scheduling Algorithms with inclusion of “NICE” Concept. The highest priority process will hold the least value in the negative scale and maximum time quantum. After execution of every process, the nice value is updated and hence the priority values. Any process, which is in the waiting state from a long time, its nice value is updated to give it a priority for execution, hence giving an opportunity for execution.
