
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * @author Aneesh Ashutosh
 * @date 11/20/14
 * 
 * The API handler. This class can download data from the Internet.
 */

public class APIHandler
{
	public APIHandler()
	{
		super();
	}

	public String get(String URL)
	{
		String result = "";
		try
		{
			//Opens up a HTTP connection
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			System.out.println(url.toString());
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
			//Writes the response to a string
			String line;
			while ((line = rd.readLine()) != null)
			{
				result += line;
			}
			rd.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
