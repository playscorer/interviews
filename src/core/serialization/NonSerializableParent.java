package core.serialization;

public class NonSerializableParent {
    
    private int i,k;

    /**
     * Without no-arg constructor deserialization generates an InvalidClassException 
     */
    public NonSerializableParent() {
	i=15;
	k=18;
    }

    public NonSerializableParent(int i, int k) {
	this.i = i;
	this.k = k;
    }

    /**
     * @return the i
     */
    public int getI() {
        return i;
    }

    /**
     * @return the k
     */
    public int getK() {
        return k;
    }

    /**
     * @param i the i to set
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * @param k the k to set
     */
    public void setK(int k) {
        this.k = k;
    }

}
