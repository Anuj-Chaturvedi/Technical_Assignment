package Backend;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.DatasetInfo;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.LegacySQLTypeName;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableDefinition;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableInfo;

import DataModel.UserDataFields;

public class CreateDatasetTable {

	static BigQuery bigquery = BigQueryOptions.newBuilder()
			  .setProjectId("import2bq")
			  .build().getService();

	public static boolean createDataset(String DatasetName) 
	{
		  Dataset dataset = null;
		  DatasetInfo datasetInfo = DatasetInfo.newBuilder(DatasetName).build();
		  try {
		    // the dataset was created
		    dataset = bigquery.create(datasetInfo);
		  } catch (BigQueryException e) {
		    return false;
			  // the dataset was not created
		  }
		  return true;
	}

	public static void createTable(String DatasetName, String TableName) 
	{
		  TableId tableId = TableId.of(DatasetName, TableName);
		// Table field definition
		  List<Field> fields = new ArrayList<Field>();
		  UserDataFields obj = new UserDataFields();
		  Schema schema = null;
		//Field field = Field.of("XYZ", LegacySQLTypeName.STRING);
		  for (java.lang.reflect.Field fd : obj.getClass().getDeclaredFields()) {
     		  fields.add(Field.of(fd.getName(),(getLegacySQLTypeName(fd.getType().getSimpleName()))));     
		  schema = Schema.of(fields.toArray(new Field[fields.size()]));
		  }
		  
		// Table schema definition
		TableDefinition tableDefinition = StandardTableDefinition.of(schema);
		TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
		Table table = bigquery.create(tableInfo);
	}
	
	public static LegacySQLTypeName getLegacySQLTypeName(String type) 
	{
		LegacySQLTypeName result = null;
		if(type.equals("String"))
			  result = LegacySQLTypeName.STRING;
		if(type.equals("int")||type.equals("long"))
			  result = LegacySQLTypeName.INTEGER;
	        if(type.equals("Date"))
			  result = LegacySQLTypeName.DATE;
		if(type.equals("Boolean"))
			  result = LegacySQLTypeName.BOOLEAN;
		if(type.equals("float")||type.equals("double"))
			  result = LegacySQLTypeName.FLOAT;
		return result;
	}
}