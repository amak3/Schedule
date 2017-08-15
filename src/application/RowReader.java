package application;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class RowReader
{
	public List<Row> parseJsonToJavaObjects() throws IOException 
	{
		URL url = new URL("http://retardo.pl/wmi-timetable-date/schedules.json");
		InputStream openStream = url.openStream();
		
		try (Scanner scanner = new Scanner(openStream, "UTF-8"))
		{
			scanner.useDelimiter("\\A");
			String jsonString = scanner.next();
			Type listType = new TypeToken<List<Row>>(){}.getType();
			Gson gson = new GsonBuilder().create();
			List<Row> list = gson.fromJson(jsonString, listType);
			return list;
		}		
	}
}
