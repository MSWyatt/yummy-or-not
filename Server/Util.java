package com.yummyornot;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

public class Util {
	/**
	 * Escapes or removes open angle bracket, apostrophe and quote, backslash,
	 * and carriage return.
	 */
	public static String clean(String str) {
		return str.replaceAll("<", "&lt;").replaceAll("'", "&apos;")
				.replaceAll("\"", "&quot;").replaceAll("\\\\", "")
				.replaceAll("\r", " ").replaceAll("\n", " ");
	}

	/**
	 * Parses an http request's parameters and converts them to a Map. If a form
	 * field named "nm" has a single string value, you can get it from the map
	 * using key "nm". If it has several string values, you can get a List of
	 * the String values using key "nm[]" If it has a single binary file value,
	 * you can get the byte[] from the map using key "nm[]" Note that the
	 * request is consumed by this method. This method uses commons fileupload.
	 */
	public static Map<String, Object> read(HttpServletRequest request) {
		Map<String, Object> rv = new HashMap<String, Object>();
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				ServletFileUpload upload = new ServletFileUpload();

				FileItemIterator iterator = upload.getItemIterator(request);

				while (iterator.hasNext()) {
					FileItemStream item = iterator.next();
					InputStream stream = item.openStream();
					String name = item.getFieldName();

					if (item.isFormField()) {
						String value = Streams.asString(stream);
						rv.put(name, value);
						@SuppressWarnings("unchecked")
						List<String> values = (List<String>) rv
								.get(name + "[]");
						if (values == null) {
							values = new ArrayList<String>();
							rv.put(name + "[]", values);
						}
						values.add(value);
					} else {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();

						int len;
						byte[] buffer = new byte[2048];
						while ((len = stream.read(buffer, 0, buffer.length)) > 0)
							bos.write(buffer, 0, len);

						rv.put(name, item.getName());
						rv.put(name + "[]", bos.toByteArray());
					}
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			@SuppressWarnings("rawtypes")
			Enumeration params = request.getParameterNames();
			while (params.hasMoreElements()) {
				String key = params.nextElement().toString();
				String[] values = request.getParameterValues(key);
				if (values != null && values.length > 0)
					rv.put(key, values[0]);
				rv.put(key + "[]", Arrays.asList(values));
			}

		}
		return rv;
	}

}
