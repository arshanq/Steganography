public class transformMsg {
	int[][] secretFile,conjugate_map;
	int Msglen,binaryDataLen,noOfSegments; 
	int BLOCK_SIZE;
	int MAXLEN=100000;
	char[][][] segmentedMsg=new char[8][8][10000];
	public transformMsg(String Msg,int thresh) {
		Msglen=Msg.length();
		BLOCK_SIZE=8;			
		char[] str=new char[Msglen];
		char[] binaryStr= new char[MAXLEN];
		char[] bitStuffedStr= new char[MAXLEN];
		str=Msg.toCharArray();
		System.out.println(str);
		binaryStr=toBinary(str); 
		bitStuffedStr=bitStuffing(binaryStr);
		System.out.println("Length of binary data: "+binaryDataLen);
		segmentedMsg=toSegments(bitStuffedStr);
		System.out.println("No. of Msg Segments: "+noOfSegments);
		displaySegments();
		setConjugateBits(thresh);
		displaySegments();
	}
	char[] toBinary(char[] str) {
		int i,cnt=0,j,tmp;
		char[] newStr=new char[MAXLEN];
		char[] ch=new char[8]; 
		for(i=0;i<Msglen;i++) {
			tmp=(int)str[i];
			//ch[0]=LSB
			for(j=0;j<8;j++) {
				if((tmp & 0x1)==1) 
					ch[j]='1';
				else
					ch[j]='0';
				tmp>>=1;
			}
	//		System.out.println(ch);
			//Written in the format MSB to LSB---->left to right
			for(j=7;j>=0;j--) {
				newStr[cnt++]=ch[j];
			}
		}
		newStr[cnt]='\n';
		System.out.println("Length before Bit stuffing: "+cnt);
		return newStr;
	}
	char[] bitStuffing (char[] str) {
		char[] newStr= new char[MAXLEN];
		String s="01111110";
		char[] flag=new char[8];
		flag=s.toCharArray();
		int i,cnt=0,ones=0;
		for(i=0;i<8;i++)
			newStr[cnt++]=flag[i];
		i=0;
		while(str[i]!='\n') {
			newStr[cnt++]=str[i];
			if(str[i]=='1') {
				ones++;
			}
			else {
				ones=0;
			}
			if(ones==5) {
				newStr[cnt++]='0';
				ones=0;
			}
			i++;
		}
		for(i=0;i<8;i++)
			newStr[cnt++]=flag[i];
		newStr[cnt]='\n';
	//	System.out.println(newStr);
		binaryDataLen=cnt;
		return newStr;
	}
	char[][][] toSegments(char[] str) {
		char[][][] newStr= new char[BLOCK_SIZE][BLOCK_SIZE][10000];
		int cnt=0,i,j,k=0,write=1;
		while(write==1) {
			for(j=1;j<BLOCK_SIZE&&str[cnt]!='\n';j++)
				newStr[0][j][k]=str[cnt++];
			for(i=1;i<BLOCK_SIZE;i++) {
				for(j=0;j<BLOCK_SIZE&&str[cnt]!='\n';j++) {
					newStr[i][j][k]=str[cnt++];
				}
			}
			//Fill the remaining segment with 0's
			for(i=0;i<8;i++) {
				for(j=0;j<8;j++) {
					if((newStr[i][j][k]!='0')&&(newStr[i][j][k]!='1')) newStr[i][j][k]='0';
				}
			}
			k++;
			if(str[cnt]=='\n') write=0;
			noOfSegments=k;
		}
		return newStr;
	}
	void setConjugateBits(int thresh) {
		int k;
		for(k=0;k<noOfSegments;k++) {
			if(isComplex(thresh,k)==1) {
				segmentedMsg[0][0][k]='0';
			}
			else {
				Conjugate(k,thresh);
				segmentedMsg[0][0][k]='1';
			}
		}
	}
	public int isComplex(int thresh,int k) {
		int i,j;
		int transitions= 0;
			for(i=2;i<BLOCK_SIZE;i++) {
				if(segmentedMsg[i][0][k]!=segmentedMsg[i-1][0][k]) transitions++;
				if(segmentedMsg[0][i][k]!=segmentedMsg[0][i-1][k]) transitions++;
			}			
			for(i=1;i<BLOCK_SIZE;i++) {
				for(j=1;j<BLOCK_SIZE;j++) {
					if(segmentedMsg[i][j][k]!=segmentedMsg[i-1][j][k]) transitions++;
					if(segmentedMsg[i][j][k]!=segmentedMsg[i][j-1][k]) transitions++;
				}
			}
			System.out.println("Segment:"+k+" :Complexity: "+transitions);
		return (transitions>thresh?1:0);
	}
	public void Conjugate(int k,int thresh) {
		byte[][] wc=new byte[8][8];
		byte tmp=0,i,j;
		for(i=0;i<8;i++) {
				wc[0][i]=(byte) (tmp%2);
				tmp++;
		}
		for(i=1;i<8;i++) {
			for(j=0;j<8;j++) {
				wc[i][j]=(byte) ((wc[i-1][j]+1)%2);
//				System.out.print(wc[i][j]);
			}
//			System.out.println();
		}
		
		for( i=0;i<8;i++) {
			for(j=0;j<8;j++) {
				segmentedMsg[i][j][k]=(char) ((segmentedMsg[i][j][k]^wc[i][j]));
			//	System.out.println(segmentedMsg[i][j][k]);
			}
		}
		//Find Conjugate matrix complexity
		int transitions= 0;
			for(i=2;i<BLOCK_SIZE;i++) {
				if(segmentedMsg[i][0][k]!=segmentedMsg[i-1][0][k]) transitions++;
				if(segmentedMsg[0][i][k]!=segmentedMsg[0][i-1][k]) transitions++;
			}			
			for(i=1;i<BLOCK_SIZE;i++) {
				for(j=1;j<BLOCK_SIZE;j++) {
					if(segmentedMsg[i][j][k]!=segmentedMsg[i-1][j][k]) transitions++;
					if(segmentedMsg[i][j][k]!=segmentedMsg[i][j-1][k]) transitions++;
				}
			}
			if(transitions<=thresh) {
				System.out.println("Low threshld of Conjugate Matrix.\nProgram terminating");
				System.exit(1);
			}
		System.out.println("Complexity of conjugate matrix: "+k+":"+transitions);
	}
	public void displaySegments() {
		int i,j;
		for(i=0;i<8;i++) {
			for(int k=0;k<noOfSegments;k++) {
				for(j=0;j<8;j++)
					System.out.print(segmentedMsg[i][j][k]);
				System.out.print("\t");
			}

			System.out.print("\n");
		}
	}
	int getnoOfSegments() {
		return noOfSegments;
	}
	char[][][] getMsgSegments() {
		return segmentedMsg;
	}
	public static void main(String[] args) {
		new transformMsg("Hi this is arshan",30);
	}
}
