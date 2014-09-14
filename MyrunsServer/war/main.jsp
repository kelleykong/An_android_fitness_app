<%@page import="com.example.lab6.data.PostEntity"%>
<%@page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Post</title>
</head>
<body>
	<h1>Exercise Entries for your devide:</h1>
	<table border = 1px>
		<tr>
			<td> id </td>
			<td> inputType </td>
			<td> activityType </td>
			<td> dateTime </td>
			<td> duration </td>
			<td> distance </td>
			<td> avgSpeed </td>
			<td> calorie </td>
			<td> climb </td>
			<td> heartrate </td>
			<td> comment </td>
			<td>  </td>
		</tr>
		<%
					ArrayList<PostEntity> postList = (ArrayList<PostEntity>) request.getAttribute("postList"); 
					DecimalFormat df = new DecimalFormat("0.00");
					for (PostEntity post : postList) {
		%>
		<tr>
			<td> <%=post.id%> </td>
			<td> <%=post.mInputType%> </td>
			<td> <%=post.mActivityType%> </td>
			<td> <%=post.mDateTime%> </td>
			<td> <%=post.mDuration%> secs</td>
			<td> <%=df.format(post.mDistance)%> Miles</td>
			<td> <%=df.format(post.mAvgSpeed)%> </td>
			<td> <%=post.mCalorie%> </td>
			<td> <%=df.format(post.mClimb)%> </td>
			<td> <%=post.mHeartRate%> </td>
			<td>  </td>
			<td> 			
				<form name="delete" action="/delete.do" method="post">
					<input type="hidden" name="id" value=<%=post.id%>>
					<input type="submit" value="delete"> 
				</form>
			</td>
		</tr>
		<%
					}
		%>
	</table>
				

				<form name="input" action="/post.do" method="post">
					<input type="text" name="post_text"> <input type="submit"
						value="Post">
				</form>


</body>
</html>
