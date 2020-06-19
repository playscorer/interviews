package byhiras.tracker;

import java.util.Set;

import byhiras.domain.Bid;
import byhiras.domain.Buyer;
import byhiras.domain.Item;
import byhiras.tracker.exception.BidException;

public interface ITracker {

    /**
     * Register an item in the tracker
     */
    void registerItem(Item item);

    /**
     * Record a bid by a buyer on an item
     * 
     * @param price
     */
    void recordBid(Item item, Buyer buyer, double price);

    /**
     * Get the current winning bid on an item
     */
    Bid getCurrentWinBid(Item item) throws BidException;

    /**
     * Get all bids on an item
     */
    Set<Bid> getAllBids(Item item) throws BidException;

    /**
     * Get all items the buyer made a bid
     */
    Set<Item> getAllItems(Buyer buyer) throws BidException;

}
