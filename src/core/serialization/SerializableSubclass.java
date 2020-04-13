package core.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableSubclass extends NonSerializableParent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int j;
    
    public SerializableSubclass() {
	super(20, 25);
	j = 30;
    }

    /**
     * @return the j
     */
    public int getJ() {
        return j;
    }
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
	oos.defaultWriteObject();
	oos.writeObject(getI());
	oos.writeObject(getK());
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	ois.defaultReadObject();
	Integer i = (Integer) ois.readObject();
	Integer k = (Integer) ois.readObject();
	setI(i);
	setK(k);
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
	SerializableSubclass subClass = new SerializableSubclass();
	String filename = "serialized.txt";
	
	System.out.println("before serialization i=" + subClass.getI() + ", j=" +subClass.getJ() + ", k=" +subClass.getK());
	
	FileOutputStream fileOutputStream = new FileOutputStream(filename);
	ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
	objectOutputStream.writeObject(subClass);
	objectOutputStream.flush();
	objectOutputStream.close();
	
	FileInputStream fileInputStream = new FileInputStream(filename);
	ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
	SerializableSubclass subClass2 = (SerializableSubclass) objectInputStream.readObject();
	objectInputStream.close();
	
	System.out.println("before serialization i=" + subClass2.getI() + ", j=" +subClass2.getJ() + ", k=" +subClass2.getK());
    }

}
