package com.example.lab6;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.lab6.data.PostDatastore;
import com.example.lab6.data.PostEntity;


public class DeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		long id = Long.parseLong(req.getParameter(PostEntity.FIELD_NAME_ID));
		PostDatastore.delete(id);

		// notify
		String from = req.getParameter("from");
		System.out.println("delete entity from datastore " + from + req.getParameter("id"));

		if (from == null || !from.equals("phone")) {
			getServletContext().getRequestDispatcher("/sendmsg.do").forward(req, resp);
		}
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}
}
