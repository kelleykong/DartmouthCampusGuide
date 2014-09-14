package edu.dartmouth.cs.dcampus.server.data;


public class PostEntity {
	public static String ENTITY_KIND_PARENT = "PostParent";
	public static String ENTITY_PARENT_KEY = ENTITY_KIND_PARENT;
	public static String ENTITY_KIND_POST = "Post";
	
	public static String FIELD_NAME_ID = "_id";
	public static String FIELD_NAME_NAME = "name";
	public static String FIELD_NAME_LAT = "lat";
	public static String FIELD_NAME_LNG = "lng";
	public static String FIELD_NAME_BITMAP = "bitmap";
	public static String FIELD_NAME_RANKING = "ranking";
	public static String FIELD_NAME_DESC = "desc";
	public static String FIELD_NAME_VOICE = "voice";
	
	public Long mId;
	public String mName;
	public double mLat;
	public double mLng;
	public String mPic;
	public int mRank;
	public String mDesc;
	public String mVoice;


	public PostEntity() {
	}

	public PostEntity(Long id, String name, double lat, double lng, String pic, int rank, String desc, String voice) {
		mId = id;
		mName = name;
		mLat = lat;
		mLng = lng;
		mPic = pic;
		mRank = rank;
		mDesc = desc;
		mVoice = voice;
	}
	
	
}
