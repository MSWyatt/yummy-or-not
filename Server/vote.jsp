<%@ page import="javax.jdo.*" %>
<%@ page import="com.yummyornot.*" %><%
	PersistenceManager pm = PMF.getPMF().getPersistenceManager();
	try {
		out.println("Processing vote.");
		String id = request.getParameter("id");
		String vote = request.getParameter("stars");
		int stars = Integer.parseInt(vote);
		
		Item dinner = (Item)pm.getObjectById(Item.class, Long.parseLong(id));		
		dinner.vote(stars);
		dinner.doAverage();

	} catch (Exception cannotLoad) {	
	} finally {
		pm.close();
		response.sendRedirect("dinner.jsp");
		
	}
%>