package csp_post;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CSPZebraWaterProblem extends CSP {
    static Set<Object> varColor = new HashSet<Object>(Arrays.asList(new String[] {"blue", "green", "ivory", "red", "yellow"}));
    static Set<Object> varDrink = new HashSet<Object>(Arrays.asList(new String[] {"coffee", "milk", "orange-juice", "tea", "water"}));
    static Set<Object> varNationality = new HashSet<Object>(Arrays.asList(new String[] {"englishman", "japanese", "norwegian", "spaniard", "ukrainian"}));
    static Set<Object> varPet = new HashSet<Object>(Arrays.asList(new String[] {"dog", "fox", "horse", "snails", "zebra"}));
    static Set<Object> varCigarette = new HashSet<Object>(Arrays.asList(new String[] {"chesterfield", "kools", "lucky-strike", "old-gold", "parliament"}));

    public boolean isGood(Object X, Object Y, Object x, Object y) {
        // isGood is used in search to check if rules are violated
        // search chooses a Variable that has not yet been assigned, and for each value in the domain checks if
        // inserting Variable X with position x into the assignment would violate any rules, inserts if none are violated.

        // X: Variable that has not been assigned
        // Y: Variable that has been assigned (exists as key within assignment TreeMap)
        // x: a member of the Domain for Variable X (ex: 2 from [1,2,3,4,5])
        // y: the currently assigned Domain value of Y (value for key Y in TreeMap)

        // If X is not even mentioned in by the constraints, just return true
        // as nothing can be violated
        if (!C.containsKey(X)) {
            return true;
        }

        // Rules 1, 2, 3, 4, 6, 7, 12, 13
        Object[][] conditions = {
                {"englishman", "red"},
                {"spaniard", "dog"},
                {"coffee", "green"},
                {"ukrainian", "tea"},
                {"old-gold", "snails"},
                {"kools", "yellow"},
                {"lucky-strike", "orange-juice"},
                {"japanese", "parliament"}
        };

        for (Object[] pair : conditions) {
            if (X.equals(pair[0]) && Y.equals(pair[1]) && !x.equals(y)) {
                return false;
            }

        }

        // Rules 8, 9 (works)
        Object[][] conditions2 = {
                {"milk", Integer.valueOf(3)},// new Integer(3)},
                {"norwegian", Integer.valueOf(1)} //new Integer(1)}
        };

        for (Object[] pair : conditions2) {
            if ((X.equals(pair[0]) && !x.equals(pair[1])) || (Y.equals(pair[0]) && !y.equals(pair[1]))) {
                return false;
            }
        }

        // Rule 5 (works)
        if ((X.equals("green") && Y.equals("ivory") && !((Integer)x - (Integer)y == 1)) ||
                (X.equals("ivory") && Y.equals("green") && !((Integer)x - (Integer)y == -1))) {
            return false;
        }

        // Rules 10, 11, 14 (works)
        Object[][] conditions3 = {
                {"chesterfield", "fox"},
                {"norwegian", "blue"},
                {"kools", "horse"}
        };

        for (Object[] pair : conditions3) {
            if ((X.equals(pair[0]) && Y.equals(pair[1]) && !(Math.abs((Integer)x - (Integer)y) == 1))) {
                return false;
            }
        }

        // Uniqueness constraints
        Object[] variables = {
                varColor,
                varDrink,
                varNationality,
                varPet,
                varCigarette
        };
        for (Object var : variables) {
            Set<Object> set = (Set<Object>)var;
            if (set.contains(X) && set.contains(Y) && !X.equals(Y) && x.equals(y)) {
                return false;
            }
        }

        // Check to see if there is an arc between X and Y
        // If there isn't an arc, then no constraint, i.e. it is good
        if (!C.get(X).contains(Y)) {
            return true;
        }

        return true;
    }

    public static void main(String[] args) throws Exception {
        CSPZebraWaterProblem csp = new CSPZebraWaterProblem();

        Integer[] domain = {1,2,3,4,5};

        // Initialize domains
        for (Object X : varColor) {
            csp.addDomain(X, domain);
        }

        for (Object X : varDrink) {
            csp.addDomain(X, domain);
        }

        for (Object X : varNationality) {
            csp.addDomain(X, domain);
        }

        for (Object X : varPet) {
            csp.addDomain(X, domain);
        }

        for (Object X : varCigarette) {
            csp.addDomain(X, domain);
        }

        // Unary constraints
        csp.addDomain("milk", new Integer[]{3});
        csp.addDomain("norwegian", new Integer[]{1});

        // Binary constraints: add constraint arcs
        Object[][] binaryConditions = {
                {"englishman", "red"},
                {"spaniard", "dog"},
                {"coffee", "green"},
                {"ukrainian", "tea"},
                {"old-gold", "snails"},
                {"kools", "yellow"},
                {"lucky-strike", "orange-juice"},
                {"japanese", "parliament"}
        };

        for (Object[] pair : binaryConditions) {
            csp.addBidirectionalArc(pair[0], pair[1]);
        }

        // Uniqueness constraints
        Object[] variables = {
                varColor,
                varDrink,
                varNationality,
                varPet,
                varCigarette
        };

        for (Object var : variables) {
            Set<Object> set = (Set<Object>)var;
            for (Object X : set) {
                for (Object Y : set) {
                    if (!X.equals(Y)) {
                        csp.addBidirectionalArc(X, Y);
                    }
                }
            }
        }

        Search search = new Search(csp);
        System.out.println(search.BacktrackingSearch());
    }
}
