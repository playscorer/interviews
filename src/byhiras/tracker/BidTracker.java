package byhiras.tracker;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import byhiras.domain.Bid;
import byhiras.domain.Buyer;
import byhiras.domain.Item;
import byhiras.tracker.exception.BidException;

public class BidTracker implements ITracker {

    private Set<Item> items;

    public BidTracker() {
	items = new HashSet<Item>();
    }

    @Override
    public void registerItem(Item item) {
	items.add(item);
    }

    @Override
    public void recordBid(Item item, Buyer buyer, double price) {
	if (item != null) {
	    Bid bid = new Bid();
	    bid.setPrice(price);
	    bid.setBuyer(buyer);
	    bid.setDate(new Date());
	    buyer.addItem(item);
	    item.addBid(bid);
	}
    }

    @Override
    public Bid getCurrentWinBid(Item item) throws BidException {
	if (item == null) {
	    throw new BidException("Item must not be null");
	} else {
	    return item.getCurrentWinBid();
	}
    }

    @Override
    public Set<Bid> getAllBids(Item item) throws BidException {
	if (item == null) {
	    throw new BidException("Item must not be null");
	} else {
	    return item.getBids();
	}
    }

    @Override
    public Set<Item> getAllItems(Buyer buyer) throws BidException {
	if (buyer == null) {
	    throw new BidException("Buyer must not be null");
	} else {
	    return buyer.getItems();
	}
    }

}
