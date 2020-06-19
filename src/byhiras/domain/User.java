package byhiras.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * The user class
 *
 */
public abstract class User {

    private String firstName;

    private String lastName;

    private String address;

    private Set<Item> items;

    public User(String firstName, String lastName, String address) {
	this.firstName = firstName;
	this.lastName = lastName;
	this.address = address;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public void addItem(Item item) {
	if (items == null) {
	    items = new HashSet<Item>();
	}
	items.add(item);
    }

    public Set<Item> getItems() {
	return items;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((address == null) ? 0 : address.hashCode());
	result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
	result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	User other = (User) obj;
	if (address == null) {
	    if (other.address != null)
		return false;
	} else if (!address.equals(other.address))
	    return false;
	if (firstName == null) {
	    if (other.firstName != null)
		return false;
	} else if (!firstName.equals(other.firstName))
	    return false;
	if (lastName == null) {
	    if (other.lastName != null)
		return false;
	} else if (!lastName.equals(other.lastName))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "User [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + "]";
    }

}
