<%@ page import="javax.jdo.*" %><%@ page import="com.yummyornot.*" %><%
PersistenceManager pm = PMF.getPMF().getPersistenceManager();
	try {
		String itemId = request.getParameter("dinner");
		
		Item item = (Item)pm.getObjectById(Item.class, Long.parseLong(itemId));
		byte[] photo = item.getPhoto();
		response.setContentType("image/jpeg");
		response.getOutputStream().write(photo);
	} catch (Exception cannotLoad) {	
	} finally {
		pm.close();
	}
%>