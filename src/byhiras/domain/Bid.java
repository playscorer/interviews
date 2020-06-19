package byhiras.domain;

import java.util.Date;

/**
 * The bid class
 *
 */
public class Bid implements Comparable<Bid> {

    private double price;

    private Date date;

    private Buyer buyer;

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Buyer getBuyer() {
	return buyer;
    }

    public void setBuyer(Buyer buyer) {
	this.buyer = buyer;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((buyer == null) ? 0 : buyer.hashCode());
	result = prime * result + ((date == null) ? 0 : date.hashCode());
	long temp;
	temp = Double.doubleToLongBits(price);
	result = prime * result + (int) (temp ^ (temp >>> 32));
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
	Bid other = (Bid) obj;
	if (buyer == null) {
	    if (other.buyer != null)
		return false;
	} else if (!buyer.equals(other.buyer))
	    return false;
	if (date == null) {
	    if (other.date != null)
		return false;
	} else if (!date.equals(other.date))
	    return false;
	if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
	    return false;
	return true;
    }

    @Override
    public int compareTo(Bid o) {
	if (this == o) {
	    return 0;
	}
	if (price < o.price) {
	    return -1;
	} else if (price > o.price) {
	    return 1;
	} else {
	    return 0;
	}
    }

    @Override
    public String toString() {
	return "Bid [price=" + price + ", date=" + date + ", buyer=" + buyer + "]";
    }

}
