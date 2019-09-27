package lse;
//@SupressWarnings("unused")
import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
@SuppressWarnings("unused")
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);//hash map of initial capacity of 1000 and load factor of 2.0
		noiseWords = new HashSet<String>(100,2.0f);//same
	}
	
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		HashMap<String,Occurrence> keywords = new HashMap<String,Occurrence>(1000,2.0f);
		 //Scanner input = new Scanner(System.in);
         File file = new File(docFile);
         Scanner sc = new Scanner(file);
         while(sc.hasNext()) {
        	 String line=sc.next();
//        	 System.out.println(line);
        	 if(line==null) {
        		 continue;
        	 }
        	 else if(line!=null)
        	 line=getKeyword(line);
        	 Occurrence op=new Occurrence(null,0);
        		 if(line!=null) {
        			 if(keywords.containsKey(line)==false) //if that key exists in the table check if the doc file matches
        				{
        					Occurrence occur = new Occurrence(docFile,1);
        					keywords.put(line, occur);
        					op=occur;
        				}
        				
        				else
        				{
        					Occurrence occur=keywords.get(line);
        					occur.frequency++;
        					op=occur;
        				}
        			// System.out.println(op);
        			
        		 //System.out.println(line);
        		 }
        		 else continue;
         }
         sc.close();
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return keywords;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for (String key : kws.keySet())
		{
			ArrayList<Occurrence> oc = new ArrayList<Occurrence>();

			if (keywordsIndex.containsKey(key)) {
				oc = keywordsIndex.get(key);//if the occurence already exist in the main key word
			}
			oc.add(kws.get(key));
			insertLastOccurrence(oc);
			keywordsIndex.put(key, oc);
		}
	}//method closed
	
	private String stripWord(String word) {
		String ans=null;
		word=word.toLowerCase();
		//System.out.println(word);
		int mid=-1;
		boolean after=false;
		for(int i=0;i<word.length();i++) {
			char a=word.charAt(i);
			if(!Character.isAlphabetic(a)) {
				if(after)continue;
				mid=i;//mid is the value all the punctutions start at
				//System.out.println(mid);
				after=true;
			}
			else if(after&&Character.isAlphabetic(a)){
				return null;
			}//if it does find an alphabet after 
		}//for closed for not all alphabetic 
		int i=mid;
		while(i!=word.length()&&i>=0&&word.charAt(i)!='.'&&word.charAt(i)!=','&&word.charAt(i)!='?'&&word.charAt(i)!=':'&&word.charAt(i)!=';'&&word.charAt(i)!='!')
			i++;
		mid=i;
		//System.out.println(mid);
		
		if(mid>=0) {
				word=word.substring(0,mid);
		}//strips of anything at the end which is not alphabet but don't want that
		if(noiseWords.contains(word)) {
			return null;
		}
		ans=word;
		
		return ans;
	}
	
	
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		String test=stripWord(word);
		if(test==null) {
			return null;
		}
		for(int i=0;i<test.length();i++) {
			if(!Character.isAlphabetic(test.charAt(i))){
				return null;
			}//if closed
		}//for closed
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return test;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Integer> seq=new ArrayList<Integer>();
		//Occurrence add
		if (occs.size() < 2)
			return null;
		Occurrence add=occs.get(occs.size()-1);
		occs.remove(occs.size()-1);
		int a[]=new int[occs.size()];
		for(int i=0;i<a.length;i++) {
			a[i]=occs.get(i).frequency;
		}
	
		seq=binarySearch(a,add.frequency);
		int addPoint=seq.get(seq.size()-1);
		occs.add(addPoint,add);
		return seq;
//		if (occs.size() < 2)
//			return null;
//
//		int low = 0;
//		int high = occs.size()-2;
//		int target = occs.get(occs.size()-1).frequency;
//		int mid = 0;
//		ArrayList<Integer> midpoints = new ArrayList<Integer>();
//
//		while (high >= low)
//		{
//			mid = ((low + high) / 2);
//			int data = occs.get(mid).frequency;
//			midpoints.add(mid);
//
//			if (data == target)
//				break;
//
//			else if (data < target)
//			{
//				high = mid - 1;
//			}
//
//			else if (data > target)
//			{
//				low = mid + 1;
//				if (high <= mid)
//					mid = mid + 1;
//			}
//		}
//		
//		midpoints.add(mid);
//
//		Occurrence temp = occs.remove(occs.size()-1);
//		occs.add(midpoints.get(midpoints.size()-1), temp);
//
//		return midpoints;
	}
//	
	//binary search from the internet
	private ArrayList<Integer> binarySearch(int a[], int srchVal){
        int lb = 0;
        int ub = a.length - 1;
        int mid=0;
        ArrayList<Integer> obj=new ArrayList<Integer>();
        while(lb <= ub){
            mid = (lb + ub)/2;
            obj.add(mid);
            if(a[mid] == srchVal){
            	break;
                //return mid;
            }
            else if(srchVal < a[mid]){
                lb = mid + 1;
            }
            else{
                ub = mid - 1;
            }
         }
        return obj;
    }

	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	
		public ArrayList<String> top5search(String kw1, String kw2)
		{
			ArrayList<String> results = new ArrayList<String>();
			ArrayList<Occurrence> ocArr1 = new ArrayList<Occurrence>();
			ArrayList<Occurrence> ocArr2 = new ArrayList<Occurrence>();
			ArrayList<Occurrence> combined = new ArrayList<Occurrence>();
			
			if (keywordsIndex.containsKey(kw1))
				ocArr1 = keywordsIndex.get(kw1);
			
			if (keywordsIndex.containsKey(kw2))
				ocArr2 = keywordsIndex.get(kw2);
			
			combined.addAll(ocArr1);
			combined.addAll(ocArr2);
			
			if (!(ocArr1.isEmpty()) && !(ocArr2.isEmpty()))
			{
				// Sort with preference for ocArr1
				for (int x = 0; x < combined.size()-1; x++)
				{
					for (int y = 1; y < combined.size()-x; y++)
					{
						if (combined.get(y-1).frequency < combined.get(y).frequency)
						{
							Occurrence temp = combined.get(y-1);
							combined.set(y-1, combined.get(y));
							combined.set(y,  temp);
						}
					}
				}

				// Remove duplicates
				for (int x = 0; x < combined.size()-1; x++)
				{
					for (int y = x + 1; y < combined.size(); y++)
					{
						if (combined.get(x).document == combined.get(y).document)
							combined.remove(y);
					}
				}
			}

			// Top 5
			while (combined.size() > 5)
				combined.remove(combined.size()-1);
			
		//	System.out.println(combined);
			
			for (Occurrence oc : combined)
				results.add(oc.document);

			return results;
		}
	
	
	}

