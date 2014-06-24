package com.yummyornot;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Object;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import javax.xml.bind.*;

@MultipartConfig
public class ImgUpload extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    	
    	String incoming = req.getParameter("photo");
    	byte[] photo = DatatypeConverter.parseBase64Binary(incoming);
    	  	
    	PersistenceManager pm = PMF.getPMF().getPersistenceManager();
    	Item item = new Item();
    	item.setPhoto(photo);
    	item.newPhoto();
    	pm.makePersistent(item);
    	pm.close();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();
        out.print("Image saved.");
 
    }
}