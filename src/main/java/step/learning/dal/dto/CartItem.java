package step.learning.dal.dto;

import java.util.UUID;

public class CartItem {
    private UUID id;
    private UUID productId;
    private int count;
    // Alt + insert  або прав мишою -> GENERATE
    // constructor / select non
    public CartItem() {
    }
    // Alt + insert  або прав мишою -> GENERATE
    // constructor
    public CartItem(UUID id, UUID productId, int count) {
        this.id = id;
        this.productId = productId;
        this.count = count;
    }
    //  Alt + insert  або прав мишою -> GENERATE

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    //  Alt + insert
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
