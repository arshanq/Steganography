import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;
/*public class ImageDemo
{
	JFrame editorFrame;
	JLabel jLabel;
	ImageIcon imageIcon;
    BufferedImage image = null;
    // String filename;
  public ImageDemo(final String filename) throws Exception
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        editorFrame = new JFrame("Image Demo");
        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        try
        {
          image = ImageIO.read(new File(filename));
        }
        catch (Exception e)
        {
          e.printStackTrace();
          System.exit(1);
        }
        imageIcon = new ImageIcon(image);
        jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

        editorFrame.pack();
        editorFrame.setLocationRelativeTo(null);
        editorFrame.setVisible(true);
      }
    });
  }
}*/
public class ImageDemo extends JFrame {
	  BufferedImage image = null;
	  ImageIcon imgicon;
	  JLabel jLabel;
	  public ImageDemo(final String filename) throws Exception
	  {
		 // final String filename="Capture.jpg";
		 /* setTitle("Image Demo");
		  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      try
	      {
	        image = ImageIO.read(new File(filename));
	      }
	      catch (Exception e)
	      {
	        e.printStackTrace();
	        System.exit(1);
	      }
		  imgicon=new ImageIcon(image);
		  jLabel=new JLabel();
		  jLabel.setIcon(imgicon);
		  getContentPane().add(jLabel,BorderLayout.CENTER);
		  pack();
		  setLocationRelativeTo(null);
		  setVisible(true);*/
	        JFrame editorFrame = new JFrame("Image Demo");
	        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        try
	        {
	          image = ImageIO.read(new File(filename));
	        }
	        catch (Exception e)
	        {
	          e.printStackTrace();
	          System.exit(1);
	        }
	        imgicon = new ImageIcon(image);
	        jLabel = new JLabel();
	        jLabel.setIcon(imgicon);
	        editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

	        editorFrame.pack();
	        editorFrame.setLocationRelativeTo(null);
	        editorFrame.setVisible(true);
	  }
	}
