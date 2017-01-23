package com.example;

public class Person extends Thread{
	private static final int WAITING=0;
	private static final int IS_WORKING=1;
	private static final int  ON_ELEVATOR=2;
	private static final int  DONE=3;
	private int status = WAITING;
	private final int[] floorsToVisit;
	private final String name;
	private final int durationOfWork;
	private final Building building;
	private int floorVisitCounter;
	private int currentFloor;
	
	public Person(String name, int[] floorsToVisit, int durationOfWork, int startingFloor, Building building) {
		super("Person " + name);
		this.name = name;
		this.floorsToVisit = floorsToVisit;
		this.durationOfWork = durationOfWork;
		this.floorVisitCounter = 0;
		this.currentFloor = startingFloor;
		this.building = building;

		// Verify if all floor numbers are valid.
		for (int i = 0; i < floorsToVisit.length; i++)
			validateFloorNumber(floorsToVisit[i], building);
		// validateFloorNumber(currentFloor,building);
		if (durationOfWork < 0)
			durationOfWork = 0;
	}

	// if floor not valid, throw exception
	private void validateFloorNumber(int floor, Building office) {
		if (floor < 0 || floor >= office.NUM_OF_FLOORS)
			throw new RuntimeException("Invalid floor number " + floor);
	}
	
	
	public void run(){
		while(floorVisitCounter < floorsToVisit.length){
			int dest = floorsToVisit[floorVisitCounter];
			
			// If on-board, check if the current floor is also the destination floor.
			// If so, the worker exits the lift & continues work at the floor.
			if(dest ==currentFloor && status == ON_ELEVATOR){
				System.out.println(name+" exiting elevator on floor "+dest);
				workATFloor();
				System.out.println(name+" finished work on floor "+dest);
				floorVisitCounter++;
			}
			
			//This block is used to request an elevator and board it. 
			else{
				System.out.println(name + " waiting on floor "+currentFloor+" to go to floor "+dest);
				
				//Request for an elevator and wait for the correct one to arrive.
				Elevator arrivedElevator = building.requestElevator(currentFloor, dest>currentFloor);
				System.out.println(name + " tries to get on "+arrivedElevator + " (checking lift capacity) to go to floor "+dest);
				status=ON_ELEVATOR;
				//Check capacity of arrived elevator
				currentFloor = arrivedElevator.boardElevator(dest, currentFloor, this);
				
				if(currentFloor != dest){ 
					status = WAITING;
					System.out.println("Capacity Full ! "+name+ " couldn't board elevator for floor "+dest+". Retry.");
					building.waitForElevatorToCome();
				}
			}
		}
		System.out.println(name + " finished work at Office.");
	}
	
	/**
	 * Once the worker has arrived on destination floor, this method sleeps the thread for the duration of the work
	 */
	private void workATFloor(){
		status=IS_WORKING;
		try{
			Thread.sleep(durationOfWork);
		}catch(InterruptedException e){
			
		}
		status=WAITING;
	}
	
	public String toString(){
		return name;
	}
}

