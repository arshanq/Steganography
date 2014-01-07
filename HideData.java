public class HideData {
	int[][] segment;
	public int BLOCK_SIZE,blocks_in_a_row,blocks_in_a_col;
	int [][][] threeDPix;
	public HideData(int[][][] threeD,char[][][] segmentedMsg,int thresh,int noOfSegments,int iRows,int iCols) {
		int MsgSegmentsLeft=noOfSegments,writeSegment=0,i,j,k,a,b,x,y;
		threeDPix=new int[iRows][iCols][4];
		for(k=0;k<4;k++)
			for(i=0;i<iRows;i++)
				for(j=0;j<iCols;j++)
					threeDPix[i][j][k]= threeD[i][j][k];
		BLOCK_SIZE=8;
		 blocks_in_a_row=iCols/BLOCK_SIZE;
		 blocks_in_a_col=iRows/BLOCK_SIZE;
			segment= new int[BLOCK_SIZE][BLOCK_SIZE];
		for(k=1;k<4 && (MsgSegmentsLeft>0);k++) {
					//Analyze each plane segment by segment
					for(y=0;y<blocks_in_a_col && (MsgSegmentsLeft>0);y++) {
						for(x=0;x<blocks_in_a_row && (MsgSegmentsLeft>0);x++) {
							System.out.println("Coordinates: "+(y*8)+" "+(x*8));
							for(i=(y*BLOCK_SIZE),a=0;i<(y*BLOCK_SIZE+8);i++,a++) {
								for(j=(x*BLOCK_SIZE),b=0;j<(x*BLOCK_SIZE+8);j++,b++) {
									segment[a][b]=(threeDPix[i][j][k]&0x1);
									System.out.print(segment[a][b]);
								}
							System.out.println();
							}
							if(isComplex(segment,thresh)==1) {
							//	display(x,y,k);
								reWrite(segmentedMsg,writeSegment,x,y,k);
								findComplexity(x,y,k);
								writeSegment++;
								MsgSegmentsLeft--;
							}
						}
					}
		}
	}
	public void findComplexity(int x,int y,int k) {
		int i,j,a,b;
		int [][] arr=new int[8][8];
		i=y*8;
		for(a=0;a<8;a++,i++)
			for(b=0,j=(x*8);b<8;b++,j++)
				arr[a][b]=(threeDPix[i][j][k]&0x1);
		int transitions=0;
		for(i=2;i<BLOCK_SIZE;i++) {
			if(arr[i][0]!=arr[i-1][0]) transitions++;
			if(arr[0][i]!=arr[0][i-1]) transitions++;
		}			
		for(i=1;i<BLOCK_SIZE;i++) {
			for(j=1;j<BLOCK_SIZE;j++) {
				if(arr[i][j]!=arr[i-1][j]) transitions++;
				if(arr[i][j]!=arr[i][j-1]) transitions++;
			}
		}
		System.out.print("location "+(y*8)+" "+(x*8)+" "+(k)+" ");
		System.out.println("findComplexity: "+transitions);
	}
	public int isComplex(int[][] arr,int thresh) {
		int i,j;
		int transitions=0;
			for(i=2;i<BLOCK_SIZE;i++) {
				if(arr[i][0]!=arr[i-1][0]) transitions++;
				if(arr[0][i]!=arr[0][i-1]) transitions++;
			}			
			for(i=1;i<BLOCK_SIZE;i++) {
				for(j=1;j<BLOCK_SIZE;j++) {
					if(arr[i][j]!=arr[i-1][j]) transitions++;
					if(arr[i][j]!=arr[i][j-1]) transitions++;
				}
			}
			System.out.println("isComplex: "+transitions);
		if(transitions>thresh) return 1;
		else return 0;
	}
	public void display(int x,int y,int k) {
		int i,j,a,b;
		i=y*8;
		System.out.println("Display"+(y*8)+" "+(x*8)+" "+(k)+" ");
		for(a=0;a<8;a++,i++) {
			for(b=0,j=(x*8);b<8;b++,j++) {
				System.out.print((threeDPix[i][j][k]&0x1));
			}
			System.out.println();
		}
	}
	public void reWrite(char[][][] Msg,int count,int x,int y,int k) {
		int i,j,a,b;
		i=(y*8);;
		for(a=0;a<8;i++,a++) {
			for(b=0,j=(x*8);b<8;j++,b++) {
				if(Msg[a][b][count]=='1') {
					threeDPix[i][j][k]=(threeDPix[i][j][k] | 0x1);
				//	if((threeDPix[i][j][k]%2)==0) threeDPix[i][j][k]++;
				}
				else  {
					threeDPix[i][j][k]=(threeDPix[i][j][k] & 0xFFFFFFFE);
				//	if((threeDPix[i][j][k]%2)!=0) threeDPix[i][j][k]--; 				
				}
			//	System.out.println("Inside rewrite: "+(i)+" "+j+"Msg: "+Msg[a][b][count]);
			}
		}
      
		//Check if Pixels Array is updated with message or not
		for(i=(y*8),a=0;i<(y*8+8);i++,a++) {
			for(j=(x*8);j<(x*8+8);j++) {
				System.out.print(((threeDPix[i][j][k]&0x1)));
			}
			System.out.print("\t");
			for(j=0;j<8;j++)
				System.out.print(Msg[a][j][count]);
			System.out.println();
		}
		display(x,y,k);
	}
	public int[][][] getthreeDPix() {
		return threeDPix;
	}
}
