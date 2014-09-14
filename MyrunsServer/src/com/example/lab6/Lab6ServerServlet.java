package com.example.lab6;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.example.lab6.data.PostDatastore;
import com.example.lab6.data.PostEntity;


@SuppressWarnings("serial")
public class Lab6ServerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<PostEntity> postList = PostDatastore.query();
		
		req.setAttribute("postList", postList);
		
		getServletContext().getRequestDispatcher("/main.jsp").forward(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}
}
