<%@ page contentType="text/html;charset=UTF-8" language="java"
    import="java.io.*"
    import="java.util.HashMap"
    import="java.util.Iterator"
    import="org.gradle.demo.HoldingRecord"%>
<html>
    <style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }
    </style>
    <head>
        <title>13F Page</title>
    </head>
    <body>
    <table>
        <tr>
        <th>Cusip</th>
        <th>Issuer Name</th>
        <th>Number of Shares</th>
        </tr>
        <%
        HashMap<String, HoldingRecord> holdingRecords = (HashMap<String, HoldingRecord>) request.getAttribute("fData");
        Iterator iterator = holdingRecords.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            HoldingRecord hr = (HoldingRecord) pair.getValue();
            %>
            <tr>
            <td><%out.println(hr.getCusip());%></td>
            <td><%out.println(hr.getIssuerName());%></td>
            <td><%out.println(hr.getNumberOfShares());%></td>
            </tr>
        <%}%>
    </table>
    </body>
</html>