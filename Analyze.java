import javax.swing.JOptionPane;


public class Analyze {
	byte[][][] plane0,segment;
	public int BLOCK_SIZE,complex_segments,x,y,a,b,blocks_in_a_row,blocks_in_a_col;
	public Analyze(int[][][] threeD,int rows,int cols,int alpha) {
		int i,j;
		plane0= new byte[rows][cols][4];
		for(i=0;i<rows;i++) {
			for(j=0;j<cols;j++) {
				//nothing in the alpha array
				plane0[i][j][0]=0;
				//R
				plane0[i][j][1]=(byte)(threeD[i][i][1] & 0x1);
				//G
				plane0[i][j][2]=(byte)(threeD[i][j][2] & 0x1);
				//B
				plane0[i][j][3]=(byte)(threeD[i][j][3] & 0x1);
			}
		}
		//Initialize variables
		BLOCK_SIZE=8;
	    complex_segments=0;
	    blocks_in_a_row=cols/BLOCK_SIZE;
	    blocks_in_a_col=rows/BLOCK_SIZE;
		segment= new byte[BLOCK_SIZE][BLOCK_SIZE][3];
		//Analyze each plane segment by segment
		for(y=0;y<blocks_in_a_col;y++) {
			for(x=0;x<blocks_in_a_row;x++) {
				for(i=(y*BLOCK_SIZE),a=0;i<(y*BLOCK_SIZE+8);i++,a++) {
					for(j=(x*BLOCK_SIZE),b=0;j<(x*8 + 8);j++,b++) {
						//r
						segment[a][b][0]=plane0[i][j][1];
						//g
						segment[a][b][1]=plane0[i][j][2];
						//b
						segment[a][b][2]=plane0[i][j][3];
					}
				}
				complex_segments+=isComplex(segment,alpha);
			}
		}
		JOptionPane.showMessageDialog(null,complex_segments+"complex segments found\n"+((complex_segments*BLOCK_SIZE)/1024)+"kB Available");
	}
	public int isComplex(byte[][][] arr,int thresh) {
		int i,j,k,count=0,a;
		int[] transitions= new int[3];
		transitions[0]=transitions[1]=transitions[2]=0;
		for(k=0;k<3;k++) {
			for(i=2;i<BLOCK_SIZE;i++) {
				if(arr[i][0][k]!=arr[i-1][0][k]) transitions[k]++;
				if(arr[0][i][k]!=arr[0][i-1][k]) transitions[k]++;
			}			
			for(i=1;i<BLOCK_SIZE;i++) {
				for(j=1;j<BLOCK_SIZE;j++) {
					if(arr[i][j][k]!=arr[i-1][j][k]) transitions[k]++;
					if(arr[i][j][k]!=arr[i][j-1][k]) transitions[k]++;
				}
			}
		}
		a=thresh;
		for(k=0;k<3;k++) {
//			System.out.println(transitions[k]);
			if(transitions[k]>a)
				count++;
		}
		return count;
	}
}
