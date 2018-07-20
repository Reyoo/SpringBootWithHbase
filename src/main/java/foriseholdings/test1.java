package foriseholdings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class test1 {
	// 4 2 false 2018-04-17 10:57:11 onlyRemarkISNull userIDisnull

	public static void main(String[] args) {
		List<String> times = new ArrayList<String>();
		times.add("1523933831000");
		times.add("1523933831500");
		times.add("1523933833361");
		times.add("1523933831400");
		times.add("1523933832330");
		times.add("1523938881361");
		// times.add("");
		// times.add("");
		// times.add("");
		// times.add("");
		// times.add("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

		for (String a : times) {
			System.out.println(sdf.format(Long.parseLong(a)));
		}

	}

}
