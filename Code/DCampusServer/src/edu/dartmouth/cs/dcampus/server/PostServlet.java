package edu.dartmouth.cs.dcampus.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

import edu.dartmouth.cs.dcampus.server.data.PostDatastore;
import edu.dartmouth.cs.dcampus.server.data.PostEntity;


public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String name = null ;
		String desc = null;
		long id = 0;
		double lat = 0;
		double lng = 0;
		JSONObject jsonObj = null;
		int rank = 1;
		String voice =null;
		String  bitmap = null;
		// lack of pic voice !!!
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				   req.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        String data = sb.toString().split("=")[1];
        
        try{
     	   jsonObj = new JSONObject(data);
     	  id = jsonObj.getLong(PostEntity.FIELD_NAME_ID);
     	  name = jsonObj.getString(PostEntity.FIELD_NAME_NAME);
     	 lat = jsonObj.getDouble(PostEntity.FIELD_NAME_LAT);
     	lng = jsonObj.getDouble(PostEntity.FIELD_NAME_LNG);
     	rank = jsonObj.getInt(PostEntity.FIELD_NAME_RANKING);
     	desc = jsonObj.getString(PostEntity.FIELD_NAME_DESC);
     	bitmap = jsonObj.getString(PostEntity.FIELD_NAME_BITMAP);
     	voice = jsonObj.getString(PostEntity.FIELD_NAME_VOICE);
     	
     	
     	
     	
     	//name = jsonObj.getString(PostEntity.FIELD_NAME_NAME);
        }catch(Exception e){
        	e.printStackTrace();
        	
        }
		
		PostEntity entity = new PostEntity(new Random().nextInt(2000)+id, name, lat, lng, bitmap, rank, desc, voice);

		PostDatastore.add(entity);

		resp.sendRedirect("/query.do");
//		getServletContext().getRequestDispatcher("/test2.jsp").forward(req, resp);
		// notify devices
		// more than one device, add this part again
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

