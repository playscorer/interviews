package Point72STP;

/**
 * Resource Optimization
 *
 * In a cloud infrastructure system, resources include:
 * - Virtual machines (VMs)
 * - Storage units
 * - Combined resource bundles that provide one VM and one storage unit
 *
 * Costs are represented by:
 * - vmCost: cost of one virtual machine
 * - storageCost: cost of one storage unit
 * - combinedCost: cost of one combined resource bundle
 */
public class Exercise1 {
    /**
     * This method determines the minimum expenditure required to meet the demand
     * for a given number of virtual machines and storage units.
     *
     * @param vmCost        the cost of a virtual machine
     * @param storageCost  the cost of a storage unit
     * @param combinedCost the cost of a combined resource bundle (1 VM + 1 storage)
     * @param x             the number of virtual machines required
     * @param y             the number of storage units required
     * @return the minimum total cost to acquire the required virtual machines
     *         and storage units
     */
    public static long calcMinimumExpenditure(int vmCost, int storageCost, int combinedCost, int x, int y) {
        long normal = (long) x  * vmCost + (long) y * storageCost;
        long combinedMin = (long) Math.min(x, y) * combinedCost
                + (long) (x - Math.min(x, y)) * vmCost + (long) (y - Math.min(x, y)) * storageCost;
        long combinedMax = (long) Math.max(x, y) * combinedCost
                + (long) Math.max(0, x - Math.max(x, y)) * vmCost + (long) Math.max(0, y - Math.max(x, y)) * storageCost;
        return Math.min(normal, Math.min(combinedMin, combinedMax));
    }

    public static void main(String[] args) {
        int vmCost = 1;
        int storageCost = 2;
        int combinedCost = 2;
        int x = 2;
        int y = 1;
        System.out.println(calcMinimumExpenditure(vmCost, storageCost, combinedCost, x, y));

        vmCost = 3;
        storageCost = 2;
        combinedCost = 1;
        x = 4;
        y = 3;
        System.out.println(calcMinimumExpenditure(vmCost, storageCost, combinedCost, x, y));
    }

}
