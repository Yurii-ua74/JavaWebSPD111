<%@ page import="step.learning.dal.dto.CartItem" %>
<%@ page import="step.learning.models.CartPageModel" %>
<%@ page import="step.learning.dal.dto.Product" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // контекст
    String context = request.getContextPath() ;
    // вилучаємо дані, передані сервлетом (контроллером)
    // CartItem[] cartItems = (CartItem[]) request.getAttribute("cart");
    CartPageModel model = (CartPageModel) request.getAttribute("model");
%>

<div class="row">

    <div class="col s8">
        <div class="row">
        <% for(Product product : model.getProducts() ) { %>

            <div class="col s4">
                <div class="card small">
                    <div class="card-image">
                        <img src="<%=context%>/img/products/<%=product.getImage() == null ? "no_image.png" : product.getImage()%>" alt="Image">
                        <%-- /*/////////////////////////  //////////////////////////*/ --%>
                        <a data-product="<%=product.getId()%>" class="product-cart-btn btn-floating halfway-fab waves-effect waves-light red" id="product-cart-button">
                            <i class="material-icons">shopping_cart</i>
                        </a>
                    </div>
                    <div class="card-content">
                        <span class="card-title"><%=product.getName()%></span>
                        <p><%=product.getDescription()%></p>
                        <p><%=product.getPrice()%></p>
                    </div>
                </div>
            </div>
          <% } %>
        </div>
    </div>


    <%
        List<Product> products = model.getProducts();
        List<CartItem> cartItems = model.getCartItems();
    %>

        <h1>Ваш кошик</h1>

    <%
        for(CartItem item : cartItems) {
        for(Product product : products) {
        // Відображаємо дані про продукт
        if(String.valueOf(item.getProductId()).equals(String.valueOf(product.getId()))) {
    %>
    <div class="col s4">
        <div class="col s12">
            <div class="card horizontal">
                <div class="card-image flex1">
                    <img src="<%=context%>/img/products/<%=product.getImage() == null ? "no_image.png" : product.getImage()%>" alt="Image">
                </div>
                <div class="card-stacked flex3">
                    <div class="card-content">

                        <p>Кількість: <%= item.getCount() %></p>
                        <p><%= product.getName() %></p>
                        <p><%= product.getDescription() %></p>

                    </div>
                    <div class="card-action">
                        <a href="#">видалити з кошику</a>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <%  } %>
    <%  } %>
    <%  } %>

</div>
