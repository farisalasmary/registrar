# Registrar

The program basically posts Course Reference Numbers (CRNs) faster inside the CRN text boxs to help student getting courses' sections that he wants before it is filled and closed.

This program helped me a lot through out all years I studied at King Fahd University of Petroleum and Minerals (KFUPM). I developed the C language version back in 2014. After two years, I developed the Java language version with a graphical user interface (GUI). I used [JNativeHook](https://github.com/kwhat/jnativehook) to capture keystrokes from the user and generate keystrokes according to the CRNs that the program is provided with.

## C Language Version

The C langauge version is developed and tested on Ubuntu.

Install the following package before compiling the source code on Ubuntu:

```C
sudo apt-get install libxtst-dev
```

Then compile the code using the following command:

```C
gcc Registrar.c -o reg -lm -lX11 -lXtst -lXext
```

Create a new txt file, open it and enter your CRNs inside it separated by spaces.

For example, `crns.txt` with the following values:

```C
20035	23659	23576	12548	12365	20035	23659	23576	12548	12365	
```

Finally, run the program:

```C
./reg crns.txt
```

## Java Language Version

The easiest way is to import the project using Eclipse IDE.