package com.example.lab6.data;

import java.util.ArrayList;
import java.util.Date;

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
		// if exist, delete
		delete(post.id);
		
		// add post
		Key parentKey = getParentKey();
		try {
			mDatastore.get(parentKey);
		} catch (Exception ex) {
			createParentEntity();
		}

		Entity entity = new Entity(PostEntity.ENTITY_KIND_POST,
				post.id, parentKey);

		entity.setProperty(PostEntity.FIELD_NAME_ID, post.id);
		entity.setProperty(PostEntity.FIELD_NAME_INPUTTYPE, new Integer(post.mInputType));
		entity.setProperty(PostEntity.FIELD_NAME_ACTIVITYTYPE, new Integer(post.mActivityType));
		entity.setProperty(PostEntity.FIELD_NAME_DATE, post.mDateTime);
		entity.setProperty(PostEntity.FIELD_NAME_DURATION, new Integer(post.mDuration));
		entity.setProperty(PostEntity.FIELD_NAME_DISTANCE, new Double(post.mDistance));
		entity.setProperty(PostEntity.FIELD_NAME_AVGSPEED, new Double(post.mAvgSpeed));
		entity.setProperty(PostEntity.FIELD_NAME_CALORIE, new Integer(post.mCalorie));
		entity.setProperty(PostEntity.FIELD_NAME_CLIMB, new Double(post.mClimb));
		entity.setProperty(PostEntity.FIELD_NAME_HEARTRATE, new Integer(post.mHeartRate));
		entity.setProperty(PostEntity.FIELD_NAME_POST, post.mPostString);

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
			post.id = (Long)entity.getProperty(PostEntity.FIELD_NAME_ID);
			post.mInputType = ((Long)entity.getProperty(PostEntity.FIELD_NAME_INPUTTYPE)).intValue();
			post.mActivityType = ((Long)entity.getProperty(PostEntity.FIELD_NAME_ACTIVITYTYPE)).intValue();
			post.mDateTime = (String)entity.getProperty(PostEntity.FIELD_NAME_DATE);
			post.mDuration = ((Long)entity.getProperty(PostEntity.FIELD_NAME_DURATION)).intValue();
			post.mDistance = (Double)entity.getProperty(PostEntity.FIELD_NAME_DISTANCE);
			post.mAvgSpeed = (Double)entity.getProperty(PostEntity.FIELD_NAME_AVGSPEED);
			post.mCalorie = ((Long)entity.getProperty(PostEntity.FIELD_NAME_CALORIE)).intValue();
			post.mClimb = (Double)entity.getProperty(PostEntity.FIELD_NAME_CLIMB);
			post.mHeartRate = ((Long)entity.getProperty(PostEntity.FIELD_NAME_HEARTRATE)).intValue();
			post.mPostString = (String)entity.getProperty(PostEntity.FIELD_NAME_POST);
			
			resultList.add(post);
		}
		return resultList;
	}
	
	public static int queryNumber() {
		ArrayList<PostEntity> array = query();
		if (array.size() == 0)
			return 1;
		return (int) array.get(array.size()-1).id + 1;
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
