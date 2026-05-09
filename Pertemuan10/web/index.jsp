<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="javax.servlet.http.Cookie"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Materi Session dan Cookie</title>
    </head>
    <body>
        <%
            // Cek cookies yang tersimpan
            String savedUser = "";
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("nama")) {
                        savedUser = c.getValue();
                    }
                }
            }
        %>

        <form action="validasi.jsp" method="post">
            <table>
                <tr>
                    <td>User ID</td>
                    <td><input type="text" name="username" value="<%= savedUser %>"/></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="password"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Login"/></td>
                </tr>
            </table>
        </form>

    </body>
</html>
