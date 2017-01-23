package com.example;

/* Use Case: Office building with 3 floors and 2 servicing lifts.
 * FloorVisitPattern stores the floors that each person would go to during the day using the lifts.
 * Each person spends some time ( FLOOR_WORK_TIME)  before taking the lift to next floor.
 * 
 * Elevators are started and once a person joins, the elevator will service as per the floor visit pattern.
 * Once all the floors are visited for all existing persons using the lift, the elevators stop.
 * 
 * Assumptions:
 * 1. All person have same FLOOR_WORK_TIME ( can be varied)
 * 2. A lift will service a person only if it is going in the same direction as the person has to go to.
 * 3. Ideal design would be to include an ElevatorManager class to manage all operations on the lift. Here the elevator
 * class is doing all the responsibility.
 * 4.More functionalities can be added to the elevator : MOVING,STOPPED,DOOR_OPENED,ANNOUCEMENT,ABRUPT_PAUSE 
 * 5. All sysout statements should be replaced by Logger log statements.
 */

public class TestElevator {

	private static final int ELEVATOR_CAPACITY = 2;
	private static final int NUM_FLOORS = 3;
	private static final int TRAVEL_TIME = 1000;
	private static final int FLOOR_TIME = 500;
	private static final int FLOOR_WORK_TIME = 3000; // Time taken by each
														// worker at a floor
	private static final int NUM_ELEVATORS = 2;

	public static void main(String args[]) throws InterruptedException {

		Building office = new Building(NUM_FLOORS, NUM_ELEVATORS, ELEVATOR_CAPACITY, FLOOR_TIME, TRAVEL_TIME);
		// Floor numbers that each person has to go during the way.
		int[] adamFloorVisitPattern = { 1, 2, 0 };
		int[] eveFloorVisitPattern = { 2, 1, 0 };

		Person adam = new Person("Adam", adamFloorVisitPattern, FLOOR_WORK_TIME, 0, office);
		Person eve = new Person("Eve", eveFloorVisitPattern, FLOOR_WORK_TIME, 0, office);

		office.startElevators();
		adam.start();
		eve.start();

		adam.join();
		eve.join();
		office.stopElevators();
	}

}
