import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Foo {
	
//	**** Heures d'ensoleillement ****
//	Heures d'ensoleillement: 8h
//	**** Lever / Coucher du soleil ****
//	<div> 
//	 <span class="glyph winddir S"></span>06:37
//	 <br> 
//	 <span class="glyph winddir N"></span>21:12 
//	</div>
//	**** Pression atmosphérique ****
//	Pression: 1020 hpa
	
	

	public static void main(String[] args)
	  {
	    Matcher m = extract();
	 
	    // if we find a match, get the group 
	    if (m.find())
	    {
	      // we're only looking for one group, so get it
	      String theGroup = m.group(1);
	       
	      // print the group out for verification
	      System.out.format("'%s'\n", theGroup);
	    }
	 
	  }


	
	private static Matcher extract() {
		String stringToSearch = "Dernière mise à jour: 11/08/2015 09:51 CEST";
	 
		// the pattern we want to search for
		Pattern p = Pattern.compile("Dernière mise à jour: .* (\\d{2}:\\d{2}) CEST");
		Matcher m = p.matcher(stringToSearch);
		return m;
	}
	


}
