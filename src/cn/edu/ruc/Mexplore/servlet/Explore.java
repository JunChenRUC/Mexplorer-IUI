package cn.edu.ruc.Mexplore.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.ruc.Mexplore.domain.Path;
import cn.edu.ruc.Mexplore.domain.Result;
import cn.edu.ruc.Mexplore.run.Process;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class Explore
 */
@WebServlet("/Explore")
public class Explore extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Process process = new Process();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Explore() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out= response.getWriter();

        long beginTime = System.currentTimeMillis();
        
        int queryType = 0;
        if(request.getParameter("type") != null)
        	queryType = Integer.parseInt(request.getParameter("type"));
        System.out.println("qeury type: " + queryType);
		
		if(queryType == 0){
			String queryString = "";
	        if(request.getParameter("entity") != null)
	        	queryString = request.getParameter("entity");
	        
	        Result result = null;
			
	        result = process.getResult(queryString);
	        
			JSONArray jsonArray = JSONArray.fromObject(result);
	        out.println(jsonArray.toString()); 
			System.out.println(jsonArray.toString());
		}
		else if(queryType == 1){
			ArrayList<Path> pathList = null;
			
			String headName = null;
			if(request.getParameter("head") != null)
				headName = request.getParameter("head");
	        String tailName = null;
	        if(request.getParameter("tail") != null)
	        	tailName = request.getParameter("tail");
			
			pathList = process.getPath(headName, tailName);
			
	        JSONArray jsonArray = JSONArray.fromObject(pathList);
	        out.println(jsonArray.toString()); 
			System.out.println(jsonArray.toString());
		}
		
		System.out.println("Time cost: " + (System.currentTimeMillis() - beginTime)/1000);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
