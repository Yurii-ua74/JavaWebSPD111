<%@ page import="step.learning.dal.dto.PromotionDTO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String context = request.getContextPath();
    // Вилучаємо дані, передані сервлетом
    List<PromotionDTO> promotions = (List<PromotionDTO>) request.getAttribute("promotion");
%>

<h1>Акційні пропозиції</h1>

<%  int i = 1;
    for (PromotionDTO prom : promotions) { %>
<div class="row">
    <div class="col s6 m3">
        <div class="card">
            <div class="card-image ">
                <img src="<%=context%>/img/im<%=i%>.jpg" alt="img"/>
                <span class="card-title"><%=prom.getName()%></span>
                <a class="btn-floating halfway-fab waves-effect waves-light red"><i class="material-icons">add</i></a>
            </div>
            <div class="card-content">
                <p><%=prom.getPrice()%></p>
                <p><%=prom.getDiscount()%></p>
            </div>
        </div>
    </div>
</div>
<%  i++; } %>

