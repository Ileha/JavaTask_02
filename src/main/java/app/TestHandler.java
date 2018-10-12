package app;

import app.Singleton.CommandBagSingleton;
import app.Singleton.SQLiteSingleton;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.*;
import app.Singleton.Exception.SinletoneException;
import org.json.*;

public class TestHandler extends HttpServlet {
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("text/html;charset=utf-8");

        PrintWriter pw = httpServletResponse.getWriter();
        pw.println("<H1>Hello from servlet!</H1></br>");

        ServletContext sc = httpServletRequest.getServletContext();
        try {
            pw.printf("DB Path is: %s</br>", SQLiteSingleton.getInstance().GetResoursePath());
            SQLiteSingleton.getInstance().getAllFromFolder(1, pw);
        } catch (SinletoneException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
//        Scanner in = null;
//        PrintWriter pw = httpServletResponse.getWriter();

//        BufferedReader rd = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream()));
//        StringBuilder res = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            res.append(line);
//        }
//        rd.close();
//
//        try {
//            JSONObject obj = new JSONObject(res.toString());
//            String pageName = obj.getString("cmd");
//            JSONObject data = obj.getJSONObject("data");
//            CommandBagSingleton.getInstance().GetCommand(pageName).Execute(data, pw);
//        } catch (Exception e) {
//            pw.println(e.toString());
//        }


//        for (Part p:httpServletRequest.getParts()) {
//            in = new Scanner(p.getInputStream());
//            pw.println("new</br>");
//            while (in.hasNextLine()) {
//                pw.printf("%s</br>", in.nextLine());
//            }
//        }
    }
}