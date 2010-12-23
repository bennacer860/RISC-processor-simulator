import java.util.*;
import java.io.*;
public class Assembler {

	ArrayList<String> Jtype=new ArrayList<String>();
	ArrayList<String> Rtype=new ArrayList<String>();
	ArrayList<String> Itype=new ArrayList<String>();
	Hashtable tobinary=new Hashtable();
	Hashtable labels=new Hashtable();
	File file;
	//memory where we are going to store our program
	ArrayList<String> RAM;
	
	public Assembler(String fname){
            //file =new File("C:\\test.txt");
		file=new File(fname);
		RAM=new ArrayList<String>();
		
		//add Jtypes
		Jtype.add("pop");Jtype.add("push");Jtype.add("b");
		
		//add Rtypes
		Rtype.add("add");Rtype.add("sub");Rtype.add("xor");Rtype.add("load");
		Rtype.add("store");Rtype.add("jalr");Rtype.add("beqz");Rtype.add("bn");
		Rtype.add("mov");
		
		//add Jtypes
		Itype.add("llli");Itype.add("llui");Itype.add("luli");Itype.add("luui");
		
		//binary numbers
		tobinary.put("0","00");	tobinary.put("1","01");	tobinary.put("2","10");	tobinary.put("3","11");
		
	}
	
	//get the array that generates the machine code from assembly code
	public Object[] getRAM(){		
		Object[] ram= (Object[]) this.RAM.toArray();
		//debug((String)ram[0]+" gg\n");
		return  ram;
	}
	
	void debug(String s){
		System.out.print(s);
	}
	
	//TODO[labels doesnt work]
	void setLabels() throws FileNotFoundException{
		Scanner scan=new Scanner(file);
		int counter=0;
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			//debug("\nline:"+line);
			//if it is not an empty line
			if(!line.equals("")){
			 Scanner scanLine=new Scanner(line);
			  //if we have a lablel 
				//debug("\n1");
			   String word=" ";
			   if(scanLine.hasNext()){
				   word=scanLine.next();
				   //debug(word.charAt(0)+"");
				   if(word.charAt(0)=='@'){
					   labels.put(word, counter);
					   debug("\nput ("+word+","+counter+")");
				   }
				   counter++;
				}
			}
		}		
		debug("\nlines: "+counter+"");
		debug("\nlabels:"+labels);
	}
	
	public String binary(int n){
		String bin=Integer.toBinaryString(n);
		int size=bin.length();
		if(size!=8){
			String padding = "";
			for(int i=0;i<8-size;i++)
				padding+="0";
			bin=padding+bin;
		}
		
		return bin;	
	}
	
	void translate() throws FileNotFoundException{
		//set all the labels before starting
		setLabels();

		
		Scanner scan=new Scanner(file);
		while (scan.hasNext()){
			String instruction=scan.next().toLowerCase();
			String assembly="";
			debug(" \n"+instruction +" ");
			if (Jtype.contains(instruction)){
				//debug("jtype");
				if(instruction.equals("pop")){
					assembly+="1111";					
					String parameters=scan.next();
					assembly+=""+tobinary.get(""+parameters.charAt(1)) ;		
					assembly+="01";
					RAM.add(assembly);
					debug(assembly);
				}
				if(instruction.equals("push")){
					assembly+="1111";					
					String parameters=scan.next();
					assembly+=""+tobinary.get(""+parameters.charAt(1))  ;
					assembly+="10";
					RAM.add(assembly);
					debug(assembly);
				}
				if(instruction.equals("b")){
					assembly+="1111";					
					String parameters=scan.next();
					debug(tobinary.get(""+parameters.charAt(1))+"");
					assembly+=""+tobinary.get(""+parameters.charAt(1))  ;		
					assembly+="00";
					RAM.add(assembly);
					debug(assembly);
				}			
			}
			else if (Rtype.contains(instruction)){
				if(instruction.equals("add")){
					assembly+="0000";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					assembly+=tobinary.get(""+parameters.charAt(4)) ;
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("sub")){
					assembly+="0001";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					assembly+=tobinary.get(""+parameters.charAt(4)) ;
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("load")){
					assembly+="0010";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					assembly+=tobinary.get(""+parameters.charAt(4)) ;	
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("store")){
					assembly+="0011";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1));		
					assembly+=tobinary.get(""+parameters.charAt(4));
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("jalr")){
					assembly+="0111";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					assembly+=tobinary.get(""+parameters.charAt(4)) ;
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("beqz")){
					assembly+="0101";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					assembly+=tobinary.get(""+parameters.charAt(4)) ;	
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("bn")){
					assembly+="0110";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					assembly+=tobinary.get(""+parameters.charAt(4)) ;
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("xor")){
					assembly+="1000";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;
					debug("dd "+tobinary.get(""+parameters.charAt(1))+"\n");
					assembly+=tobinary.get(""+parameters.charAt(4)) ;	
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("mov")){
					debug("heree");
					assembly+="1001";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					assembly+=tobinary.get(""+parameters.charAt(4)) ;
					RAM.add(assembly);
					debug(assembly);
				}
				
			}
			else  if (Itype.contains(instruction)){
				if(instruction.equals("llli")){
					assembly+="1010";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					if(parameters.substring(3).charAt(0)=='@'){
						//check if we have the label in our table
						if(labels.get(parameters.substring(3)) == null){
							debug("\nnone existing label! :"+parameters.substring(3));
							System.exit(1);
						}
						//if we try to load a label
						assembly+=""+binary(Integer.parseInt(""+labels.get(parameters.substring(3)))).substring(6,8) ;
						//debug(binary(Integer.parseInt(""+labels.get(parameters.substring(3)))).substring(6,8) +"\n");
					}
					else
						//if we load an immediate
						assembly+=binary(Integer.parseInt(parameters.substring(3))).substring(6,8) ;	
					
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("llui")){
					assembly+="1011";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					if(parameters.substring(3).charAt(0)=='@'){
						//check if we have the label in our table
						if(labels.get(parameters.substring(3)) == null){
							debug("\nnone existing label! :"+parameters.substring(3));
							System.exit(1);
						}
						//if we try to load a label
						assembly+=binary(Integer.parseInt(""+labels.get(parameters.substring(3)))).substring(4,6) ;	
					}
					else
						//if we load an immediate
						assembly+=binary(Integer.parseInt(parameters.substring(3))).substring(4,6) ;		
					
					RAM.add(assembly);
					debug(assembly);
				}
				else if(instruction.equals("luli")){
					assembly+="1100";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					if(parameters.substring(3).charAt(0)=='@'){
						//check if we have the label in our table
						if(labels.get(parameters.substring(3)) == null){
							debug("\nnone existing label! :"+parameters.substring(3));
							System.exit(1);
						}
						//if we try to load a label
						assembly+=binary(Integer.parseInt(""+labels.get(parameters.substring(3)))).substring(2,4) ;
					}
					else
						//if we load an immediate
						assembly+=binary(Integer.parseInt(parameters.substring(3))).substring(2,4) ;		
					
					RAM.add(assembly);
					debug(assembly);
					}
				else if(instruction.equals("luui")){
					assembly+="1101";					
					String parameters=scan.next();
					assembly+=tobinary.get(""+parameters.charAt(1)) ;		
					if(parameters.substring(3).charAt(0)=='@'){
						//check if we have the label in our table
						if(labels.get(parameters.substring(3)) == null){
							debug("\nnone existing label! :"+parameters.substring(3));
							System.exit(1);
						}
						//if we try to load a label
						assembly+=binary(Integer.parseInt(""+labels.get(parameters.substring(3)))).substring(0,2) ;	
					}
					else
						//if we load an immediate
						assembly+=binary(Integer.parseInt(parameters.substring(3))).substring(0,2) ;				
					
					RAM.add(assembly);
					debug(assembly);
				}
			}
			else if(instruction.charAt(0)=='@'){
				
			}
			else{
				debug("this instruction instruction is invalid");
				System.exit(1);
			}
			
			
			
		}
		
		
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		Assembler asm=new Assembler("test.txt");
        asm.debug("Conversion : "+asm.binary(19));
		asm.translate();
        asm.debug("\n"+asm.RAM.toString());
        asm.getRAM();
        //        asm.setLabels();
     //   asm.debug(asm.tobinary(0));
	}

}
