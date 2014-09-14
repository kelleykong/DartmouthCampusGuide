package edu.dartmouth.cs.dcampus.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.dartmouth.cs.dcampus.server.data.PostDatastore;
import edu.dartmouth.cs.dcampus.server.data.PostEntity;


import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.labs.repackaged.org.json.JSONArray;


public class GetHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<PostEntity> postList = PostDatastore.query();
		
		//Get Parent Key
		

		PrintWriter out = resp.getWriter();
		JSONArray array = new JSONArray();
		for (PostEntity entity : postList) {
			//out.append(entity.mPostDate.toString() + "    " + entity.mPostString + "\n");
			//out.append(entity.mName+":"+entity.mDesc+":"+entity.mRank+"\n");
			
			JSONObject obj = new JSONObject();
			//obj.put(MainActivity.PROPERTY_REG_ID, MainActivity.regid);
			try{
			
				
				obj.put(PostEntity.FIELD_NAME_ID, entity.mId);
				obj.put(PostEntity.FIELD_NAME_NAME, entity.mName);
				obj.put(PostEntity.FIELD_NAME_BITMAP, entity.mPic);
				obj.put(PostEntity.FIELD_NAME_RANKING, entity.mRank);
				obj.put(PostEntity.FIELD_NAME_DESC, entity.mDesc);
				obj.put(PostEntity.FIELD_NAME_VOICE, entity.mVoice);
				obj.put(PostEntity.FIELD_NAME_LAT, entity.mLat);
				obj.put(PostEntity.FIELD_NAME_LNG, entity.mLng);
				array.put(obj);
		
				 			
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			//System.out.print("Find one entry\n");
		}
		out.append(array.toString());	
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}

}
