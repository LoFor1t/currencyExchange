<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello Servlets!" %></h1>
<br/>
<a href="currencies">Get all Currencies</a>
<%--<br/>--%>
<%--<form action="/currency/">--%>
<%--    <label>--%>
<%--        <input name="currencyName">--%>
<%--    </label>--%>
<%--    <button type="button">Send</button>--%>
<%--</form>--%>
<br/>
<a href="exchangeRates">Get all exchange rates</a>

<br/>
<form method="post" action="${pageContext.request.contextPath}/currencies">
    <label>
        <input name="name">
    </label>
    <br/>
    <label>
        <input name="code">
    </label>
    <br/>
    <label>
        <input name="sign">
    </label>
    <br/>
    <button type="submit">Create new currency</button>
</form>

<br/>
<form method="post" action="${pageContext.request.contextPath}/exchangeRates">
    <label>
        <input name="baseCurrencyCode">
    </label>
    <br/>
    <label>
        <input name="targetCurrencyCode">
    </label>
    <br/>
    <label>
        <input name="rate">
    </label>
    <br/>
    <button type="submit">Create new exchange rate.</button>
</form>
</body>
</html>