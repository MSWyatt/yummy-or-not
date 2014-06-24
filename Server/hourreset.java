package com.yummyornot;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


public class hourreset extends HttpServlet 
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		handleRequest(resp);
	}
			
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		handleRequest(resp);
	}
	
	public void handleRequest(HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/html");
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Item");
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) 
		{
			Long id = (Long) result.getKey().getId();
			Item item = (Item)pm.getObjectById(Item.class, id);
			item.resetHour();
			pm.makePersistent(item);
		}
		pm.close();
	}		
}