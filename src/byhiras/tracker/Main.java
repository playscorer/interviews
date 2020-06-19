package byhiras.tracker;

import java.util.Set;

import byhiras.domain.Bid;
import byhiras.domain.Buyer;
import byhiras.domain.Item;
import byhiras.tracker.exception.BidException;

public class Main {

    public static void main(String[] args) throws BidException {
	ITracker tracker = new BidTracker();

	Item acAdapterItem = new Item("AC Adapter", 7, 25);
	tracker.registerItem(acAdapterItem);

	Buyer buyerFil = new Buyer("Fil", "A", "London");
	tracker.recordBid(acAdapterItem, buyerFil, 2);

	Set<Bid> acBids = tracker.getAllBids(acAdapterItem);
	System.out.println("All bids for the ac adapter : " + acBids);

	Set<Item> itemsFil = tracker.getAllItems(buyerFil);
	System.out.println("All items Filipe is interested in : " + itemsFil);

	Buyer buyerSeb = new Buyer("Seb", "F", "London");
	tracker.recordBid(acAdapterItem, buyerSeb, 12);

	try {
	    tracker.getCurrentWinBid(acAdapterItem);
	} catch (BidException e) {
	    System.out.println(e.getMessage());
	}

	Buyer buyerFlo = new Buyer("Flo", "J", "Deauville");
	tracker.recordBid(acAdapterItem, buyerFlo, 25);

	Bid winBid = tracker.getCurrentWinBid(acAdapterItem);
	System.out.println("Winning bid on the ac adapter : " + winBid);

	Buyer buyerOli = new Buyer("Oli", "J", "Paris");
	tracker.recordBid(acAdapterItem, buyerOli, 27);

	winBid = tracker.getCurrentWinBid(acAdapterItem);
	System.out.println("Winning bid on the ac adapter : " + winBid);

	acBids = tracker.getAllBids(acAdapterItem);
	System.out.println("All bids for the ac adapter : " + acBids);

	Item hDTV = new Item("HDTV", 5, 430);
	tracker.registerItem(hDTV);

	tracker.recordBid(hDTV, buyerFil, 55);

	itemsFil = tracker.getAllItems(buyerFil);
	System.out.println("All items Filipe is interested in : " + itemsFil);

    }

}
