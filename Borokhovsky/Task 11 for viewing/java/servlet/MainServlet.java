package servlet;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MainServlet extends HttpServlet {
    String table = "";

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        resp.setCharacterEncoding("utf8");
        pw.println("<!DOCTYPE html>\n" + "<html>\n" + "<head lang=\"en\">\n" + "    <meta charset=\"UTF-8\">\n"
                + "    <title>Main Page</title>\n" + "</head>\n" + "<body>\n" + "\n"
                + "<center><div style='font-size:20px'>Table Generator</div></center>"
                + "<form action=\"/drawtable.do\" method=\"post\">\n"
                + "    <label><input name=\"tableHeader\" > Title</label><br/><br/>\n"
                + "    <label><input name=\"tableColor\"> Color (in html format)</label><br/><br/>\n"
                + "    <label><input name=\"tableCols\"> Cols</label><br/><br/>\n"
                + "    <label><input name=\"tableRows\"> Rows</label><br/><br/>\n"
                + "    <input type=\"submit\" value=\"Submit\"/>\n" + "</form>\n<br/>"+ table + "\n" + "</body>\n" + "</html>");
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int tCols = 0;
        int tRows = 0;
        String title = req.getParameter("tableHeader");
        String color = req.getParameter("tableColor");
        String cols = req.getParameter("tableCols");
        String rows = req.getParameter("tableRows");
        if(!color.matches("#[a-f0-9]{6}")){
            table = "<p color='red'><div style='font-color:red;'>Error with parsing color</div></p>";
            resp.sendRedirect(resp.encodeRedirectURL("/"));
            return;
        }
        try{
            tCols = Integer.parseInt(cols);
            tRows = Integer.parseInt(rows);
        } catch (Exception e){
            table = "<p color='red'><div style='font-color:red;'>Error with parsing numbers</div></p>";
            resp.sendRedirect(resp.encodeRedirectURL("/"));
            return;
        }
        table = "";
        table += "<p>"+title+"</p>\n<table border='1' bgcolor='"+color+"'>\n";
        for(int i = 0 ; i < tRows ; i++){
            table += "<tr>\n";
            for(int j = 0 ; j < tCols ; j++){
                table+="<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n";
            }
            table += "</tr>\n";
        }
        table += "</table>\n";
        resp.sendRedirect(resp.encodeRedirectURL("/"));
    }
}