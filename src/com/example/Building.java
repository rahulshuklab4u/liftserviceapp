package com.example;

public class Building {
	public final int NUM_OF_FLOORS; // No of floors in the builiding
	public final int NUM_OF_ELEVATORS; // No of elevators 
	private Elevator[] lift;

	public Building(int numOfFloors, int numOfElevators, int capacityOfElevator,int serviceFloor, int travelTime) {
		NUM_OF_FLOORS = numOfFloors;
		NUM_OF_ELEVATORS = numOfElevators;
		lift = new Elevator[numOfElevators];
		for (int liftNum = 0; liftNum < numOfElevators; liftNum++) {
			lift[liftNum] = new Elevator("LIFT " + liftNum, NUM_OF_FLOORS, liftNum % numOfFloors, capacityOfElevator, this,
					serviceFloor, travelTime);
		}
	}
	
	/**
	 * This method starts all of the elevators in the building
	 */
	public void startElevators(){
		for(int liftNum =0;liftNum<NUM_OF_ELEVATORS;liftNum++){
			lift[liftNum].start();
		}
	}
	
	//Stop all of the elevators from running
	public void stopElevators(){
		for(int liftNum=0;liftNum<NUM_OF_ELEVATORS;liftNum++){
			lift[liftNum].stopElevator();
		}
	}
	
	//Notify all waiting that an elevator has arrived at current Floor
	public synchronized void tellAt(){
		notifyAll();
	}
	
	/**
	 * This method finds the appropriate lift to furnish the request based on:
	 * 1. Current floor location of requester
	 * 2. If the lift is going up/ down.
	 * @param currentWorkerFloor current floor of the requestor
	 * @param goingUp : decides the direction
	 * @return
	 */
	public synchronized Elevator requestElevator(int currentWorkerFloor, boolean goingUp) {
		while (true) {
			for (int liftNum = 0; liftNum < NUM_OF_ELEVATORS; liftNum++) {
				if (lift[liftNum].getCurrentFloor() == currentWorkerFloor && lift[liftNum].isDirectionUp() == goingUp) {
					return lift[liftNum];
				}
			}
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void waitForElevatorToCome(){
		try{ 
			wait();
		}catch(InterruptedException e){
			
		}
	}
	
	
//class close	
}
