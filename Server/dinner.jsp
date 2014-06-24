<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Rate My Dinner!</title>
		<script src="jquery-1.8.2.min.js"></script>
		<style>
			.table {
				align: center;
				border: 5px solid black;
				padding: 0px;
				border-spacing: 0px;
				background-color: #EEEEEE;
			}
			
			.red {
				color: red;
				font-style: italic;
			}
			
			.tabletitle {
				background-color: blue;
				color: white;
				text-align: center;
			}
			
			.uform {
				background-color: #CCCCCC;
				color: black;
				border: 5px solid black;
			}
			
.rating{
	width:80px;
	height:16px;
	margin:0 0 20px 0;
	padding:0;
	list-style:none;
	clear:both;
	position:relative;
	background: url(star-matrix.gif) no-repeat 0 0;
}
/* add these classes to the ul to effect the change to the correct number of stars */
.nostar {background-position:0 0}
.onestar {background-position:0 -16px}
.twostar {background-position:0 -32px}
.threestar {background-position:0 -48px}
.fourstar {background-position:0 -64px}
.fivestar {background-position:0 -80px}
ul.rating li {
	cursor: pointer;
 /*ie5 mac doesn't like it if the list is floated\*/
	float:left;
	/* end hide*/
	text-indent:-999em;
}
ul.rating li a {
	position:absolute;
	left:0;
	top:0;
	width:16px;
	height:16px;
	text-decoration:none;
	z-index: 200;
}
ul.rating li.one a {left:0}
ul.rating li.two a {left:16px;}
ul.rating li.three a {left:32px;}
ul.rating li.four a {left:48px;}
ul.rating li.five a {left:64px;}
ul.rating li a:hover {
	z-index:2;
	width:80px;
	height:16px;
	overflow:hidden;
	left:0;	
	background: url(star-matrix.gif) no-repeat 0 0
}
ul.rating li.one a:hover {background-position:0 -96px;}
ul.rating li.two a:hover {background-position:0 -112px;}
ul.rating li.three a:hover {background-position:0 -128px}
ul.rating li.four a:hover {background-position:0 -144px}
ul.rating li.five a:hover {background-position:0 -160px}
/* end rating code */
h3{margin:0 0 2px 0;font-size:110%}
		</style>
	</head>
	<body>
		<div>
		<table class="table">
			<tr>
				<td class="tabletitle">Dinner</td>
				<td class="tabletitle">Best Dinners</td>
			</tr>
			<tr>
				<%@ page import="com.yummyornot.*" %>
				<%@ page import="org.apache.commons.lang3.*" %>
				<%@ page import="java.io.*" %>
				<%@ page import="com.google.appengine.api.datastore.*" %>
				<%@ page import="com.google.appengine.api.*" %>
				<%@ page import="java.lang.Comparable" %>
				<%@ page import="java.util.List" %>
				<%@ page import="javax.jdo.PersistenceManager" %>
				<%
					PersistenceManager pm = PMF.getPMF().getPersistenceManager();
					
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					Query q = new Query("Item");
					PreparedQuery pq = datastore.prepare(q);
					int i=0;
					for (Entity result : pq.asIterable()) 
					{
						i++;
					}	
						int dinner = (int) Math.floor(Math.random() * i);
						i = 0;
						for (Entity result : pq.asIterable())
						{
							if(i == dinner)
							{	
								Long id = (Long) result.getKey().getId();
								out.println("<td>Dinner #" + dinner + "<br><img style=\"max-height: 450px; max-width: 450px;\" alt=\"Rate this dinner!\" src=\"viewimage.jsp?dinner=" + id + "\">");
								out.println("<ul class=\"rating threestar\">");
								out.println("<li class=\"one\"><a href=\"vote.jsp?id=" + id + "&amp;stars=1\" title=\"1 Star\">1</a></li>");
								out.println("<li class=\"two\"><a href=\"vote.jsp?id=" + id + "&amp;stars=2\" title=\"2 Stars\">1</a></li>");
								out.println("<li class=\"three\"><a href=\"vote.jsp?id=" + id + "&amp;stars=3\" title=\"3 Stars\">1</a></li>");
								out.println("<li class=\"four\"><a href=\"vote.jsp?id=" + id + "&amp;stars=4\" title=\"4 Stars\">1</a></li>");
								out.println("<li class=\"five\"><a href=\"vote.jsp?id=" + id + "&amp;stars=5\" title=\"5 Stars\">1</a></li>");
								out.println("</td>");
							}
						i++;
						}
					out.println("<td><table><tr><td>Best dinner of the last hour:<br>");
					q = new Query("Item")
						.addSort("hour_average", Query.SortDirection.DESCENDING);
					pq = datastore.prepare(q);
					i = 0;
					for (Entity result : pq.asIterable())
					{
						if(i == 0)
							out.println("<tr><td><img style=\"max-height:200px; max-width:200px;\" src=\"viewimage.jsp?dinner=" + result.getKey().getId() + "\"><br><center>An average of " + result.getProperty("hour_average") +" stars!</center></td></tr>");
						i++;
					}
										out.println("<tr><td>Best dinner of ALL TIME:<br>");
					q = new Query("Item")
						.addSort("average", Query.SortDirection.DESCENDING);
					pq = datastore.prepare(q);
					i = 0;
					for (Entity result : pq.asIterable())
					{
						if(i == 0)
							out.println("<tr><td><img style=\"max-height:200px; max-width:200px;\" src=\"viewimage.jsp?dinner=" + result.getKey().getId() + "\"><br><center>An average of " + result.getProperty("average") +" stars!</center></td></tr>");
						i++;
					}
					
					
					out.println("</table></td>");
				%>
			</tr>
		</table>
		</div>
		<p>
		<form method="post" enctype="multipart/form-data" action="dinner.jsp">
		<table class="uform">
			<tr>
				<td>Add a Dinner</td>
			</tr>
			<tr>
				<td class="red" id="error" colspan=2></td>
			</tr>
			<tr>
				<td>Dinner:</td>
				<td><input name="photo" type="file"></td>
			</tr>
			<tr>
				<td><input type="submit"></td>
			</tr>
		</table>
		</form>
		<%@ page import="java.util.*" %>
		<%@ page import="javax.jdo.PersistenceManager" %>
		<%
			pm = PMF.getPMF().getPersistenceManager();
			try {
			Map<String, Object> data = Util.read(request);
			byte[] photo = (byte[])data.get("photo[]");
			String photoName = (String)data.get("photo");
		
			if (photo == null) photo = new byte[0];

			String errorMessage = null;
		
			if (photo.length > 1048576) 
			{errorMessage = "File is too large for Google Application Engine";}
			else if (photo.length > 0 && !photoName.toLowerCase().endsWith(".jpg")) 
			{errorMessage = ".jpg files only";} 
			else if (photo.length > 0) 
			{
				Item item = new Item();
				item.setPhoto(photo);
				item.newPhoto(); 
				pm.makePersistent(item);
			}
			out.write("<script>");
				if (errorMessage != null) 
				{	
					out.write("$('#error').text('"+Util.clean(errorMessage)+"');");
				}
			out.write("</script>");
		
		} finally {
			pm.close();
		}
%>
	</body>
</html>