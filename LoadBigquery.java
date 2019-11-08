package Backend;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.DatasetId;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableId;

import DataModel.UserDataFields;

public class LoadBigquery {
	
	static String PROJECT_ID = "import2bq";
	static String DATASET_NAME = "Anuj";
	static String TABLE_NAME = "Users";
	static String DATASTORE_NAME = "User";
	static String PRIMARY_KEY = "Email";
	static BigQuery bigquery = BigQueryOptions.newBuilder()
			  .setProjectId(PROJECT_ID)
			  .build().getService();

	public static String start() 
	{
		DatasetId datasetId = DatasetId.of(PROJECT_ID, DATASET_NAME);
		Dataset dataset = bigquery.getDataset(datasetId);
		if(dataset == null)
		{
			boolean status = CreateDatasetTable.createDataset(DATASET_NAME);
			if(!status)
				return "Some Exception occured while creating Bigquery Dataset";
		}
		TableId tableId = TableId.of(PROJECT_ID, DATASET_NAME, TABLE_NAME);
		Table table = bigquery.getTable(tableId);
		if(table == null)
			CreateDatasetTable.createTable(DATASET_NAME, TABLE_NAME);
		boolean status = loadData();
		if(!status)
			return "Some Exception occured while loading data to Bigquery (QueryError)";
		return "The Migration was completed successfully...!";
	}
	
	public static boolean loadData() 
	{
		boolean status = true;
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(DATASTORE_NAME);
		PreparedQuery pq = ds.prepare(q);
		for(Entity user:pq.asIterable())
		{
			String query = "insert into `"+PROJECT_ID+"."+DATASET_NAME+"."+TABLE_NAME+"` \n"
		        	+ "values(";
			UserDataFields obj = new UserDataFields();
			for (java.lang.reflect.Field fd : obj.getClass().getDeclaredFields()) 
			{
				if(!user.hasProperty(fd.getName()))
					query=query+"null,";
				else {
					if(fd.getType().getSimpleName().equals("Date")){
						Date date = (Date) (user.getProperty(fd.getName()));
						if(date == null)
							query=query+"null,";
						else {
			    				LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
			    				query=query+"date("+ldt.getYear()+","+ldt.getMonth().getValue()+","+ldt.getDayOfMonth()+"),";
						}
					}
					else if(fd.getType().getSimpleName().equals("String"))
			    			query=query+"\""+user.getProperty(fd.getName())+"\",";
					else
						query=query+user.getProperty(fd.getName())+",";
				}
			}
			query=query.substring(0, query.length() - 1)+");";
			QueryJobConfiguration queryConfig =
				QueryJobConfiguration.newBuilder(query).build();
			try {
				bigquery.query(queryConfig);
			} catch (JobException | InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = false;
			}
		}
		return status;
	}
}