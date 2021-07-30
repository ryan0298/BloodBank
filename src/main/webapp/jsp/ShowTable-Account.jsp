<%-- 
    Document   : ShowTable-Account
    Created on : Jan 4, 2020
    Author     : Shariar (Shawn) Emami
--%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${title}"/></title>
        <!-- https://www.w3schools.com/css/css_table.asp -->
        <link rel="stylesheet" type="text/css" href="style/tablestyle.css">
        <script>
            var isEditActive = false;
            var activeEditID = -1;
            function createTextInput(text, name) {
                var node = document.createElement("input");
                node.name = name;
                node.className = "editor";
                node.type = "text";
                node.value = text;
                return node;
            }
            function convertCellToInput( id, readOnly, name){
                var idCell = document.getElementById(id);
                var idInput = createTextInput(idCell.innerText, name);
                idInput.readOnly = readOnly;
                idCell.innerText = null;
                idCell.appendChild(idInput);
            }
            window.onload = function () {
                var elements = document.getElementsByClassName("edit");
                for (let i = 0; i < elements.length; i++) {
                    elements[i].childNodes[0].onclick = function () {
                        var id = elements[i].id;
                        if (isEditActive) {
                            if (activeEditID === id) {
                                this.type = "submit";
                            }
                            return;
                        }
                        isEditActive = true;
                        activeEditID = id;
                        this.value = "Update";
                        
                        <c:forEach var="code" items="${columnCode}">
                            convertCellToInput( ++id, false, "${code}");
                        </c:forEach>
                    };
                }
            };
        </script>
    </head>
    <body>
        <!-- https://www.w3schools.com/css/css_table.asp -->
        <form>
            <table style="vertical-align:middle">
                <tr>
                    <td><input type="text" name="searchText" /></td>
                    <td><input type="submit" value="Search" /></td>
                </tr>
            </table>
        </form>
        <form method="post">
            <table border="1">
                <tr>
                    <th><input type="submit" name="delete" value="Delete" /></th>
                    <th>Edit</th>
                    <c:forEach var="name" items="${columnName}">
                        <th>${name}</th>
                    </c:forEach>
                </tr>
                <c:set var="counter" value="-1"/>
                <c:forEach var="entity" items="${entities}">
                    <tr>
                        <td class="delete">
                            <input type="checkbox" name="deleteMark" value="${entity[0]}" />
                        </td>
                        <c:set var="counter" value="${counter+1}"/>
                        <td class="edit" id="${counter}" ><input class="update" type="button" name="edit" value="Edit" /></td>
                        <c:forEach var="data" items="${entity}">
                            <c:set var="counter" value="${counter+1}"/>
                            <td class="name" id="${counter}" >${data}</td>
                        </c:forEach>
                    </tr>
                </c:forEach>
                <tr>
                    <th><input type="submit" name="delete" value="Delete" /></th>
                    <th>Edit</th>
                    <c:forEach var="name" items="${columnName}">
                        <th>${name}</th>
                    </c:forEach>
                </tr>
            </table>
        </form>
        <div style="text-align: center;">
            <pre>${path}</br>${request}${message}</pre>
        </div>
    </body>
</html>
