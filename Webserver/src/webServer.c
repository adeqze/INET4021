/* Webserver Source code
 *Authors:
	  Mohamed Yunis: noorx004@umn.edu
	  Mohamoud Egal: egalx011@umn.edu
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <signal.h>
#include <time.h>
#include <errno.h>
#include <signal.h>
#include "webServer.h"


void callCGI(int socket, char *args[]){
	close(1);
	dup2(socket,1);
	int rc = execvp("./cgi", args);	
	if(rc < 0)//in the case exec fails
		sendError(socket,"500 Internal Server Error"); //send this to be logged
}
 //receiving function through socket. 
int recieve(int socket){
	char *url;
	char buffer[BUFSIZE];
	memset (buffer,'\0', BUFSIZE);
	ssize_t sSize;
	if (socket<0) {
		perror("Socket error:\n"); //show on terminal the failure
		{  
		error_logging(-1,strerror(errno)); //log the error  
		return -1;
			}
		}
	sSize = recv(socket, buffer, BUFSIZE, 0);
	if (sSize < 0) {
		perror("receving error: \n"); //Display on terminal
		{ 
		error_logging(-1,strerror(errno)); //log error on file
		return -1;
			}
		}
	else {
		url = malloc(BUFSIZE); //space for buffer
		strcpy(url,buffer); //put url to buffer
		char *request[]={url,NULL};    
		callCGI(socket, request); //call the cgi request as argument
		return 0;
	}
}

//avoiding zombie process
void catch_sig(int sig){//catch child's termination Signal
	int status;
	while(wait3(&status, WNOHANG,(struct rusage *)0)>=0);
}

//Main 
void main()
{	

	int sock,new_sock; int port; int backlog;
	//server setup 
	fprintf(stderr, "Setting up server .......\n");	
	FILE *fp;
	char *root; char *conf_file;  char *line = malloc(100);
	root = malloc(100);
	conf_file = "../conf/httpd.conf";
	fp = fopen(conf_file, "r");
	if (fp == NULL)
	{
	fprintf(stderr, "Error opening config file for reaading\n"); //display error 
	error_logging(-1,strerror(errno));  exit(1); //then log
	}
	fscanf(fp, "%s %s", line, root); //get root
	fscanf(fp, "%s %i", line, &port); //get port
	fscanf(fp, "%i", &backlog); //get maximum number of connections (backlog)
	fclose(fp); //close file
	free(line); //free the memory

	//Display the information
	fprintf(stderr, "Server has been set up\n\n");
	fprintf(stderr, "port pulled from file is: %d\n", port);
	fprintf(stderr, "Starting root or index is: %s\n\n", root);
	//server setup done. 
	struct sockaddr_in address;
	address.sin_family = AF_INET;
	address.sin_addr.s_addr = INADDR_ANY;
	address.sin_port = htons(port);
	int addrlen;
	addrlen = sizeof(address);


	fprintf(stderr, "Creating socket .......\n");

	sock = socket(AF_INET, SOCK_STREAM, 0); //Creat the socket
	if(sock == -1 ){ 
	perror("Error creating socket: \n"); //test socket creation
		{
		error_logging(-1,strerror(errno));  //log in error
			exit(-1);
		}
	}
/*
 bzero((char *)&address, sizeof(address));

        address = gethostbyname("localhost");
        bcopy((char *)address->h_addr,(char *)&serv_addr.sin_addr.s_addr,address->h_length);

*/
	fprintf(stderr, "Socket created\n\n");
	fprintf(stderr, "Binding socket .......\n");
	if (bind(sock, (struct sockaddr *) &address, sizeof(address)) != 0){ //bind 
	perror("Error binding socket:\n"); //test binding
		{ error_logging(-1,strerror(errno)); 
		exit(-1); 
		} //log if fails
	}
	fprintf(stderr, "Socket bound to port: %d\n\n", port);

	fprintf(stderr, "Listening for connections ..........\n");
	fprintf(stderr, "Maximum allowable connections(backlog) are: %d\n", backlog);

	
	if (listen(sock, backlog) < 0){ //Listen for connections
	perror("Error listening: \n");   //Test connection
		{ error_logging(-1,strerror(errno)); 
		exit(-1); 
		} //Log if fail
	}

	(void) signal(SIGCHLD, catch_sig);//Eliminate a zombie process 
	
	while (1){//accept connections in an infinite loop
	new_sock = accept(sock, (struct sockaddr *)&address, &addrlen);
	if ( new_sock < 0 ){
	perror("Error accepting: \n");
	 {error_logging(-1,strerror(errno));
	}
	} 
	fprintf(stderr, "Server Accepted a connection..\n");
	switch (fork())   { //fork
	case 0: //child process
		close(sock);//close child's listening socket inherited from parent
		recieve(new_sock);
		exit(0);
	default: //parent
		close(new_sock);
		break; 
	case -1: //fork failed
		error_logging(-1,strerror(errno)); //log error
		exit(-1);   //exit
	}
	}
}


