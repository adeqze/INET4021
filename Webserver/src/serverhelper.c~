#include <stdio.h>
#include <string.h>
#include <time.h>
#include <sys/socket.h>
#include <errno.h>
#include <sys/stat.h>
#include "webServer.h"


void sendError(int socket, char *errMsg){//Error mesages send to Browser and then log to error log file
	send(socket, errMsg, strlen(errMsg), 0); 
	error_logging(-1, errMsg);
}

void error_logging(int i, char *msg){ //A function for logging server activities
	char *access_log; char *error_log;
	FILE *fpacc, *fperr;
	struct stat s;

	/*
	mkdir ("../log/accesslogs.log", 0700);
	mkdir("../log/errorlogs.log", 0700);
	*/
	access_log = "../log/accesslogs.log";  error_log = "../log/errorlogs.log";
	time_t t; 	
	time(&t);
	if (i==0){
		fpacc=fopen(access_log, "a+");
		fprintf(fpacc,"\nServer got Accessed on %s Request was %s",ctime(&t), msg);
		fclose(fpacc);
	}
	else if(i==-1){
		fperr = fopen(error_log, "a+");
		fprintf(fperr,"\nError Occured Due to : %s",msg);
		fclose(fperr);
	} 
}

