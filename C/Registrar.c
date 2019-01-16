/*
    @author
          ______         _                  _
         |  ____|       (_)           /\   | |
         | |__ __ _ _ __ _ ___       /  \  | | __ _ ___ _ __ ___   __ _ _ __ _   _
         |  __/ _` | '__| / __|     / /\ \ | |/ _` / __| '_ ` _ \ / _` | '__| | | |
         | | | (_| | |  | \__ \    / ____ \| | (_| \__ \ | | | | | (_| | |  | |_| |
         |_|  \__,_|_|  |_|___/   /_/    \_\_|\__,_|___/_| |_| |_|\__,_|_|   \__, |
                                                                              __/ |
                                                                             |___/
            Email: farisalasmary@gmail.com
            Date:  Apr 30, 2014
*/


/*
//http://bharathisubramanian.wordpress.com/2010/03/14/x11-fake-key-event-generation-using-xtest-ext/

Build and Run it using following commands:

gcc Registrar.c -o reg -lm -lX11 -lXtst -lXext 

**To run it
  ./reg
========================================
for UBUNTU before compiling:
 
sudo apt-get install libxtst-dev

========================================
CRNs for test : 
	20035	23659	23576	12548	12365	20035	23659	23576	12548	12365	

========================================


Look into /usr/include/X11/ keysymdef.h header file to know about key symbols of other keys.*/



#include <stdio.h>
#include <stdlib.h>
#include <X11/Xlib.h>
#include <X11/Intrinsic.h>
#include <X11/extensions/XTest.h>
#include <unistd.h>
#include <math.h>
#include <string.h>

#define RED     "\x1b[31m"
#define GREEN   "\x1b[32m"
#define YELLOW  "\x1b[33m"
#define BLUE    "\x1b[34m"
#define MAGENTA "\x1b[35m"
#define CYAN    "\x1b[36m"
#define RESET   "\x1b[0m"

/* Send Fake Key Event */

static void SendKey(Display * disp, KeySym keysym, KeySym modsym){
 KeyCode keycode = 0, modcode = 0;
 keycode = XKeysymToKeycode (disp, keysym);
 if (keycode == 0) return;
 XTestGrabControl (disp, True);
 /* Generate modkey press */
 if (modsym != 0) {
  modcode = XKeysymToKeycode(disp, modsym);
  XTestFakeKeyEvent (disp, modcode, True, 0);
 }
 /* Generate regular key press and release */
 XTestFakeKeyEvent (disp, keycode, True, 0);
 XTestFakeKeyEvent (disp, keycode, False, 0); 
 
 /* Generate modkey release */
 if (modsym != 0)
  XTestFakeKeyEvent (disp, modcode, False, 0);
 
 XSync (disp, False);
 XTestGrabControl (disp, False);
}


void splitInt(int num, int aryInt[], int size);
void printAry(int ary[], int size);
void reverseInt(int ary[], int size);
void sendkeys(int ary[], int size, Display *disp);
void readAry(int ary[], int size);
void receivekeys(int ary[],int CRN[], int size, Display *disp);
void welcome(void);
void readFromFile(char name[], int ary[]);
int  ArraySizeFromFile(char name[]);



int main(int argc, char *argv[]){
	int i, size, num;
 
		Display *disp = XOpenDisplay (NULL);
		welcome();

		if(argc < 2){

			do{
				printf("\n\n\n\n\n\nEnter the number of CRNs : > ");
				scanf("%d", &size);
			}while(size <= 0);

			num = 99;

		}else if(argc == 2)
			size = ArraySizeFromFile(argv[1]);

	
		int ary[size], CRN[size];

		if(argc == 2){
			readFromFile(argv[1], CRN);
			printf("CRNs have been read from file : %s \n\n ", argv[1]);
			printAry(CRN, size);
			num = 1; // MUST be equal to 1 to work
		}
	

		do{

			if(num == 0)
				break;
			
			if(num == 99)
				readAry(CRN, size);

			printf("\n\nPress \"Tab\" to display the numbers OR press \"ESC\" to Quit\n");
			receivekeys(ary, CRN, size, disp);


			printf("Do you want to continue?\n");
			printf("( 0 : No, 99 : change CRNs, 1 : YES ) : > ");
			scanf("%d", &num);
		}while(num == 1 || num == 99);
	
	return 0;

}


void welcome(void){

	printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	printf(YELLOW" ######                                                         #         ###   \n"RESET);
	printf(YELLOW" #     # ######  ####  #  ####  ##### #####    ##   #####      ##        #   #  \n"RESET);
	printf(YELLOW" #     # #      #    # # #        #   #    #  #  #  #    #    # #       #     # \n"RESET);
	printf(GREEN" ######  #####  #      #  ####    #   #    # #    # #    #      #       #     # \n"RESET);
	printf(GREEN" #   #   #      #  ### #      #   #   #####  ###### #####       #       #     # \n"RESET);
	printf(CYAN" #    #  #      #    # # #    #   #   #   #  #    # #   #       #   ###  #   #  \n"RESET);
	printf(BLUE" #     # ######  ####  #  ####    #   #    # #    # #    #    ##### ###   ###   \n"RESET);
	printf(RED"\n\tBy :"MAGENTA"\n\tFaris Abdullah Alasmary (c) 2014\n"RESET);
	printf("\n\n\n\n\n\n\n");
}


void readFromFile(char name[], int ary[]){
   int CRN, r, i;
	FILE *fp;
   fp = fopen(name, "r"); /* No need to check if the file exists because 
										we did that in function "ArraySizeFromFile" */

		i = 0;
		while(r != -1){
     		r = fscanf(fp, "%d", &ary[i]);
			if(r == -1) break; /* the Condition of the loop must be here to avoid
										 Logical Errors such as repeating the last digit*/	
				i++;	
			}

		fclose(fp);

}


void readAry(int ary[], int size){
	int i;

		for(i = 0; i < size; i++){
			printf("Enter CRN #%d : > ", (i + 1));
			scanf("%d", &ary[i]);
			}


}


int ArraySizeFromFile(char name[]){
    int r, i, size = 0, dummy;
    FILE *fp;
    fp = fopen(name, "r");
		if(fp == NULL){
			printf("Fatal Error : %s : No such file or directory.\n", name);
			exit(1);
		}

		for(i = 0; r != -1; i++){
			r = fscanf(fp, "%d", &dummy);// dummy for counting
			if(r == -1) break; /* the Condition of the loop must be here to avoid
									 Logical Errors such as repeating the last digit*/
			size++;
			}

		fclose(fp);
	
	return size;

}


void printAry(int ary[], int size){
	int i;

		for(i = 0; i < size; i++)
			printf("%d ", ary[i]);
		printf("\n");

}


void splitInt(int num, int aryInt[], int size){
	int i;

			size = 5;/*Set size to 5 is because that the CRN number is 5-digit number and
								I want to keep these function in general case to use it again and again!!*/
		for(i = size - 1  ; i >= 0; i--)
			aryInt[i] = num / (int) pow(10, i) % 10 ;
		reverseInt(aryInt, size);
}


void reverseInt(int ary[], int size){
	int i, j;
	int temp[size];
	//printf("\n\n%d\n\n", size);
	//reversing elements
	for(i = 0, j = size -1 ; j >= 0, i < size; i++, j--)
		temp[i] = ary[j];
	
	//storing elemnets in the same array again
	for(i = 0; i < size ;i++)
		ary[i] = temp[i];	
		

}


void receivekeys(int ary[], int CRN[], int size, Display *disp){

    Display * display;

    char szKey[32], szKeyOld[32] = {0}, szBit, szBitOld, szKeysym, *szKeyString;
    int  iCheck, iKeyCode, i, j, iReverToReturn, DeadLine = 0;
    Window focusWin;

    display = XOpenDisplay( NULL );

    if( display == NULL )
    {
        printf( "Error: XOpenDisplay in function receivekeys\n" );
        exit(-1);
    }

    while(iKeyCode != 9 || DeadLine < 1000 ){ // 9 means the ESC KEY
        XQueryKeymap( display, szKey );

        if( memcmp( szKey, szKeyOld, 32 ) != 0 ){ 
				// it was " ( memcmp( szKey, szKeyOld, 32 ) != NULL ) "

            for(i = 0; i < sizeof( szKey ); i++ ){
                szBit = szKey[i];
                szBitOld = szKeyOld[i];
                iCheck = 1;

              	  for (j = 0 ; j < 8 ; j++ ){
                     if ( ( szBit & iCheck ) && !( szBitOld & iCheck ) ){
                         iKeyCode = i * 8 + j ;

							if(iKeyCode == 23){			// 23 means the TAB KEY
								for(i = 0; i < size; i++){
									splitInt(CRN[i], ary, size);	
									sendkeys(ary, size, disp);
									if(i == 0)
										SendKey (disp, XK_Tab, 0);
										}

									printf("The numbers have been displayed\n");
									SendKey (disp, XK_Return, 0);

								}		

                     }
                    iCheck = iCheck << 1 ;
                }
            }
        }
      memcpy( szKeyOld, szKey, 32 );
		DeadLine++;
    }
    XCloseDisplay( display );

}


void sendkeys(int ary[], int size, Display *disp){
	int i;
    size = 5;/*Set size to 5 is because that the CRN number is 5-digit number and
                            I want to keep these function in general case to use it again and again!!*/
    for(i = 0; i < size; i++)
        switch(ary[i]){
            case 0:
                SendKey (disp, XK_0, 0);
                break;
            case 1:
                SendKey (disp, XK_1, 0);
                break;
            case 2:
                SendKey (disp, XK_2, 0);
                break;
            case 3:
                SendKey (disp, XK_3, 0);
                break;
            case 4:
                SendKey (disp, XK_4, 0);
                break;
            case 5:
                SendKey (disp, XK_5, 0);
                break;
            case 6:
                SendKey (disp, XK_6, 0);
                break;
            case 7:
                SendKey (disp, XK_7, 0);
                break;
            case 8:
                SendKey (disp, XK_8, 0);
                break;
            case 9:
                SendKey (disp, XK_9, 0);
                break;
            default:
                printf("Error : An error has occured inside \"sendkeys\" function!!\n");

        }
    SendKey (disp, XK_Tab, 0);
}



