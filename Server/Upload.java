package com.yummyornot;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Upload extends HttpServlet {
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String blobUploadUrl = blobstoreService.createUploadUrl("/ImgUpload"); 

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();
        out.print(blobUploadUrl);
    }
}