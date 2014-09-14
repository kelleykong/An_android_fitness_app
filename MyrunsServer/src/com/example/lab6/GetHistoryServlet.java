package com.example.lab6;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.lab6.data.PostDatastore;
import com.example.lab6.data.PostEntity;


public class GetHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<PostEntity> postList = PostDatastore.query();

		PrintWriter out = resp.getWriter();
		for (PostEntity entity : postList) {
			out.append(entity.mDateTime + "    " + entity.mPostString + "\n");
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}

}
