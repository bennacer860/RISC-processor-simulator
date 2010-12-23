import java.io.FileNotFoundException;
import java.util.*;
import java.beans.AppletInitializer;
import java.io.*;
import java.applet.*;
import java.awt.*;
import javax.swing.JTable;

public class Simulator extends SimApplet{
    //registers
    String[] RAM;/*={"11111001","11111001","11111001","11111001","11110010","11111010",
    		"11111010","11111010","FF","10001010","10001010","10001010","10001010",
    		"10001010","10001010","10001010","10001010","","","","","","","",""
    		,"","","","","","","","","","","","","","","","","","","","","","","",""};
  */
   //  SimApplet sim=new SimApplet();
    int[]RF=new int[4];

    int Cycle,PC=0,AOR,AIR;
    String IR;
        Object[] memory ;
    
        
    //TODO[review the architecture of push and pop ]
    public Simulator(String fname) throws FileNotFoundException{
    	RAM=new String[256];
    	load(fname);
    	//initialize the register
        RF[3]=256;
    	RF[2]=0;
        RF[1]=0;
        RF[0]=0;        
        }
    
    public void debug(String s){
        
        System.out.println(s);
    }
    
    public int binaryToDecimal(String i){
        return Integer.parseInt(i,2);
    }
    
    public void show1(){
    	
    	debug("\nREGISTERS :** R0:"+RF[0]+" R1:"+RF[1]+" R2:"+RF[2]+" R3:"+RF[3]);
    	debug("\nStack :");
        showStack();
    }
    public void showregisters()
    {
        jtextr0.setText(Integer.toString(RF[0]));
        jtextr1.setText(Integer.toString(RF[1]));
        jtextr2.setText(Integer.toString(RF[2]));
        jtextr3.setText(Integer.toString(RF[3]));
    }
    
    public void showStack(){
    	ArrayList<String> Stack=new ArrayList<String>();
    	int pointer=RF[3];
    	while(pointer <256){
    		Stack.add(RAM[pointer]);
    		pointer++;
    	}
    	debug(Stack.toString()+"-->");
    	
    	/*for(int i=250;i<256;i++)
    		Stack.add(RAM[i]);
    	debug(Stack.toString()+"-->");
    	debug("\naddress in :"+RAM[41]);*/
    }
    
    public void test(String IR){
        String ins=IR;
        debug(IR);
        debug("rs"+binaryToDecimal(ins.substring(4,6)));
        debug("rt"+binaryToDecimal(ins.substring(6,8)));
        debug("opcode"+ins.substring(0,4));
    }
    public void load(String fname) throws FileNotFoundException{
    	//load the machine code in memory
    	Assembler asm=new Assembler(fname);
    	asm.translate();
      memory = asm.getRAM();                     //Object[] memory
    	for(int i=0;i<memory.length;i++)
        {	  
            RAM[i]=(String)memory[i];
            
       
        }
    	//insert the syscall for ending the program
    	RAM[memory.length]="FF";
        
    	//set to empty values the rest of the RAM 
    	for(int i=memory.length+1;i<256;i++)
        {
    		RAM[i]="FF";
              
        }
    }
    public void initialize()
    {
        for(int i=0;i<memory.length;i++)
        {	  
            RAM[i]=(String)memory[i];
        jTable1.setValueAt(RAM[i],i,1);
        }
        jTable1.setValueAt("FF",memory.length,1);
        for(int i=memory.length+1;i<256;i++)
        {
                    jTable1.setValueAt("FF",i,1);
        }
        jtextr0.setText(Integer.toString(RF[0]));
        jtextr1.setText(Integer.toString(RF[1]));
        jtextr2.setText(Integer.toString(RF[2]));
        jtextr3.setText(Integer.toString(RF[3]));
    }
    
    public void run(){
    	int limit=0;
    	debug("\n*************************************************");
    	debug("\n************RUNNING THE SIMULATOR:***************");
    	debug("\n*************************************************");
    	
        while(true)
        {    try{
                Thread.sleep(4000);
             }catch(Exception e){}
            switch(Cycle)
            {
                
                
                   
                
                case 0:
                	/*if(limit==20)
                		return;
                	limit++;*/
                	//fetch the instruction
                    
                    //Object [][] update=
                  //  jTable1.setValueAt("300",3,1);
                    //////
                    IR=RAM[PC];
                    jtextir.setText(IR);
                   
                    PC=PC+1;
                     jtextpc.setText(Integer.toHexString(PC));
                    Cycle=Cycle+1;
                  // jTable1.editCellAt(3,1);
                    //if we are in the end of the ram
                    jtextmux1.setText("XX");
                     jtextmux2.setText("1");
                     jtextmux5.setText("PC");
                    jtextalu.setText("+") ; 
                    jtextreg.setText("clk:IR,PC");
                    
                    if(IR.equals("FF")){
                        return;
                    }
                    
                    break;
                case 1:
                    AIR=RF[binaryToDecimal(IR.substring(4,6))];    //RF[rs]
                    //debug(AIR+" "+IR.substring(4,6));   
                     jtextair.setText(Integer.toString(AIR));
                     jtextmux1.setText("reg:"+Integer.toString(binaryToDecimal(IR.substring(4,6))));   //RF[rs]
                     jtextmux2.setText("XX");
                    jtextmux5.setText("XX");
                     jtextalu.setText("XX") ; 
                     jtextmux3.setText("XX");
                     jtextmux4.setText("XX");
                     jtextreg.setText("clk:AIR");
                     
                    Cycle=Cycle+1;
                    break;
                    
                    /*choose the cycle from the opcode of IR*/
                case 2:
                	//opcode==0 (ADD)
                    if (binaryToDecimal(IR.substring(0,4))==0)        
                    {   
                        jtextrf.setText(IR.substring(6,8));
                        AOR=AIR+RF[binaryToDecimal(IR.substring(6,8))];    //RF[rt]
                        jtextmux1.setText("reg:"+Integer.toString(binaryToDecimal(IR.substring(6,8))));    //RF[rt]
                        jtextmux2.setText("AIR");
                        jtextmux5.setText("RF");
                        jtextalu.setText("+");
                        jtextaor.setText(Integer.toString(AOR));
                        jtextreg.setText("clk:AOR");
                        jtextmux3.setText("XX");
                        jtextmux4.setText("XX");
                        //debug(AOR+"");
                        Cycle=Cycle+1;
                    }
                    //opcode==2 or 3 (Load or Store)
                    else if (binaryToDecimal(IR.substring(0,4))==3 || binaryToDecimal(IR.substring(0,4))==2)        
                    {
                        AIR=RF[binaryToDecimal(IR.substring(6,8))];    //RF[rt]
                        //opcode =3 perform load instruction
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                        jtextreg.setText("AIR");
                        jtextmux2.setText("XX");
                        jtextmux3.setText("XX");
                        jtextmux4.setText("XX");
                         jtextmux5.setText("XX");
                         jtextair.setText(Integer.toString(AIR));
                         jtextalu.setText("XX");
                         
                        if(binaryToDecimal(IR.substring(0,4))==2)
                         Cycle=Cycle+2;
                        //opcode =2 perform store instruction
                        else
                         Cycle=Cycle+3;    
                    }
                    
                    
                    //opcode==5 (BEZQ)
                    else if(binaryToDecimal(IR.substring(0,4))==5)  ////BEQZ
                    {             	
                       	AOR=RF[binaryToDecimal(IR.substring(4,6))];
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                        jtextaor.setText(Integer.toString(AOR));
                        jtextreg.setText("AOR");
                        jtextmux2.setText("XX");
                        jtextmux3.setText("XX");
                        jtextmux4.setText("XX");
                         jtextmux5.setText("XX");
                          jtextalu.setText("XX");
                        
                       	debug("beqz  AOR:"+AOR+" RF["+IR.substring(4,6));
                    	Cycle=Cycle+8;
                    }
                    
//                  opcode==6 (BN)
                    else if(binaryToDecimal(IR.substring(0,4))==6)      
                    {
                    	System.out.println("i am here");
                        
                    	Cycle=Cycle+13;
                    }
                    
                    //opcode==8 (XOR)
                    else if (binaryToDecimal(IR.substring(0,4))== 8)        
                    {
                        AOR=AIR^RF[binaryToDecimal(IR.substring(6,8))]; //RF[rt]
                        //debug(AOR+"");
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                        jtextalu.setText("XOR");
                        jtextreg.setText("AOR");
                        jtextmux2.setText("1");    ///
                        jtextmux5.setText("RF");
                        jtextmux3.setText("XX");
                        jtextmux4.setText("XX");
                         jtextaor.setText(Integer.toString(AOR));
                        
                        Cycle=Cycle+1;
                    }
//                  opcode==9 (MOV)
                    else if (binaryToDecimal(IR.substring(0,4))== 9)        
                    {
                    	AOR=RF[binaryToDecimal(IR.substring(6,8))]; //RF[rt]
                        debug(AOR+"");
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                        jtextmux5.setText("RF");
                        jtextalu.setText("XX");
                        jtextreg.setText("AOR");
                        jtextmux3.setText("XX");
                        jtextmux4.setText("XX");
                        jtextmux2.setText("XX"); 
                         jtextaor.setText(Integer.toString(AOR));
                        Cycle+=14;
                    }  
                    
                    //opcode==10 (LLLI)
                    else if(binaryToDecimal(IR.substring(0,4))==10) { //LLLI                    	               	
                        //	debug("LLLI");
                        	Cycle=Cycle+9;                       	
                    }
                    //opcode==11 (LLUI)
                    else if(binaryToDecimal(IR.substring(0,4))==11) {//llui                       	                        	
                        //	debug("LLUI");
                        	Cycle=Cycle+10;                        	
                    }
                    
                    //opcode==12 (LULI)
                    else if(binaryToDecimal(IR.substring(0,4))==12) {                        	                       	
                    //	debug("LULI");				 //luli	
                        	Cycle=Cycle+11;                       	
                    }
                    
                    //opcode==13 (LUUI)
                    else if(binaryToDecimal(IR.substring(0,4))==13) {                       	                        	
                        //	debug("LUUI");				//luui
                        	Cycle=Cycle+12;                    	
                    }
                    
                    //opcode==15 (PUSH B POP)
                    else if (binaryToDecimal(IR.substring(0,4))== 15)        
                    {
                    	//check the last bit to know what instrcution
                    	
                    	//IR is a B instruction
                    	if(binaryToDecimal(IR.substring(6,8))==0)
                    	{
                    		PC=RF[binaryToDecimal(IR.substring(4,6))];
                			//PC is loaded with address in RF[rs]
                                jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                                jtextmux3.setText("PC");
                                jtextreg.setText("PC clk");
                                jtextpc.setText(Integer.toString(PC));
                                jtextmux2.setText("XX"); 
                                jtextmux5.setText("XX");
                                jtextmux4.setText("XX");
                                jtextalu.setText("XX");
                			Cycle=0;
                			System.out.println("B executed go to &"+PC+":"+RAM[PC] );                      			
                	
                    	}
                    	
                    	//IR is a POP instruction
                    	if(binaryToDecimal(IR.substring(6,8))==1)
                    	{
                    		//debug("AIR "+AIR+" stack"+RF[3]);
                    		RF[binaryToDecimal(IR.substring(4,6))]=Integer.parseInt(RAM[RF[3]]+"");    //m[AIR]->RF[3] the stack
                                jtextmux3.setText("m[AIR]");
                                jtextmux4.setText("memory");
                                jtextreg.setText("RF");
                                jtextmux2.setText("XX");
                                jtextmux5.setText("XX");
                                   jtextalu.setText("XX");
                                   jtextr3.setText(RAM[RF[3]]);
                                jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                    		Cycle=Cycle+4;
                    	}
                    	
                      	//IR is a PUSH instruction
                    	if(binaryToDecimal(IR.substring(6,8))==2)
                    	{
                    		//debug("AIR "+AIR+" stack"+RF[3]);
                    		AOR=RF[3]-1;
                                jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                                jtextmux2.setText("1");
                                jtextmux3.setText("XX");
                                jtextmux4.setText("XX");
                                jtextmux5.setText("RF");
                                jtextreg.setText("AOR");
                                jtextalu.setText("-");
                                jtextaor.setText(Integer.toString(AOR));
                    		Cycle=Cycle+6;
                    	}
                    	
                    	
                    }
                    //opcode==1(sub)
                    else if (binaryToDecimal(IR.substring(0,4))== 1)
                    {  
                    	debug("here");
                        AOR=AIR-RF[binaryToDecimal(IR.substring(6,8))];  //RF[rt]
                        Cycle=Cycle+1;
                       // jtextrf.setText(IR.substring(6,8));
                       
                        jtextmux1.setText("reg:"+Integer.toString(binaryToDecimal(IR.substring(6,8))));    //RF[rt]
                        jtextmux2.setText("AIR");
                        jtextmux5.setText("RF");
                        jtextalu.setText("-");
                        jtextaor.setText(Integer.toString(AOR));
                        jtextreg.setText("clk:AOR");
                        jtextmux3.setText("XX");
                        jtextmux4.setText("XX");
                    }
                    break;
                case 3:
                	//add3 sub3 XOR3
                    RF[binaryToDecimal(IR.substring(4,6))]=AOR;    //RF[rd]
                    jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                     jtextmux2.setText("XX");
                    jtextmux5.setText("XX");
                     jtextalu.setText("XX") ; 
                     jtextreg.setText("RF");
                     jtextmux3.setText("XX");
                     jtextmux4.setText("RF");
                    showregisters();                     
                     
                     
                    Cycle=0;
                    debug("operation done R"+binaryToDecimal(IR.substring(6,8))+"="+RF[binaryToDecimal(IR.substring(6,8))]);
                    break;
                    
                case 4:
                    //load
                    RF[binaryToDecimal(IR.substring(4,6))]=Integer.parseInt(""+binaryToDecimal(RAM[AIR]));                    
                    debug("loaded in register Rresult="+RF[binaryToDecimal(IR.substring(6,8))]);
                     jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8)))); 
                       
                     jtextmux3.setText("AIR");
                    jtextmux5.setText("XX");
                     jtextalu.setText("XX") ; 
                     jtextreg.setText("RF");
                     jtextmux2.setText("XX");
                     jtextmux4.setText("mem");
                     showregisters();
                    
                    Cycle=0;
                    
                    break;
                    
                case 5:
                    //store
                    RAM[AIR]=""+RF[binaryToDecimal(IR.substring(4,6))];  
                    jTable1.setValueAt(RAM[AIR],AIR,1);
                
                   
                   
                    debug("\nstored in address "+ RF[binaryToDecimal(IR.substring(6,8))] +" RAM res="+RAM[AIR]+"");
                  
                    jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                    jtextmux3.setText("m[AIR]");
                    jtextmux5.setText("XX");
                     jtextalu.setText("XX") ; 
                     jtextreg.setText("XX");       ////////////
                     jtextmux2.setText("XX");
                     jtextmux4.setText("mem");
                    
                    
                    Cycle=0;
                    break;
                    
                case 6:
                    //pop3
                    AOR=RF[3]+1;  
                    jtextmux1.setText("R[3]");
                    jtextalu.setText("+");
                    jtextaor.setText(Integer.toString(AOR));
                    jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                    
                    jtextmux3.setText("m[AIR]");
                    jtextmux5.setText("RF");
                     
                     jtextreg.setText("clk:AOR");       ////////////
                     jtextmux2.setText("1");
                     jtextmux4.setText("XX");
                    
                    Cycle=Cycle+1;
                    break;    
                case 7:
                    //pop4
                    RF[3]=AOR;        
                    debug("poped value: "+RF[binaryToDecimal(IR.substring(4,6))]+" in R"+binaryToDecimal(IR.substring(4,6)));
                    debug("the stack points to the address : "+RF[3]);
                    jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));     ///////////////////
                    jtextmux4.setText("RF");          //////
                    jtextreg.setText("RF");
                    jtextmux3.setText("m[AIR]");
                    jtextmux5.setText("XX");
                     jtextalu.setText("XX") ; 
                     
                     jtextmux2.setText("XX");
                     jtextr3.setText(Integer.toString(RF[3]));
                                        
                    
                    Cycle=0;
                    break;       
                case 8:
                	//push3
                	RF[3]=AOR;
                	Cycle=Cycle+1;
                        jtextmux1.setText("RF[3]");////
                        jtextmux2.setText("XX");
                       
                        jtextreg.setText("AIR"); 
                        jtextmux4.setText("XX");          //////
                   
                    jtextmux3.setText("XX");
                    jtextmux5.setText("XX");
                     jtextalu.setText("XX") ; 
                     jtextr3.setText(Integer.toString(RF[3]));
                                  
                        
                        
                	break;
            
                case 9:
                	//push4
                	debug("AIR "+AIR+" AOR "+AOR);
                	RAM[AOR]=AIR+"";
                	debug("Stack point to "+RF[3] + " with value "+RAM[RF[3]]);
                        jTable1.setValueAt(RAM[AOR],AOR,1);
                        jtextmux4.setText("XX");          //////
                    jtextreg.setText("AIR");
                    jtextmux3.setText("m[AIR]");
                    jtextmux5.setText("XX");
                     jtextalu.setText("XX") ;
                     jtextmux1.setText("XX");
                     jtextmux2.setText("XX");
                     
                	Cycle=0;
                	break;
                	
                	//beqz
                case 10:
                	if(AOR==0)
            		{
            			PC=RF[binaryToDecimal(IR.substring(6,8))];
                                jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                                jtextmux5.setText("PC");
                                jtextreg.setText("reg clk:IR,PC");
                                 
                     jtextalu.setText("if==0") ;
                     jtextmux3.setText("XX");
                     jtextmux2.setText("XX");
                      jtextmux4.setText("XX");
                      jtextpc.setText(Integer.toString(PC));
                                
            				//PC is loaded with address in RF[rs]
            				Cycle=0;
            				System.out.println("BEQZ executed go to address &"+PC+":"+ RAM[PC]);            				
            				//show();
            		}
            		else
            		{
            			Cycle=0;
            		}
                	break;
                	
                case 11:
                	//LLLI
                	RF[binaryToDecimal(IR.substring(4,6))]=0;
                	int  bit0=Integer.parseInt(""+IR.charAt(7));
                	int bit1=Integer.parseInt(""+IR.charAt(6));
                	RF[binaryToDecimal(IR.substring(4,6))]=binaryToDecimal(IR.substring(6,8));
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                        jtextmux4.setText("RF[0:1]");
                        jtextalu.setText("XX") ;
                     jtextmux3.setText("XX");
                     jtextmux2.setText("XX");
                      jtextreg.setText("");           /////////doubt////
                      jtextmux5.setText("RF"); ///////////////doubt/////
                	debug("\nLLLI"+RF[binaryToDecimal(IR.substring(4,6))]);
                        showregisters();
                	Cycle=0;
                	break;
                	
                case 12:
                	//LLUI
                	double  bit2=Integer.parseInt(""+IR.charAt(7));
                	double bit3=Integer.parseInt(""+IR.charAt(6));
                	RF[binaryToDecimal(IR.substring(4,6))]=RF[binaryToDecimal(IR.substring(4,6))]+(int)(Math.pow(2,2)*bit2)+(int)(Math.pow(2,3)*bit3);//binaryToDecimal(IR.substring(6,8));
                	//debug((int)(Math.pow(2,2)*bit2)+(int)(Math.pow(2,3)*bit3)+"");
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                        jtextmux4.setText("RF[2:3]");
                        
                         jtextalu.setText("XX") ;
                     jtextmux3.setText("XX");
                     jtextmux2.setText("XX");
                      jtextreg.setText("");           /////////doubt////
                      jtextmux5.setText("RF");
                       showregisters();
                        
                	debug("LLUI"+RF[binaryToDecimal(IR.substring(4,6))]);
                	Cycle=0;
                	break;
                	
                case 13:
                	//LULI
                	double  bit4=Integer.parseInt(""+IR.charAt(7));
                	double bit5=Integer.parseInt(""+IR.charAt(6));
                	RF[binaryToDecimal(IR.substring(4,6))]=RF[binaryToDecimal(IR.substring(4,6))]+(int)(Math.pow(2,4)*bit4)+(int)(Math.pow(2,5)*bit5);///binaryToDecimal(IR.substring(6,8));
                	debug("LULI"+RF[binaryToDecimal(IR.substring(4,6))]);
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                        jtextmux4.setText("RF[4:5]");
                        jtextalu.setText("XX") ;
                     jtextmux3.setText("XX");
                     jtextmux2.setText("XX");
                      jtextreg.setText("");           /////////doubt////
                      jtextmux5.setText("RF");
                       showregisters();
                	Cycle=0;
                	break;
                	
                case 14:
                	//LUUI               	
                	double  bit6=Integer.parseInt(""+IR.charAt(7));
                	double  bit7=Integer.parseInt(""+IR.charAt(6));
                	RF[binaryToDecimal(IR.substring(4,6))]=RF[binaryToDecimal(IR.substring(4,6))]+(int)(Math.pow(2,6)*bit6)+(int)(Math.pow(2,7)*bit7);//binaryToDecimal(IR.substring(6,8));
                	debug("LUUI"+RF[binaryToDecimal(IR.substring(4,6))]);
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                        jtextmux4.setText("RF[6:7]");
                        jtextalu.setText("XX") ;
                     jtextmux3.setText("XX");
                     jtextmux2.setText("XX");
                      jtextreg.setText("");           /////////doubt////
                      jtextmux5.setText("RF");
                       showregisters();
                	Cycle=0;
                	break;
                case 15:
                	//branch if negative number
                	AOR=RF[binaryToDecimal(IR.substring(4,6))];
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                        jtextreg.setText("AOR");
                        jtextalu.setText("<");
                        
                     jtextmux3.setText("XX");
                     jtextmux2.setText("XX");
                       jtextmux2.setText("XX");       
                      jtextmux5.setText("RF");
                       showregisters();
                        
            		if(AOR<0)
            		{
            			PC=RF[binaryToDecimal(IR.substring(6,8))];
            				//PC is loaded with address in RF[rs]
                                jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(6,8))));
                                jtextmux5.setText("PC");
                                jtextreg.setText("reg clk:IR,PC");
                                 
                     jtextalu.setText("if==0") ;
                     jtextmux3.setText("XX");
                     jtextmux2.setText("XX");
                      jtextmux4.setText("XX");
                      jtextpc.setText(Integer.toString(PC));
            				Cycle=0;
            				System.out.println("BN executed");
            				System.out.println("pc is"+PC);
            		}
            		else
            		{
            			Cycle=0;
            		}            			
            		break;
                case 16:
                	RF[binaryToDecimal(IR.substring(4,6))]=AOR;
                        jtextmux1.setText(Integer.toString(binaryToDecimal(IR.substring(4,6))));
                     jtextmux2.setText("XX");
                      jtextmux3.setText("XX");
                      jtextmux4.setText("XX");
                       jtextmux5.setText("XX");
                       jtextalu.setText("XX");
                        jtextreg.setText("clk:RF");
                	Cycle=0;
                	break;
              }        
            }
        }

    
    public static void main(String args[]) throws FileNotFoundException{
        Simulator s=new Simulator("C:\\test.txt");
        //s.test("00001001");
       
        	//SimApplet sim= class.forName("SimApplet.java");		
     
        
       // s.asstomac();
        Frame myFrame=new Frame("Applet Holder");
        s.init();
        s.start();
        myFrame.add(s);
        myFrame.pack();
        myFrame.setVisible(true);
        s.initialize();
        s.jtextalu.setText("beeeee");
        s.jtextmux4.setText("Mux4");
        s.run();
        s.show1();
        s.jtextalu.setText("beeeee");
        s.jtextmux4.setText("Mux4");
       // s.debug("&41:"+s.RAM[41]);
    }   
}