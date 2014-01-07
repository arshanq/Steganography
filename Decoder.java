import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Decoder extends JFrame implements ActionListener {
	JButton Browse,Decode,bnoise;
	String msg,file;
	JLabel filename;
	JTextField noisethresh;
	JLabel msgarea;
	ImageDemo openImg;
	int MAXLEN=100000;
	char[] Msg=new char[MAXLEN];
	BufferedImage rawImage;
	int iRows,iCols,thresh;
	int[] oneDPix;
	int[][][] threeDPix;
	char[][][] segmentedMsg; 
	public Decoder(){
		setTitle("Decoder");
		setSize(400,300);
		setLayout(new GridLayout(3,2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Browse =new JButton("Browse");
		Decode= new JButton("Decode");
		filename= new JLabel("Filename appears here");
		noisethresh = new JTextField("Enter any number between 1 and 110");
		msgarea= new JLabel("Your msg appears here");
		bnoise = new JButton("Save threshold");
		add(filename);
		add(Browse);
		add(noisethresh);
		add(bnoise);
		add(msgarea);
		add(Decode);
		Browse.addActionListener(this);
		Decode.addActionListener(this);
		Decode.setVisible(false);
		bnoise.addActionListener(this);
		bnoise.setVisible(false);
	//	msgarea.setEditable(false);
		noisethresh.setEditable(false);
	}
	public static void main(String[] args) {
		Decoder obj= new Decoder();
		obj.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj=e.getSource();
		if(obj==Browse) {
			FileDialog fd=new FileDialog(new JFrame(),"File Dialog");
			fd.setVisible(true);
			file=fd.getFile();
			filename.setText(file);
			noisethresh.setEditable(true);
			bnoise.setVisible(true);
			try {
				openImg=new ImageDemo(file);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			rawImage=openImg.image;
		}
		else if(obj==Decode) {
			grabPixels();
			convertToThreeD();
			readData obj2=new readData(threeDPix,thresh,iRows,iCols);
			Msg=obj2.getMsg();
			msg=Msg.toString();
		//	msgarea.setEditable(true);
			msgarea.setText(msg);
			
		}
		else if(obj==bnoise) {
			thresh=Integer.parseInt(noisethresh.getText());
			Decode.setVisible(true);
		}
	}
	//Grab Pixels function
	public void grabPixels()  {
		iRows=rawImage.getHeight();
		iCols=rawImage.getWidth();
		oneDPix= new int[iRows*iCols];
		System.out.println("Height:"+iRows+"\nWidth:"+iCols);
		try {
			PixelGrabber pgObj =new PixelGrabber(rawImage,0,0,iCols,iRows,oneDPix,0,iCols);
			if(pgObj.grabPixels()&&((pgObj.getStatus()&(ImageObserver.ALLBITS)))!=0) {
				
			}
		} catch (InterruptedException e) {
			System.out.println("Pixel Grab failure");
			e.printStackTrace();
			System.exit(1);
		}
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
}
