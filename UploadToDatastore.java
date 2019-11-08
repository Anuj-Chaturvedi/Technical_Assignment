package Backend;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

import DataModel.UserDataFields;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class UploadToDatastore {
	
	static String PRIMARY_KEY = "Email";

	public static boolean start(List<FileItem> items) 
	{
		  boolean status=true;
		  try {
			    String saveFile=System.getProperty("java.io.tmpdir");
			    
			    Iterator<FileItem> itr = items.iterator();
			    FileItem item = (FileItem)itr.next();
			    String itemname = item.getName();
			    String filename = FilenameUtils.getName(itemname);
			    File f = new File(saveFile+"/"+filename);
			    item.write(f);
			    status=readExcel(f);
			    f.delete();
		    } 
		    catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			    status = false;
		    }
		    return status;
	}

	public static boolean readExcel(File f) 
	{
		try 
		{
    			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Workbook workBook = Workbook.getWorkbook(f);
			String [] sheetNames = workBook.getSheetNames();
			Sheet sheet=null;
			
			for (int sheetNumber =0; sheetNumber < sheetNames.length; sheetNumber++) 
			{
				sheet=workBook.getSheet(sheetNames[sheetNumber]);
				
				for (int rows=1;rows < sheet.getRows();rows++) 
				{
					if(!sheet.getCell(0, rows).getContents().equals("")) 
					{
					    Entity e = new Entity("User");
				        for (int columns=0;columns < sheet.getColumns();columns++)
				        {
				        	UserDataFields obj = new UserDataFields();
				    		for (java.lang.reflect.Field fd : obj.getClass().getDeclaredFields()) 
				    		{
				    			if(sheet.getCell(columns, 0).getContents().equalsIgnoreCase(fd.getName())) 
				    			{
				    				if(fd.getType().getSimpleName().equals("Date")) 
				    				{
			    						java.util.Date date = new java.util.Date();
			    						date = null;
			    						if(!sheet.getCell(columns, rows).getContents().equals(null)) 
			    						{
			    							try 
			    							{
			    								DateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
			    								date=simpleDateFormat.parse(sheet.getCell(columns, rows).getContents());
			    								e.setProperty(fd.getName(),
													date);
			    							} 
			    							catch (ParseException e1) 
			    							{
			    								DateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			    								try 
			    								{
			    									date=simpleDateFormat.parse(sheet.getCell(columns, rows).getContents());
			    								} 
			    								catch (ParseException e2){}
			    							}
			    						}
										e.setProperty(fd.getName(),
											date);
									}
				    			
				    				else if(fd.getType().getSimpleName().equals("long")) 
				    				{
				    					Long temp = null;
				    					if(!sheet.getCell(columns, rows).getContents().equals("")) 
				    					{
				    						try 
				    						{
				    							temp = Long.parseLong(sheet.getCell(columns, rows).getContents());
				    						}
				    						catch (NumberFormatException e3) {};
				    					}
				    					e.setProperty(fd.getName(),temp);
				    				}
				    				else if(fd.getType().getSimpleName().equals("int")) 
				    				{
				    					Integer temp = null;
				    					if(!sheet.getCell(columns, rows).getContents().equals("")) 
				    					{
				    						try 
				    						{
				    							temp = Integer.parseInt(sheet.getCell(columns, rows).getContents());
				    						}
				    						catch (NumberFormatException e4) {};
				    					}
				    					e.setProperty(fd.getName(),temp);
				    				}
				    				else if(fd.getType().getSimpleName().equals("boolean")) 
				    				{
				    					Boolean temp = null;
				    					if(!sheet.getCell(columns, rows).getContents().equals("")) 
				    						Boolean.parseBoolean(sheet.getCell(columns, rows).getContents());
				    					e.setProperty(fd.getName(),temp);
				    				}
				    				else if(fd.getType().getSimpleName().equals("float")) 
				    				{
				    					Float temp = null;
				    					if(!sheet.getCell(columns, rows).getContents().equals("")) 
				    					{
				    						try 
				    						{
				    							Float.parseFloat(sheet.getCell(columns, rows).getContents());
				    						}
				    						catch (NumberFormatException e5) {};
				    					}
				    					e.setProperty(fd.getName(),temp);
				    				}
				    				else if(fd.getType().getSimpleName().equals("double")) 
				    				{
				    					Double temp = null;
				    					if(!sheet.getCell(columns, rows).getContents().equals("")) 
				    					{
				    						try 
				    						{
				    							Double.parseDouble(sheet.getCell(columns, rows).getContents());
				    						}
				    						catch (NumberFormatException e6) {};
				    					}
				    					e.setProperty(fd.getName(),temp);
				    				}
				    				else
				    					e.setProperty(fd.getName(),
				    							fd.getType().cast(sheet.getCell(columns, rows).getContents()));
				    				break;
				    			}
				    		}    
				    		Filter filter = new Query.FilterPredicate(PRIMARY_KEY, FilterOperator.EQUAL, e.getProperty(PRIMARY_KEY));
				    		Query q = new Query("User").setFilter(filter);
				    		PreparedQuery pq = ds.prepare(q);
				    		boolean found = false;
				    		for(Entity result : pq.asIterable()) 
				    		{
				    			found=true;
				    			if(!result.getProperties().equals(e.getProperties()))
				    			{
				    				ds.delete(result.getKey());
				    				found=false;
				    			}
				    		}
				    		if(!found)
				    			ds.put(e);
				        	}
					}
				}
			}
		}
    		catch (BiffException | IOException e) 
    		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	return true;
    }
}