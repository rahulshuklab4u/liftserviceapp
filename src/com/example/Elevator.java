package com.example;

import java.util.Vector;

public class Elevator extends Thread{
	
	private final String name;
	private final int capacity; // No of passengers that can board the elevator together
	private int currentFloor;
	private boolean isDirectionUp = true;
	private Building thisBuilding;
	private long floorPauseTime; // Service time of the lift at each floor
	private long timeBetweenFloors; // Time taken to travel between floors
	private int passengerCount;  //  No of passengers currently in the elevator.
	private Vector[] passengers; // List of passengers in the lift.
	private int numberFloors;
	private boolean running = true;

	public Elevator(String name, int numberFloors, int startingFloor, int capacity, Building office,
			long floorPauseTime, long timeBetweenFloors) {
		this.name = name;
		this.numberFloors = numberFloors;
		this.currentFloor = startingFloor;
		this.thisBuilding = office;
		this.floorPauseTime = floorPauseTime;
		this.timeBetweenFloors = timeBetweenFloors;
		this.capacity = capacity;
	}

	public void stopElevator() {
		this.running = false;
	}

	public synchronized int getCurrentFloor() {
		return this.currentFloor;
	}
	
	public void run() {
		System.out.println(toString() + " starting from floor "+currentFloor);
		while (running) {
			System.out.println(toString() + " now on floor " + currentFloor);

			// Change elevator direction if it reaches boundary floors
			if (currentFloor == numberFloors - 1) {
				isDirectionUp = false;
			} else if (currentFloor == 0) {
				isDirectionUp = true;
			}

			// Notify all passengers to exit or enter the lift.
			notifyPassengers();
			thisBuilding.tellAt();
			try {
				sleep(floorPauseTime);
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}

			// Move lift to the next floor
			if (isDirectionUp) {
				currentFloor++;
			} else {
				currentFloor--;
			}
			try {
				// Pause for time taken to traverse between floors
				sleep(timeBetweenFloors);
			} catch (InterruptedException e) {

			}
		}

	}
	
	// If elevator is on currentFloor and there is room for new passenger
	//then return destFloor, else return currentFloor
	/**
	 * If the lift is on the current floor and the capacity is not full, requestor can board the lift.
	 * @param destFloor Floor to go to
	 * @param currFloor Current floor of request
	 * @param waiter Person requesting the lift
	 * @return
	 */
	public synchronized int boardElevator( int destFloor, int currFloor, Person waiter){
		
		if(currentFloor == currFloor && passengerCount < capacity ){
			passengerCount++;
			System.out.println(waiter + " successfully boarding on "+toString()+ " on floor "+currFloor);
			
			while(currentFloor != destFloor){
				try{
					wait();
				}catch (InterruptedException e){
					
				}
			}
			passengerCount--;
			return destFloor;
		}else{
			return currFloor;
		}
	}
	
	// Notify all passengers
	private synchronized void notifyPassengers(){
		notifyAll();
	}
	
	public String toString(){
		return name;
	}
	public boolean isDirectionUp(){
		return isDirectionUp;
	}
	//class close
}
