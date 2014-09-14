package edu.dartmouth.cs.dcampus.server.data;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PostDatastore {
	public static long curID = 1000; 
	
	private static final DatastoreService mDatastore = DatastoreServiceFactory
			.getDatastoreService();

	private static Key getParentKey() {
		return KeyFactory.createKey(PostEntity.ENTITY_KIND_PARENT,
				PostEntity.ENTITY_PARENT_KEY);
	}

	private static void createParentEntity() {
		Entity entity = new Entity(getParentKey());

		mDatastore.put(entity);
	}

	public static boolean add(PostEntity post) {
		Key parentKey = getParentKey();
		try {
			mDatastore.get(parentKey);
		} catch (Exception ex) {
			createParentEntity();
		}

		Entity entity = new Entity(PostEntity.ENTITY_KIND_POST,
				post.mId, parentKey);

		entity.setProperty(PostEntity.FIELD_NAME_ID, post.mId);
		entity.setProperty(PostEntity.FIELD_NAME_NAME, post.mName);
		entity.setProperty(PostEntity.FIELD_NAME_LAT, post.mLat);
		entity.setProperty(PostEntity.FIELD_NAME_LNG, post.mLng);
		entity.setProperty(PostEntity.FIELD_NAME_BITMAP, post.mPic);
		entity.setProperty(PostEntity.FIELD_NAME_RANKING, post.mRank);
		entity.setProperty(PostEntity.FIELD_NAME_DESC, post.mDesc);
		entity.setProperty(PostEntity.FIELD_NAME_VOICE, post.mVoice);
		
		mDatastore.put(entity);

		return true;
	}

	public static ArrayList<PostEntity> query() {
		ArrayList<PostEntity> resultList = new ArrayList<PostEntity>();

		Query query = new Query(PostEntity.ENTITY_KIND_POST);
		query.setFilter(null);
		query.setAncestor(getParentKey());
		query.addSort(PostEntity.FIELD_NAME_ID, SortDirection.ASCENDING);
		PreparedQuery pq = mDatastore.prepare(query);

		for (Entity entity : pq.asIterable()) {
			PostEntity post = new PostEntity();
			post.mId = (Long)entity.getProperty(PostEntity.FIELD_NAME_ID);
			post.mName = (String)entity.getProperty(PostEntity.FIELD_NAME_NAME);
			post.mLat = (double)entity.getProperty(PostEntity.FIELD_NAME_LAT);
			post.mLng = (double)entity.getProperty(PostEntity.FIELD_NAME_LNG);
			post.mPic = (String)entity.getProperty(PostEntity.FIELD_NAME_BITMAP);
			post.mRank = ((Long)entity.getProperty(PostEntity.FIELD_NAME_RANKING)).intValue();
			post.mDesc = (String)entity.getProperty(PostEntity.FIELD_NAME_DESC);
			post.mVoice = (String)entity.getProperty(PostEntity.FIELD_NAME_VOICE);
			
			resultList.add(post);
		}
		return resultList;
	}
	
	public static boolean delete(long id) {

		// query
		Filter filter = new FilterPredicate(PostEntity.FIELD_NAME_ID,
				FilterOperator.EQUAL, id);

		Query query = new Query(PostEntity.ENTITY_KIND_POST);
		query.setFilter(filter);

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = mDatastore.prepare(query);

		Entity result = pq.asSingleEntity();
		boolean ret = false;
		if (result != null) {
			// delete
			mDatastore.delete(result.getKey());
			ret = true;
		}

		return ret;		
	}

}
