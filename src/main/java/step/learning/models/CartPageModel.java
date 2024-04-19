package step.learning.models;

import step.learning.dal.dto.CartItem;
import step.learning.dal.dto.Product;

import java.util.List;

public class CartPageModel {
    private List<Product> products;
    private List<CartItem> cartItems;
    // private List<CartItemProduct> cartItemProducts;


    public CartPageModel(List<Product> products, List<CartItem> cartItems) {
        this.products = products;
        this.cartItems = cartItems;
        // this.cartItemProducts = cartItemProducts;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    //public List<CartItemProduct> getCartItemProducts() {
    //    return cartItemProducts;
    //}
//
    //public void setCartItemProducts(List<CartItemProduct> cartItemProducts) {
    //    this.cartItemProducts = cartItemProducts;
    //}
}
