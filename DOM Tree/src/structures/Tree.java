package structures;

import java.util.*;

//import org.w3c.dom.Node;
@SuppressWarnings("unused")
/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	private String getMatter(String s) {
		StringTokenizer str =  new StringTokenizer(s,"<>");
		String ans="";
		while(str.hasMoreTokens()) {
			ans+=str.nextToken();
		}
		return ans;
	}
	private boolean isTag(String s) {
		if(s.charAt(0)=='<'&&s.charAt(1)!='/')
			return true;
		return false;					
	}
	
	private boolean isClosingTag(String s) {
	if(s.charAt(0)=='<') {	
		if(s.charAt(1)=='/') {
			return true;}
	}
		return false;
	}
	
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		root=new TagNode(null,null,null);
		Stack<TagNode> tags=new Stack<TagNode>();
		tags.push(new TagNode(getMatter(sc.nextLine()),null,null));
		root=tags.peek();
		tags.push(new TagNode(getMatter(sc.nextLine()),null,null));
		root.firstChild=tags.peek();
		//a=root.firstChild;
		TagNode ptr=root.firstChild;
		//puts html and body in the loop like it's no big deal
		
		while(sc.hasNext()) {
			
			String s=sc.nextLine();
			if(s.equals("</body>"))
				break;
			if(isTag(s)) {
				TagNode add=new TagNode(getMatter(s),null,null);
				if(tags.peek().firstChild==null) {
					ptr.firstChild=add;
					ptr=ptr.firstChild;
				}
				else {
					ptr.sibling=add;
					ptr=ptr.sibling;
				}
				tags.push(add);
			}//if the html line is a tag it does this
			else if(!(isTag(s))&&!(isClosingTag(s))) {
				TagNode addTag=new TagNode(s,null,null);
				if(tags.peek().firstChild==null) {
					ptr.firstChild=addTag;
					ptr=ptr.firstChild;
				}
				else {
					ptr.sibling=addTag;
					ptr=ptr.sibling;
				}
				
			}//if the html line is not a tag or a closing tag
			
			else if (isClosingTag(s)) {
				tags.pop();
				ptr=tags.peek();
				if(tags.peek().firstChild!=null)
					ptr=tags.peek().firstChild;
				while(ptr.sibling!=null) {
					ptr=ptr.sibling;
				}
			}
		}//wjile closed
		
	}//method closed
	
	private void replace(TagNode n,String oldTag, String newTag) {
		//TagNode ptr=n;
		while(n!=null)
		{
			if(n.tag.equals(oldTag)) {
				n.tag=newTag;
			}
			if(n.firstChild!=null)
				 replace(n.firstChild,oldTag,newTag);
				n=n.sibling;

		}
		
			
		//return 
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		replace(root,oldTag,newTag);
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	
	private TagNode ptrfound;
	private TagNode find(TagNode tag,String tagS,int count) {
		
		TagNode ptr=tag;
		//TagNode ptr2=ptr;
		while(ptr!=null) {
			if(ptr.tag.equals(tagS)) {
				count--;
				if(count==0) {
					ptrfound=ptr;
					//return ptrfound;
				}//if closed
			}//if closed
			if(ptr.firstChild!=null) {
				find(ptr.firstChild,tagS,count);
			}
			ptr=ptr.sibling;
		}///while closed
						
	return ptrfound;
	}
	
	
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		//boldingMethod(root,row);
		ptrfound=find(root,"tr",row);
		TagNode ptr=ptrfound.firstChild;
		while((ptr!=null)) {	
			if(ptr.tag.equals("td")) {
				TagNode add=new TagNode("b",ptr.firstChild,null);
				ptr.firstChild=add;
				ptr=ptr.sibling;
			}//if closed
			else
				ptr=ptr.sibling;
		}//while closed
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
//	private boolean isBorEmTag(TagNode ptr) {
//		if(ptr.tag.equals("b")||ptr.tag.equals("em")||ptr.tag.equals("b")&&ptr.firstChild!=null)
//			return true;
//		return false;
//		
//	}
//	private void deleteP (TagNode prev, TagNode ptr, TagNode nextPtr) {
//		
//		if(isBorEmTag(prev)) {
//			prev.firstChild=ptr.firstChild;
//			nextPtr.sibling=ptr.sibling;
//		}
//		else {
//			prev.sibling=ptr.firstChild;
//			nextPtr.sibling=ptr.sibling;
//		}
//		ptr=null;
//	}\
	

	//TagNode prev=null;
	
public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if(tag.equals("p") || tag.equals("b") || tag.equals("em")) {
			int k = findLoopC(root, tag, 0);
			for(int i = 1; i <= k; i++) {
				TagNode ptr = findL(root, tag, 1, 0);
				TagNode prev = findPrev(root, ptr);
				if(prev.firstChild == (ptr) && ptr.firstChild != null) {
					TagNode ptr2 = ptr.firstChild;
					while(ptr2.sibling != null) {
						ptr2 = ptr2.sibling;
					}
					ptr2.sibling = prev.firstChild.sibling;
					prev.firstChild = ptr.firstChild;					
				}
				else if(prev.firstChild == (ptr) && ptr.firstChild == null) {
					prev.firstChild = ptr.sibling;
				}
				else if(prev.sibling == (ptr) && ptr.firstChild != null) {
					if(prev.firstChild !=null) {
						TagNode ptr2 = prev.firstChild;
						while (ptr2.sibling != null) {
							ptr2 = ptr2.sibling;
						}
						ptr2.sibling = ptr.firstChild;
					}
					else {
						TagNode ptr2 = ptr.firstChild;
						while(ptr2.sibling != null) {
							ptr2 = ptr2.sibling;
						}
						ptr2.sibling = ptr.sibling;
						prev.sibling = ptr.firstChild;
					}
				}
				else if(prev.sibling == (ptr) && ptr.firstChild == null) {
					prev.sibling = ptr.sibling;
				}
			}
		}
		//add ifs for when the prev is a sibling and stuff. doesnt work for ul after ol
		else if(tag.equals("ol") || tag.equals("ul")) {
			int k = findLoopC(root,tag,0);
			for(int i = 1; i <= k; i++) {
				TagNode ptr = findL(root, tag, 1, 0);
				TagNode prev = findPrev(root, ptr);
				if(prev.firstChild == (ptr) && ptr.firstChild != null) {
					TagNode ptr2 = ptr.firstChild;
					while(ptr2.sibling != null) {
						ptr2.tag = "p";
						ptr2 = ptr2.sibling;
					}
					ptr2.tag = "p";
					ptr2.sibling =  prev.firstChild.sibling;
					prev.firstChild = ptr.firstChild;					
				}
				else if(prev.firstChild == (ptr) && ptr.firstChild == null) {
					prev.firstChild = ptr.sibling;
				}
				else if(prev.sibling == (ptr) && ptr.firstChild != null) {
					if(prev.firstChild !=null) {
						TagNode ptr3 = ptr.firstChild;
						prev.sibling = ptr3;
						while(ptr3.sibling != null) {
							ptr3.tag = "p";
							ptr3 = ptr3.sibling;
						}
						ptr3.tag = "p";
						ptr3.sibling =ptr.sibling;
					}
					else {
						TagNode ptr2 = ptr.firstChild;
						while(ptr2.sibling != null) {
							ptr2.tag = "p";
							ptr2 = ptr2.sibling;
						}
						ptr2.tag = "p";
						ptr2.sibling = ptr.sibling;
						prev.sibling = ptr.firstChild;
					}
				}
				else if(prev.sibling == (ptr) && ptr.firstChild == null) {
					prev.sibling = ptr.sibling;
				}
			}
			
		}
	}
	private TagNode prev = null;
	private TagNode findPrev(TagNode start, TagNode target) {
		if(start.firstChild != null && start.firstChild ==target) {
			prev = start;
		}
		if(start.sibling != null && start.sibling ==target) {
			prev = start;
		}
		if (start.firstChild != null) {
			findPrev(start.firstChild, target);
		}
		if (start.sibling != null) {
			findPrev(start.sibling, target);
		}
		return prev;
	}
	
	private int findLoopC(TagNode start, String foundTag, int c) {
		for(TagNode ptr = start; ptr != null; ptr = ptr.sibling) {
			if(ptr.tag.equals(foundTag)) {
				c++;
			}
			if(ptr.firstChild!= null) {
				c=findLoopC(ptr.firstChild, foundTag, c);
			}
		}
		return c;
	}
	private TagNode ptrFound = null;
	private TagNode findL(TagNode start, String foundTag, int row, int counter) {
		for(TagNode ptr = start; ptr != null; ptr = ptr.sibling) {
			if(ptr.tag.equals(foundTag)) {
				counter++;
				if(counter == row) {
					ptrFound = ptr;
				}
			}
			if(ptr.firstChild!=null) {
				findL(ptr.firstChild, foundTag, row, counter);
			}
		}
		return ptrFound;
	}


	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		if (!tag.equals("em") && !tag.equals("b")) {
			return;
		}
		addTagRecursive(word, tag, root, null, 1);
	}

	private void addTagRecursive(String word, String tag, TagNode node, TagNode temp, int level) {
		if (node == null) {
			return;
		}
		TagNode tbd = new TagNode(tag, null, null);
		for (TagNode ptr = node; ptr != null; ptr = ptr.sibling) {

			if (ptr.tag.equals(word)) {
				if (ptr == temp.firstChild) {
					temp.firstChild = tbd;
					tbd.firstChild = ptr;
				} else if (ptr == temp.sibling) {
					temp.sibling = tbd;
					tbd.sibling = ptr;
				} else {
				}
				temp = ptr;
			} else if (ptr.tag.contains(word)) {
				TagNode temp_holder = temp;
				String str = ptr.tag;
				String[] tokens = str.split(" ");
				List<String> tok_list = new ArrayList<String>(Arrays.asList(tokens));
				for (int i = 0; i < tok_list.size(); i++) {
					if (tok_list.get(i).contains(word)) {
						continue;
					} else if (i > 0 && !(tok_list.get(i - 1).contains(word))) {
						String a = tok_list.get(i - 1);
						String b = tok_list.get(i);
						tok_list.remove(i);
						tok_list.add(i - 1, a + " " + b);
						tok_list.remove(i);
						a = b = null;
						i--;
					}
				}

				TagNode tempRoot = null;
				TagNode tempPtr = null;
				for (int i = 0; i < tok_list.size(); i++) {
					String str_tok = tok_list.get(i);
					if (str_tok.contains(word)) {
						TagNode tag_tag = new TagNode(tag, null, null);
						TagNode word_tag = new TagNode(str_tok, null, null);
						tag_tag.firstChild = word_tag;
						if (tempPtr != null) {
							if (tempPtr.sibling == null) {
								tempPtr.sibling = tag_tag;
								tempPtr = tag_tag;
							}
						} else if (tempPtr == null) {
							if (tempRoot == null) {
								tempRoot = tag_tag;
							}
							tempPtr = tag_tag;
						}

					} else {
						if (tempRoot == null) {
							tempRoot = new TagNode(str_tok, null, null);
							tempPtr = tempRoot;
						} else {
							if (tempPtr.sibling == null) {
								tempPtr.sibling = new TagNode(str_tok, null, null);
								tempPtr = tempPtr.sibling;
							}
						}
					}
				}
				temp.firstChild = tempRoot;
				tempPtr.sibling = ptr.sibling;
			}

			if (ptr.firstChild != null) {
				addTagRecursive(word, tag, ptr.firstChild, ptr, level + 1);
			}
		}
	}

	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}