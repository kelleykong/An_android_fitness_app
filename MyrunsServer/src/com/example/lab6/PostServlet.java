package com.example.lab6;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.lab6.data.PostDatastore;
import com.example.lab6.data.PostEntity;


public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String postText = req.getParameter("post_text");
		String mDisplayDateTime = req.getParameter(PostEntity.FIELD_NAME_DATE);
		Long id = Long.parseLong(req.getParameter(PostEntity.FIELD_NAME_ID));
		
/*		Date now = new Date();
		String[] time = now.toString().split(" ");
		String mDisplayDateTime = time[3]+" " + time[1] +" " + time[2] + " " + time[5];
*/
		PostEntity entity = new PostEntity(id, postText, mDisplayDateTime);
//		entity.setValues(0, 0, mDisplayDateTime, 0, 0);
//		entity.setValues2(0, 0, 0);
		entity.setValues(Integer.parseInt(req.getParameter(PostEntity.FIELD_NAME_INPUTTYPE)), 
				Integer.parseInt(req.getParameter(PostEntity.FIELD_NAME_ACTIVITYTYPE)), mDisplayDateTime, 
				Integer.parseInt(req.getParameter(PostEntity.FIELD_NAME_DURATION)), (float)Double.parseDouble(req.getParameter(PostEntity.FIELD_NAME_DISTANCE)));
		entity.setValues2(Double.parseDouble(req.getParameter(PostEntity.FIELD_NAME_AVGSPEED)), 
				Integer.parseInt(req.getParameter(PostEntity.FIELD_NAME_CALORIE)), Double.parseDouble(req.getParameter(PostEntity.FIELD_NAME_CLIMB)));
		PostDatastore.add(entity);

		// notify
/*		String from = req.getParameter("from");
		System.out.println("add entity to datastore " + from);

		if (from == null || !from.equals("phone")) {
			resp.sendRedirect("/sendmsg.do");
		}*/
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}
}
