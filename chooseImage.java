//Using Swings
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
public class chooseImage extends JFrame implements ActionListener{
	JButton Browse,Next,Message,bAnalyze,bnoise,bsecretMsg;
	JLabel imgname;
	JTextArea msgArea;
	JTextField tnoise,tsecretMsg;
	String filename,secretMsg;
	ImageDemo openImg;
	int[] oneDPix,oneDPixMod;
	int[][][] threeDPix;
	int[][][] threeDPixMod;
	char[][][] segmentedMsg; 
//	byte[][][] plane0,plane1,plane2;
//	BufferedImage rawImg=new BufferedImage();
	BufferedImage rawImg,image;
	Image modImg;
	int iRows,iCols;
	int noiseThreshold,secretMsgThreshold;
	public chooseImage () throws Exception {
		setTitle("Choose Image");
		setSize(400,300);
		setLayout(new GridLayout(5,2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//XInitialing all  components one by one
		bnoise = new JButton("Save Noise threshold");
		bsecretMsg=new JButton("Save SecretMsg threshold");
		tnoise = new JTextField("34");
		tsecretMsg=new JTextField("40");
//		tnoise = new JTextField("Enter any number between 1 and 110");
//		tsecretMsg=new JTextField("Enter any number between 1 and 110");
		Browse = new JButton("Browse");
		imgname= new JLabel("Filename appears here");
		Next = new JButton("Encode");
		Message =new JButton("Save Message");
		msgArea= new JTextArea("Enter your secret message");
		bAnalyze= new JButton("Analyze");
		add(imgname);
		add(Browse);
		add(msgArea);
		Message.setVisible(false);
		add(Message);
		add(tnoise);
		tnoise.setEditable(false);
		add(bnoise);
		bnoise.addActionListener(this);
		bnoise.setVisible(false);
		add(tsecretMsg);
		tsecretMsg.setEditable(false);
		add(bsecretMsg);
		bsecretMsg.setVisible(false);
		add(bAnalyze);
		bAnalyze.addActionListener(this);
		bAnalyze.setVisible(false);
		add(Next);
		Next.setVisible(false);
		msgArea.setEditable(false);
		Message.addActionListener(this);
		Browse.addActionListener(this);
		Next.addActionListener(this);
	}	
	//ActionEvent Handling
	public  void actionPerformed(ActionEvent e) {
		Object src=e.getSource();
		if(src==Browse) {
			FileDialog fd=new FileDialog(new JFrame(),"File Dialog");
			fd.setVisible(true);
			filename=fd.getFile();
			imgname.setText(filename);
			try {
				openImg=new ImageDemo(filename);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			rawImg=openImg.image;
//			grabPixels();
			msgArea.setEditable(true);
			Message.setVisible(true);
		}
		else if(src==Message) {
			secretMsg=msgArea.getText();
			JOptionPane.showMessageDialog(null,"You want to Hide this msg:\n\""+secretMsg+"\"\nin\n"+filename);
			tnoise.setEditable(true);
			bnoise.setVisible(true);
		}
		else if(src==bnoise) {
			noiseThreshold=Integer.parseInt(tnoise.getText());
			//noiseThreshold=Integer.parseInt(tnoise.getText());
			tsecretMsg.setEditable(true);
			bsecretMsg.setVisible(true);	
			bAnalyze.setVisible(true);
			Next.setVisible(true);
		}
		else if(src==bsecretMsg) {
			secretMsgThreshold=Integer.parseInt(tsecretMsg.getText());
//			secretMsgThreshold=Integer.parseInt(tsecretMsg.getText());
		}
		else if(src==bAnalyze) {
			grabPixels();
			convertToThreeD();
			Analyze obj=new Analyze(threeDPix,iRows,iCols,noiseThreshold);
		}
		else if(src==Next){
			if(openImg==null) JOptionPane.showMessageDialog(null, "Choose File first");
			else {
			//	JOptionPane.showMessageDialog(null,"This button is currently inactive");
				grabPixels();
				convertToThreeD();
				int noOfSegments;
				transformMsg obj1=new transformMsg(secretMsg,noiseThreshold);
				noOfSegments=obj1.getnoOfSegments();
				segmentedMsg=obj1.getMsgSegments();
				HideData obj2=new HideData(threeDPix, segmentedMsg, noiseThreshold, noOfSegments, iRows,iCols);
				threeDPixMod=obj2.getthreeDPix();
				convertToOneD();
				image= new BufferedImage(iCols,iRows,BufferedImage.TYPE_INT_RGB);
				image.setRGB(0,0,iCols, iRows, oneDPixMod,0,iCols);
		        //  modImg =  createImage(
		          //        new MemoryImageSource(
		            //      iCols,iRows,oneDPixMod,0,iCols));
		          try {
		        	    // retrieve image
		        	    File outputfile = new File("saved.bmp");
		        	    ImageIO.write( image ,"png", outputfile);
		        	} catch (IOException ef) {
		        	   
		        	}  
				
			}
		}
	}
	
	//Grab Pixels function
	public void grabPixels()  {
		iRows=rawImg.getHeight();
		iCols=rawImg.getWidth();
		oneDPix= new int[iRows*iCols];
		System.out.println("Height:"+iRows+"\nWidth:"+iCols);
		try {
			PixelGrabber pgObj =new PixelGrabber(rawImg,0,0,iCols,iRows,oneDPix,0,iCols);
			if(pgObj.grabPixels()&&((pgObj.getStatus()&(ImageObserver.ALLBITS)))!=0) {
				
			}
		} catch (InterruptedException e) {
			System.out.println("Pixel Grab failure");
			e.printStackTrace();
			System.exit(1);
		}
		//Here starts
	/*	image= new BufferedImage(iCols,iRows,BufferedImage.TYPE_INT_RGB);
		image.setRGB(0,0,iCols, iRows, oneDPix,0,iCols);
          try {
        	    // retrieve image
        	    File outputfile = new File("saved.png");
        	    ImageIO.write( image ,"bmp", outputfile);
        	} catch (IOException ef) {
        	   
        	}  */
          //Here ends
		System.out.println("Pixel grab succesfull");
	}
	
	//ConvertToThreeD
	public void convertToThreeD() {
		int[] aRow= new int[iCols];
		threeDPix=new int[iRows][iCols][4];
		int row,col;
		for(row=0;row<iRows;row++) {
			for(col=0;col<iCols;col++) {
		        int element = row * iCols + col;				
				aRow[col]=oneDPix[element];
			}
			for(col=0;col<iCols;col++) {
				
		        threeDPix[row][col][0] = (aRow[col] >> 24) & 0xFF;
//Red data
		        threeDPix[row][col][1] = (aRow[col] >> 16) & 0xFF;
//Green data
		        threeDPix[row][col][2] = (aRow[col] >> 8) & 0xFF;
//Blue data
		        threeDPix[row][col][3] = (aRow[col]) & 0xFF;
			}
		}
		System.out.println("Two D transformation successfull");
	}
	public void convertToOneD() {
		     oneDPixMod = new int[ iCols * iRows ];
		     for(int row = 0,cnt = 0;row < iRows;row++){
		       for(int col = 0;col < iCols;col++){
		         oneDPixMod[cnt] = ((threeDPixMod[row][col][0] << 24)
		                                    & 0xFF000000)
		                      | ((threeDPixMod[row][col][1] << 16)
		                                    & 0x00FF0000)
		                       | ((threeDPixMod[row][col][2] << 8)
		                                    & 0x0000FF00)
		                            | ((threeDPixMod[row][col][3])
		                                    & 0x000000FF);
		         cnt++;
		       }
		     } 
		     System.out.println("One D transformation successfull");
	}
	//
	public static void main(String[] args) throws Exception {
		
		try {
			chooseImage frame1=new chooseImage();
			frame1.setVisible(true);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
}
