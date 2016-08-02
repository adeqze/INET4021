all: 	webServer cgi move 
	
move: 
	mv webServer ../cgi-bin/
	mv cgi ../cgi-bin/

cgi: 	
	g++ cgi.cpp -o cgi

webServer: webServer.o helper_funcs.o
	gcc -o webServer webServer.o helper_funcs.o

helper_funcs.o: helper_funcs.c webServer.h
	gcc -c helper_funcs.c

webServer.o: webServer.c webServer.h
	gcc -c webServer.c
 
clean:
	rm ../cgi-bin/webServer webServer.o helper_funcs.o ../cgi-bin/cgi



