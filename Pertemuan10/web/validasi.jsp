<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="javax.servlet.http.Cookie"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Materi Session dan Cookie</title>
    </head>
    <body>
        <%
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Format waktu
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

            // Cek apakah sudah ada session
            String userLogin    = (String) session.getAttribute("userLogin");
            String waktuLogin   = (String) session.getAttribute("waktuLogin");

            if (userLogin == null) {
                // Belum ada session — proses login
                if (username != null && username.equals("ADMIN") && password != null && password.equals("ADMIN")) {
                    // Login berhasil
                    String waktu = sdf.format(new Date());

                    // Simpan ke session
                    session.setAttribute("userLogin",  username);
                    session.setAttribute("waktuLogin", waktu);
                    session.setMaxInactiveInterval(60 * 60);

                    // Simpan cookies
                    Cookie cNama  = new Cookie("nama", username);
                    Cookie cWaktu = new Cookie("waktuLogin", waktu);
                    cNama.setMaxAge(60 * 60 * 24);
                    cWaktu.setMaxAge(60 * 60 * 24);
                    response.addCookie(cNama);
                    response.addCookie(cWaktu);

                    userLogin  = username;
                    waktuLogin = waktu;
                } else {
                    // Login gagal
        %>
                    <p>Username atau password salah!</p>
                    <a href="index.jsp">Coba Lagi</a>
        <%
                    return;
                }
            }

            // Tampilkan semua cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
        %>
                    Data cookie ke-<%= i %> nama: <%= cookies[i].getName() %><br/>
                    Data cookie ke-<%= i %> data: <%= cookies[i].getValue() %><br/>
        <%
                }
            }
        %>

        <h1>Belajar Cookies dan Session</h1>
        <br/>
        Sudah login dengan nama: <b><%= userLogin %></b><br/>
        Waktu login: <%= waktuLogin %><br/>
        Waktu saat ini: <%= sdf.format(new Date()) %><br/>
        <br/>
        <a href="index.jsp">Logout / Kembali</a>

    </body>
</html>
