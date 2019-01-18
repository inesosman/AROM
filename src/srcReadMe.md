You can expand the JVM while running  by putting these JVM arguments : -Xms1024m -Xmx2048m
or -Xms2048m -Xmx4096m if you want more.

If you use HermiT in the , you need to put the following JVM arguments : -Xms2048m -Xmx4096m -Xss208m
because HermiT needs more time time to compute all infrences, unlike ELK.
