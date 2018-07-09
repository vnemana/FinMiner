<%@ page contentType="text/html;charset=UTF-8" language="java"
    import="java.io.*"
    import="java.util.ArrayList"
    import="org.gradle.demo.FilingLinkInfo"%>
<html>
    <style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }
    </style>
    <head>
        <title>Update 13F Filings</title>
    </head>
    <body>
    <form method="post" action="updatefiling">
        <script>
            function selectAll(box) {
                var boxes = document.getElementsByName("filing");
                var i = 0;
                while (i < boxes.length) {
                    boxes[i].checked = boxes[0].checked;
                    i++;
                }
            }
        </script>
        <table>
            <tr>
            <th>Select</th>
            </tr>
            <tr><td><input type="checkbox" name="filing" value="all" onchange='selectAll(this);'>Select All</input></td></tr>
            <%
            ArrayList<FilingLinkInfo> filingLinks = (ArrayList<FilingLinkInfo>) request.getAttribute("filingLinks");
            for (int ii=0; ii<filingLinks.size(); ii++) {
                %>
                <tr>
                <td><input type="checkbox" name="filing" value=<%out.println(filingLinks.get(ii).getLink());%>
                    id=<%out.println(filingLinks.get(ii).getFilingDate());%>>
                    <%out.println(filingLinks.get(ii).getFilingDate());%></input></td>
                </tr>
            <%}%>
        </table>

        <input type="submit" id="updatefiling-button" value="Update Selected Filings"/>
    </form>
    </body>
</html>
