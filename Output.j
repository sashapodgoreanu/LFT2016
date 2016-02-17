.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static printBool(I)V
 .limit stack 3
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 bipush 1
 if_icmpeq Ltrue
 ldc "false"
 goto Lnext
 Ltrue:
 ldc "true"
 Lnext:
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static printInt(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 0
 istore 0
 ldc 1
 istore 1
L0:
 iload 0
 iload 1
 if_icmplt L1
 ldc 0
 goto L2
L1:
 ldc 1
L2:
 ldc 0
 if_icmpeq L3
 iload 0
 iload 1
 if_icmplt L4
 ldc 0
 goto L5
L4:
 ldc 1
L5:
 ldc 0
 if_icmpeq L6
 ldc 0
 istore 0
 ldc 0
 istore 1
 goto L7
L6:
 ldc 0
 istore 0
 ldc 0
 istore 1
L7:
 iload 0
 ldc 1
 iadd 
 istore 0
 goto L0
L3:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

