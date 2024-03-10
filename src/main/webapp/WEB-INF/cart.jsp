<%@ page import="step.learning.dal.dto.CartItem" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String context = request.getContextPath();
    // Вилучаємо дані, переданісервлетом (контролером)
    CartItem[] cartItem = (CartItem[]) request.getAttribute("cart");
%>
<h1>ВАШ КОШИК</h1>
<%-- Відображення даних --%>
<% for(CartItem item : cartItem) { %>
<div class="col s12 m7">
    <h2 class="header">Horizontal Card</h2>
    <div class="card horizontal">
        <div class="card-image flex1">
            <img src="<%=context%>/img/no_image.png" alt="img"/>
        </div>
        <div class="card-stacked flex3">
            <div class="card-content">
                <p><%=item.getProductId() %></p>
                <p><%=item.getCount() %></p>
            </div>
            <div class="card-action">
                <a href="#">видалити з кошика</a>
            </div>
        </div>
    </div>
</div>
<% } %>
