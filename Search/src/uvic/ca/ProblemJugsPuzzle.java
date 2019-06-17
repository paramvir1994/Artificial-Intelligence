package uvic.ca;
import java.util.HashSet;
import java.util.Set;

public class ProblemJugsPuzzle extends Problem {
    private static final int jug12Gallon = 0;
    private static final int jug8Gallon = 1;
    private static final int jug3Gallon = 2;
    private static final int ground = 3;

    public double step_cost(Object fromState, Object toState) { 
        int costs=0;
        StateJugsPuzzle prev = (StateJugsPuzzle) fromState;
        StateJugsPuzzle next = (StateJugsPuzzle) toState;

        for(int i=0; i< prev.jugsArray.length; i++){
            if(next.jugsArray[i] - prev.jugsArray[i] >0) costs+=(next.jugsArray[i] - prev.jugsArray[i]);
        }

        return costs;
    }
    
    public double h(Object state) {
        return 0;
    }
    
    public boolean goal_test(Object state) {
        StateJugsPuzzle currentState = (StateJugsPuzzle) state;

        int[] results =  currentState.jugsArray;
        for(int res: results)
            if(res==1) return true;

        return false;
    }

    private boolean isValid(StateJugsPuzzle state) {
        int[] jugs = state.jugsArray;

        if(jugs[0] > 12 || jugs[0]<0) return false;
        if(jugs[1] > 8 || jugs[1]<0) return false;
        if(jugs[2] > 3 || jugs[2]<0) return false;

        return true;
    }

     public Set<Object> getSuccessors(Object state) {
        Set<Object> set = new HashSet<Object>();
        StateJugsPuzzle jugs_state = (StateJugsPuzzle) state;

        //Let's create without any constraint, then remove the illegal ones
        StateJugsPuzzle successor_state;
        int amount = 0;
        int capacity12G, capacity8G, capacity3G, remain12, remain8, remain3 = 0;

        //Filling Water into 12 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        if (successor_state.jugsArray[jug12Gallon] < 12) {
            amount = 12 - successor_state.jugsArray[jug12Gallon];
            successor_state.jugsArray[jug12Gallon] += amount;
            if (isValid(successor_state)) set.add(successor_state);
        }

        //Filling Water into 8 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        if (successor_state.jugsArray[jug8Gallon] < 8) {
            amount = 8 - successor_state.jugsArray[jug8Gallon];
            successor_state.jugsArray[jug8Gallon] += amount;
            if (isValid(successor_state)) set.add(successor_state);
        }


        //Filling Water into 3 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        if (successor_state.jugsArray[jug3Gallon] < 3) {
            amount = 3 - successor_state.jugsArray[jug3Gallon];
            successor_state.jugsArray[jug3Gallon] += amount;
            if (isValid(successor_state)) set.add(successor_state);
        }


        //Pour water from 12 gallons to 8 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        capacity12G = successor_state.jugsArray[jug12Gallon];
        remain8 = 8 - successor_state.jugsArray[jug8Gallon];
        if(capacity12G > 0 && remain8 >0) {
            if (capacity12G <= remain8) {
                successor_state.jugsArray[jug12Gallon] -= capacity12G;
                successor_state.jugsArray[jug8Gallon] += capacity12G;
            } else {
                successor_state.jugsArray[jug12Gallon] -= remain8;
                successor_state.jugsArray[jug8Gallon] += remain8;
            }
            if(isValid(successor_state)) set.add(successor_state);
        }

        //Pour water from 12 gallons to 3 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        capacity12G = successor_state.jugsArray[jug12Gallon];
        remain3 = 3 - successor_state.jugsArray[jug3Gallon];
        if (capacity12G > 0 && remain3 >0) {
            if (capacity12G <= remain3) {
                successor_state.jugsArray[jug12Gallon] -= capacity12G;
                successor_state.jugsArray[jug3Gallon] += capacity12G;
            } else {
                successor_state.jugsArray[jug12Gallon] -= remain3;
                successor_state.jugsArray[jug3Gallon] += remain3;
            }
            if (isValid(successor_state)) set.add(successor_state);
        }

        //Pour water from 8 gallons to 3 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        capacity8G = successor_state.jugsArray[jug8Gallon];
        remain3 = 3 - successor_state.jugsArray[jug3Gallon];
        if (capacity8G > 0 && remain3 > 0) {
            if (capacity8G <= remain3) {
                successor_state.jugsArray[jug8Gallon] -= capacity8G;
                successor_state.jugsArray[jug3Gallon] += capacity8G;
            } else {
                successor_state.jugsArray[jug8Gallon] -= remain3;
                successor_state.jugsArray[jug3Gallon] += remain3;
            }
            if(isValid(successor_state)) set.add(successor_state);
        }

        //Pour water from 8 gallons to 12 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        capacity8G = successor_state.jugsArray[jug8Gallon];
        remain12 = 12 - successor_state.jugsArray[jug12Gallon];
        if (capacity8G > 0 && remain12 > 0) {
            if (capacity8G <= remain12) {
                successor_state.jugsArray[jug8Gallon] -= capacity8G;
                successor_state.jugsArray[jug12Gallon] += capacity8G;
            } else {
                successor_state.jugsArray[jug8Gallon] -= remain12;
                successor_state.jugsArray[jug12Gallon] += remain12;
            }
            if(isValid(successor_state)) set.add(successor_state);
        }

        //Pour water from 3 gallons to 8 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        capacity3G = successor_state.jugsArray[jug3Gallon];
        remain8 = 8 - successor_state.jugsArray[jug8Gallon];
        if (capacity3G > 0 && remain8 > 0) {
            if (capacity3G != 0 && capacity3G <= remain8) {
                successor_state.jugsArray[jug3Gallon] -= capacity3G;
                successor_state.jugsArray[jug8Gallon] += capacity3G;
            } else {
                successor_state.jugsArray[jug3Gallon] -= remain8;
                successor_state.jugsArray[jug8Gallon] += remain8;
            }
            if(isValid(successor_state)) set.add(successor_state);
        }

        //Pour water from 3 gallons to 12 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        capacity3G = successor_state.jugsArray[jug3Gallon];
        remain12 = 12 - successor_state.jugsArray[jug12Gallon];
        if (capacity3G > 0 && remain12 > 0) {
            if (capacity3G != 0 && capacity3G <= remain12) {
                successor_state.jugsArray[jug3Gallon] -= capacity3G;
                successor_state.jugsArray[jug12Gallon] += capacity3G;
            } else {
                successor_state.jugsArray[jug3Gallon] -= remain12;
                successor_state.jugsArray[jug12Gallon] += remain12;
            }
            if(isValid(successor_state)) set.add(successor_state);
        }

        //Pour water out from 12 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        if (successor_state.jugsArray[jug12Gallon]>0){
            successor_state.jugsArray[ground] += successor_state.jugsArray[jug12Gallon]; 
            successor_state.jugsArray[jug12Gallon] = 0;
            if(isValid(successor_state)) set.add(successor_state);
        }

        //Pour water out from 8 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        if (successor_state.jugsArray[jug8Gallon]>0){
            successor_state.jugsArray[ground] += successor_state.jugsArray[jug8Gallon]; 
            successor_state.jugsArray[jug8Gallon] = 0;
            if(isValid(successor_state)) set.add(successor_state);
        }

        //Pour water out from 3 gallons jug
        successor_state = new StateJugsPuzzle(jugs_state);
        if (successor_state.jugsArray[jug3Gallon]>0){
            successor_state.jugsArray[ground] += successor_state.jugsArray[jug3Gallon]; 
            successor_state.jugsArray[jug3Gallon] = 0;
            if(isValid(successor_state)) set.add(successor_state);
        }

        return set;

     }


    public static void main(String[] args) throws Exception {
		ProblemJugsPuzzle problem = new ProblemJugsPuzzle();
        System.out.println("Water Jugs Problem using Uninformed search strategies:");
        
        int [] waterJugs = {0,0,0,0};	//Array -> 12g, 8g, 3g and Ground
        problem.initialState = new StateJugsPuzzle(waterJugs);
		Search search  = new Search(problem);
		
        System.out.println("Question #4:\nWater Jugs Problem using Uninformed search strategies:");
        System.out.println("Array indicates the amount of water in 12gallon, 8gallon, 3gallon jugs and poured into the ground");

		System.out.println("BreadthFirstTreeSearch:\t\t" + search.BreadthFirstTreeSearch());
        System.out.println("\nBreadthFirstGraphSearch:\t" + search.BreadthFirstGraphSearch());
        
		System.out.println("\nDepthFirstTreeSearch:\t" + search.DepthFirstTreeSearch());
        System.out.println("\nDepthFirstGraphSearch:\t" + search.DepthFirstGraphSearch());
        
        System.out.println("\nUniformCostTreeSearch:\t" + search.UniformCostTreeSearch());
        System.out.println("\nUniformCostGraphSearch:\t" + search.UniformCostGraphSearch());

        System.out.println("\nIterativeDeepeningTreeSearch:\t" + search.IterativeDeepeningTreeSearch());
        System.out.println("\nIterativeDeepeningGraphSearch:\t" + search.IterativeDeepeningGraphSearch());

    }

}
