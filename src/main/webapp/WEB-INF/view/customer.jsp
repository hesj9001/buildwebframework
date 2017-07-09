<%--
  Created by IntelliJ IDEA.
  User: shijia
  Date: 7/9/17
  Time: 9:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>customer management</title>
</head>
<body>

<h1>customer list</h1>

<table>
    <tr>
        <th>customer name</th>
        <th>contact</th>
        <th>telephone</th>
        <th>email address</th>
        <th>operation</th>
    </tr>
    <c:forEach var="customer" items="${customers}">
        <tr>
            <td>${customer.name}</td>
            <td>${customer.contact}</td>
            <td>${customer.telephone}</td>
            <td>${customer.email}</td>
            <td>
                <a href="${BASE}/customer_edit?id=${customer.id}">edit</a>
                <a href="${BASE}/customer_delete?id=${customer.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
