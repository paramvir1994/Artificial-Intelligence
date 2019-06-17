package csp_post;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Search {
    CSP csp;

    public Search(CSP csp) {
        this.csp = csp;
    }

    // Returns an assignment
    public Map<Object, Object> BacktrackingSearch() {
        // Create an empty assignment
        Map<Object, Object> assignment = new TreeMap<Object, Object>();
        return Backtrack(assignment);
    }

    private Map<Object, Object> Backtrack(Map<Object, Object> assignment) {
        if (isComplete(assignment)) {
            return assignment;
        }

        Object X = SelectUnassignedVariable(assignment);
        for (Object x : this.csp.D.get(X)) {
            if (isConsistent(X,x,assignment)) {
                assign(X, x,assignment);
                Map<Object, Object> result = Backtrack(assignment);
                if (result != null) {
                    return result;
                }
                assignUndo(X, assignment);
            }
        }
        return null;
    }

    Deque<Map<Object, Set<Object>>> stack = new ArrayDeque<Map<Object, Set<Object>>>();

    // Assigns x to X and does inferencing
    private void assign(Object X, Object x, Map<Object, Object> assignment) {
        assignment.put(X, x);

        //now do forward checking and record deleted values 
        //var-set of deleted values
        if (!this.csp.C.containsKey(X)) {
            return;
        }

        Map<Object, Set<Object>> deletedValues = new TreeMap<Object, Set<Object>>();

        for (Object Y : this.csp.C.get(X)) {
            if (assignment.containsKey(Y)) {
                continue;
            }

            for (Object y : this.csp.D.get(Y)) {
                if (!this.csp.isGood(X, Y, x, y)) {
                    if (!deletedValues.containsKey(Y)) {
                        deletedValues.put(Y, new TreeSet<Object>());
                    }
                    deletedValues.get(Y).add(y);
                }
            }
        }

        stack.push(deletedValues);

        // Do the real value deletions from variable domains in csp
        for (Object Y : deletedValues.keySet()) {
            this.csp.D.get(Y).removeAll(deletedValues.get(Y));
        }
    }

    private void assignUndo(Object X, Map<Object, Object> assignment) {
        assignment.remove(X);
        
        // Now undo value deletions by forward checking
        if (stack.isEmpty()) {
            return;
        }

        Map<Object, Set<Object>> deletedValues = stack.pop();

        for (Object Y : deletedValues.keySet()) {
            this.csp.D.get(Y).addAll(deletedValues.get(Y));
        }
    }

    private boolean isConsistent(Object X, Object x, Map<Object, Object> assignment) {
        for (Object Y : assignment.keySet()) {
            Object y = assignment.get(Y);

            if (!this.csp.isGood(X,Y,x,y)) {
                return false;
            }

            if (!this.csp.isGood(Y,X,y,x)) {
                return false;
            }
        }
        return true;
    }

    private Object SelectUnassignedVariable(Map<Object, Object> assignment) {
        // Implements minimum remaining values (MRV)
        int min = Integer.MAX_VALUE;
        Object Xmin = null;
        
        for (Object X : this.csp.D.keySet()) {
            if (assignment.containsKey(X)) {
                continue;
            }

            if (this.csp.D.get(X).size() < min) {
                min = this.csp.D.get(X).size();
                Xmin = X;
            }
        }

        return Xmin;
    }

    private boolean isComplete(Map<Object, Object> assignment) {
        if (assignment.size() == this.csp.D.size()) {
            return true;
        }

        return false;
    }
}
