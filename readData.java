public class readData {
	char [][][] segmentedMsg;
	byte[][][] plane0;
	int[][] segment,wc;
	char[] binaryStr,bstr;	
	int MAXLEN=100000;
	public int BLOCK_SIZE,blocks_in_a_row,blocks_in_a_col;
	public readData(int[][][] threeD,int thresh,int iRows,int iCols) {
		int nOfSegments,i,j,k,a,b,x,y,done=0,len=0,ones=0,mark=0;
		plane0= new byte[iRows][iCols][4];
		binaryStr=new char[MAXLEN];
		bstr=new char[MAXLEN];
		BLOCK_SIZE=8;
		blocks_in_a_row=iCols/BLOCK_SIZE;
	    blocks_in_a_col=iRows/BLOCK_SIZE;
		segment= new int[BLOCK_SIZE][BLOCK_SIZE];
		ConjugateMatrix();
		for(k=1;k<2&&(done==0);k++) {
			for(y=0;y<blocks_in_a_col &&(done==0);y++) {
				for(x=0;x<blocks_in_a_row&&(done==0);x++) {
				//	System.out.println("Coordinates: "+(y*8)+" "+(x*8));
					for(i=(y*BLOCK_SIZE),a=0;i<(y*BLOCK_SIZE+8);i++,a++) {
						for(j=(x*BLOCK_SIZE),b=0;j<(x*BLOCK_SIZE+8);j++,b++) {
							segment[a][b]=(threeD[i][j][k]&0x1);
						//	System.out.print(segment[a][b]);
						}
					//	System.out.println();
					}
					System.out.print("Location "+(y*8)+" "+(x*8)+" "+k);
					if(isComplex(segment,thresh)) {
						if(segment[0][0]==1) {
							for( i=0;i<8;i++) {
								for(j=0;j<8;j++) {
									segment[i][j]= (segment[i][j]^wc[i][j]);
								//	System.out.print(segment[i][j]);

								}
								//	System.out.println();
							}
						}
						for(a=1;a<8&&(mark!=2);a++) {
							if(segment[0][a]==1) {
								binaryStr[len++]='1';
								ones++;
							}
							else {
								if(ones==5) {
								}
								else
								binaryStr[len++]='0';
								ones=0;
							}
							if(ones==6) {
								mark++;
								System.out.println("Mark found in: "+(y*8)+" "+(x*8)+" "+k);
							}
						}
						for(a=1;a<8&&(mark!=2);a++) {
							for(b=0;b<8&&(mark!=2);b++) {
								if(segment[a][b]==1) {
									binaryStr[len++]='1';
									ones++;
								}
								else {
									if(ones==5) {
									}
									else
									binaryStr[len++]='0';
									ones=0;
								}
								if(ones==6) {
									mark++;
									System.out.println("Mark found in: "+(y*8)+" "+(x*8)+" "+k);
								}			
							}
						}
						if(mark==2) {
							done=1;
							binaryStr[len++]='0';
							binaryStr[len]='\n';
							System.out.println("len: "+len);
						}
					}
				}
			}
		}
		bstr=readBinary(binaryStr);
	}
	public boolean isComplex(int [][] arr,int thresh) {
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
			System.out.println(" transitions "+transitions);
		if(transitions>thresh) return true;
		else return false;
	}
	public void ConjugateMatrix() {
		wc=new int[8][8];
		byte tmp=0,i,j;
		for(i=0;i<8;i++) {
				wc[0][i]= (tmp%2);
				tmp++;
		}
		for(i=1;i<8;i++) {
			for(j=0;j<8;j++) {
				wc[i][j]= ((wc[i-1][j]+1)%2);
//				System.out.print(wc[i][j]);
			}
//			System.out.println();
		}
	
	}
	char[] getMsg() {
		return bstr;
	}
	char[] readBinary(char[] str) {
	char[] newStr=new char[MAXLEN];
	char[] ch=new char[8];
	int i=0,tmp,j,cnt=0;
	while(str[i]!='\n') {
		//0->Msb 7->Lsb
		for(j=0;j<8;j++) {
			ch[j]=str[i++];
		}
		tmp=0;
		for(j=0;j<8;j++) {
			tmp<<=1;
			if(ch[j]=='1')
				tmp++;
		}
		newStr[cnt++]=(char)tmp;
//			System.out.println(tmp);
	}
	System.out.println(newStr);
	return newStr; 
	}

}
