package byhiras.domain;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import byhiras.tracker.exception.BidException;

/**
 * The item to be sold
 *
 */
public class Item {
    private static long refNumber = 0;

    private long reference;

    private String title;

    private Date startDate;

    private int duration;

    private double currentPrice;

    private double reservedPrice;

    private SortedSet<Bid> bids;

    public Item(String title, int duration, double reservedPrice) {
	reference = refNumber++;
	this.title = title;
	startDate = new Date();
	this.duration = duration;
	currentPrice = 0;
	this.reservedPrice = reservedPrice;
    }

    public Set<Bid> getBids() {
	return bids;
    }

    public Bid getCurrentWinBid() throws BidException {
	if (bids == null) {
	    throw new BidException("No bids on this item!");
	}
	Bid latestBid = bids.last();
	if (latestBid.getPrice() >= reservedPrice) {
	    return latestBid;
	} else {
	    throw new BidException("No winning bids yet!");
	}
    }

    public void addBid(Bid newBid) {
	if (bids == null) {
	    bids = new TreeSet<Bid>();
	}
	bids.add(newBid);
	currentPrice = newBid.getPrice();
    }

    public static long getRefNumber() {
	return refNumber;
    }

    public long getReference() {
	return reference;
    }

    public String getTitle() {
	return title;
    }

    public Date getStartDate() {
	return startDate;
    }

    public int getDuration() {
	return duration;
    }

    public double getCurrentPrice() {
	return currentPrice;
    }

    public double getReservedPrice() {
	return reservedPrice;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (reference ^ (reference >>> 32));
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
	Item other = (Item) obj;
	if (reference != other.reference)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Item [reference=" + reference + ", title=" + title + ", startDate=" + startDate + ", duration=" + duration
		+ ", currentPrice=" + currentPrice + ", reservedPrice=" + reservedPrice + "]";
    }

}
