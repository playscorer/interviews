package DSA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvalidTransactions {
    public List<String> invalidTransactions(String[] transactions) {
        // split[2] == amount
        // split[1] == duration
        // split[3] == location
        // split[0] == name

        List<String> failedTrans = new ArrayList<>();

        for (int i=0; i<transactions.length; i++) {
            boolean invalid = false;
            String[] splitI = transactions[i].split(",");

            for (int j=0; !invalid && j<transactions.length; j++) {
                String[] splitII = transactions[j].split(",");

                if (Integer.parseInt(splitI[2]) > 1000
                        || splitI[0].equals(splitII[0])
                        && !splitI[3].equals(splitII[3])
                        && Math.abs(Integer.parseInt(splitI[1]) - Integer.parseInt(splitII[1])) <= 60) {

                    invalid = true;
                    failedTrans.add(transactions[i]);
                }
            }
        }
        return failedTrans;
    }
}
