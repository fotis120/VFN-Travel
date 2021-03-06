package helpFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dao.ListingDAO;
import dao.ListingDAOI;
import dao.UserDAO;
import dao.UserDAOI;
import jpautils.EntityManagerHelper;
import model.Listing;
import model.ListingPK;
import model.User;

import java.util.HashMap;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    public static void parsing (String csvFile) throws Exception {

    	/*BufferedReader scanner = null;
        scanner = new BufferedReader(new FileReader(new File(csvFile)));
        String something;
        int count = 0;
       // Scanner scanner = new Scanner(new File(csvFile));
        scanner.readLine();
        EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createQuery("SELECT MAX(l.id.idlisting) FROM Listing l");
		int idPK = (int)query.getSingleResult() + 1;
		HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();
        while ((something = scanner.readLine()) != null && count < 5) {
            //List<String> line = parseLine(scanner.nextLine());
           
           // System.out.println(something);
        	List<String> line = parseLine(something);
        	UserDAOI dao = new UserDAO();
    		User user = new User();
    		user.setUsername(line.get(3));
    		user.setName(line.get(4));
    		user.setCity(line.get(5));
    		user.setTrn("123456789");
    		user.setType(2);
    		dao.create(user);
    		dao.findByUsername(line.get(3));
        	
        	
            ListingDAOI daoo = new ListingDAO();
        	Listing listing = new Listing();
        	ListingPK lpk = new ListingPK();
        	listing.setUser(user);
        	listing.setId(lpk);
        	listing.setPhoto1("1");
        	int id = Integer.parseInt(line.get(0));
        	hmap.put(id, idPK);
        	++idPK;
        	listing.setName(line.get(1));
        	listing.setDescription(line.get(2));
        	listing.setCity(line.get(7));
        	listing.setArea(line.get(6));
        	listing.setLatitude(Float.parseFloat(line.get(9)));
        	listing.setLongtitude(Float.parseFloat(line.get(10)));
        	
        	//listing.setDailyPrice(Integer.parseInt(line.get(19)));
        	if (!line.get(13).isEmpty() && !line.get(13).equals(""))
        		listing.setMaxPeople((int)Float.parseFloat(line.get(13)));
        	if (!line.get(14).isEmpty() && !line.get(14).equals(""))
        		listing.setBathsNumber((int)Float.parseFloat(line.get(14)));
        	if (!line.get(15).isEmpty() && !line.get(15).equals(""))
        		listing.setRoomsNumber((int)Float.parseFloat(line.get(15)));
        	if (!line.get(16).isEmpty() && !line.get(16).equals(""))
        		listing.setBedsNumber((int)Float.parseFloat(line.get(16)));
        	if (!line.get(17).isEmpty() && !line.get(18).equals(""))
        		listing.setSize((int)Float.parseFloat(line.get(18)));
        	
        	daoo.create(listing);
            System.out.println(count + " " + line.size());
            System.out.println("Country [id= " + id + " " + idPK + " " + user.getUsername()   + " "+ line.get(3) +  " " + line.get(4) + " " + listing.getArea() + " " + listing.getCity() + " " + listing.getLatitude() + " " + listing.getBedsNumber()) ;
            ++count;
        }
        scanner.close();*/

    }
   /* List<String> line = parseLine(something);
    ListingDAOI daoo = new ListingDAO();
	Listing listing = new Listing();
	ListingPK lpk = new ListingPK();
	listing.setName(line.get(1));
	listing.setDescription(line.get(2));*/

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

}
