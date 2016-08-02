/* CGI Source code
 *Authors:
	  Mohamed Yunis: noorx004@umn.edu
	  Mohamoud Egal: egalx011@umn.edu
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <time.h>
#include <errno.h>
#include  <fcntl.h>
#include <iostream>
using namespace std;

#define BUFSIZE 1024


char *requestFile, *mimeType, *web; 
char *tokens[5]; 
char *url;
FILE *fp;

void serverConfig_func(){ //confif_file and root directory
	char const *config_file;
	char *line;
	line = (char *)malloc(100);
	web = (char*)malloc(100);
	config_file = (char *)malloc(50);
	config_file = "../conf/httpd.conf";
	fp = fopen(config_file, "r");
	if(fp == NULL){perror("Opening config_file:\n");}
	fscanf(fp, "%s %s", line, web);
	fclose(fp);
	free(line); //free malloc
	
}
 //Log activities and errors
void server_logging_func(int i, char *msg){
	char const *access_log, *error_log;
	FILE *fpacc, *fperr;
	access_log = "../log/access.log";  error_log = "../log/error.log";
	time_t t; 	
	time(&t);
	if (i==0){
	fpacc=fopen(access_log, "a+");
	if(fpacc == NULL){perror("Opening access:\n");} //test file status
	//fprintf(stderr, "Successfully created the access_log file\n");
	fprintf(fpacc,"\nTime accessed is: %s Request message was %s %s",ctime(&t), tokens[0], msg);
	fclose(fpacc);
	}
	else if(i==-1){
	fperr = fopen(error_log, "a+");
	if(fperr == NULL){perror("Opening error_log:\n");} //test file status
	//fprintf(stderr, "Successfully created the error_log file\n");
	fprintf(fperr,"%s occured on %s ",msg, ctime(&t));
	fclose(fperr);
	} 
}
 //What type of request: POST or GET
int requestMethod_func(char *request){ 
	int i=0; char *substr;
	if(strlen(request)>0){
		for (char *ptr=strtok(request," "); ptr!= NULL; ptr=strtok(NULL, " "))//tokenize url
			{ char *pass=strdup(ptr); tokens[i]=pass; i++; 	}
	}
	requestFile=(char*)malloc(BUFSIZE);
	strcpy(requestFile,tokens[1]);
	if(strcmp("GET", tokens[0])==0) 
		return 1;
	else if (strcmp("POST", tokens[0])==0)
		return 2;
	else if (strcmp("HEAD", tokens[0])==0)
		return 3;
	else 
		return -1;
}

//check request to meet extension. Validate
int urlCheck_func(char *request){ 
	char *mime;
	if(strstr(request,"/cgi-bin/")==NULL) return -1;
	if(strstr(request,".")==NULL) return -1;
		mime=strtok(request, "."); 
		mime=strtok(NULL, ".");
	if(mime==NULL) return -1;
	else {mimeType=mime; return 0;}	
}

//text, gif, jpeg/jpg, webpages/htm/html. Otherwise not supported. 
char * contentGet_func(char *type){
	char *text =  new char[11];
	char *gif = new char[11];
	char *jpeg = new char[11];
	char *okay_not_found = new char[41];
	strcpy(text, "text/html"); //text
	strcpy(gif, "image/gif"); //gif
	strcpy(jpeg, "image/jpeg"); //jpeg or jpg
	strcpy(okay_not_found, "Error 400 The extension type not supported!");
	if (strcmp(type,"html")==0 || strcmp(type,"htm")==0) return text;
	else if (strcmp(type,"gif")==0 ) return gif;
	else if (strcmp(type,"jpeg")==0 || strcmp(type,"jpg")==0 || strcmp(type,"jpe")==0) return jpeg;
	else return okay_not_found;
}

//Mime checking
char * mimeCheck_func(char *request){
	char const *mime_file; 
	char *retValue = new char[23]; char *line; size_t len=0, read; 
	strcpy(retValue, "Error 400 Bad Request");
	if(urlCheck_func(request)<0) return retValue;
	else{
		mime_file= "../conf/mime.type";
		fp = fopen(mime_file, "r");
		if(fp == NULL){
		perror("Error opening mime_file: \n");}
		//fprintf(stderr, "Successfully opened the mime_file for reading\n");
	while((read = getline(&line, &len, fp)) != -1){
	if((strstr(line,"#")==NULL)){ 
		if (strstr(line,mimeType)!=NULL) {  retValue = contentGet_func(mimeType); fclose(fp); return retValue; } 
		} 
	}
	}
	return retValue;
}

//Functin to get the file
char * file_getting_func(char *request){
	FILE *fp; int i, j=0; char *path; char *fileName;
	size_t len; 
	serverConfig_func();
	fileName=(char *)malloc(BUFSIZE);
	strcpy(fileName,request);
	for(i=strlen("/cgi-bin/");i<strlen(request);++i){
		if(j<i) { fileName[j]=fileName[i]; j++; }			
	}
	fileName=strndup(fileName, strlen(fileName)-strlen("/cgi-bin/"));
	path = (char *)malloc(BUFSIZE);
	strcpy(path,web); strcat(path,fileName);
	path=strndup(path, strlen(web)+strlen(fileName));
	return path;
	free(path);
	free(fileName);		
}
 //the Http Header,
void write_Head_func(char *type, int len){
	 cout<<"\nHTTP/1.0 200 OK";
	 cout<<"\nContent-Type: %s", type;
	 cout<<"\nServer: INET-Server v/0.1";
	 cout<<"\nContent-Length: %i", len;
	 cout<<"\n\n";
}

//write files on socket/STD OUT
char * fileWrite_func(char *file){
	FILE *webFp; char ch; 
	webFp = fopen(file,"r");
	if(webFp == NULL){
	perror("Error opening webFP:\n");}
	//fprintf(stderr, "Sucessfully opened the webFP file for reading\n");
	do { 
		ch = fgetc(webFp); 
	if( feof(webFp) ) break ;
		printf("%c", ch);
	}while(1);
	fclose(webFp);
}

//the size of the file
int fileSize_func(char *file){
	int size=0;
	struct stat st;
	stat(file, &st);
	size = st.st_size;
	return size;
}
//found/OK or not found
char * status_func(){
	char *notFound = new char[21];
	char *ok = new char[7];
	strcpy(notFound,"Error 404 Not Found"); 
	strcpy(ok,"200 OK"); 
	struct stat st;
	int result = stat(file_getting_func(requestFile), &st);	
	if(result==0 ) return ok;
	else  return notFound;
}

//Http GET
void GET_http(){
	char *mimeContent; char *fileStatus= new char[21];
	strcpy(fileStatus,"Error 404 Not Found");
	mimeContent=(mimeCheck_func(tokens[1]));
	if(strstr(mimeContent,"400")!=NULL) { 
		printf("HTTP/1.0 %s \nContent Type: text/plain\n\n%s",mimeContent,mimeContent); 
		server_logging_func(-1,mimeContent);  
		}
	else if (strcmp(status_func(), fileStatus)==0) { 
		printf("HTTP/1.0 %s \nContent Type: text/plain\n\n%s",status_func(),status_func()); 
		server_logging_func(-1,fileStatus); 
		}
		else 
		{ 	
			server_logging_func(0,file_getting_func(requestFile));
			write_Head_func(mimeContent, fileSize_func(file_getting_func(requestFile)) );
			fileWrite_func( file_getting_func(requestFile) );
		}
}

//Http POST
void POST_http(char *buffer){
	char *values; char *line; size_t len=0, read;
	char *name; char *key; int salary;
	values=(char*)malloc(100); 
	FILE *postFile;
	server_logging_func(0,buffer); 
	postFile=fopen("postFile.txt", "a+");
	if(postFile == NULL){
	perror("Error opening postFile: \n");}
	fputs(buffer, postFile);
	fclose(postFile);
	postFile=fopen("postFile.txt", "r");
	if(postFile == NULL){
	perror("Error opening postFile for reading: \n");}
	while((read = getline(&line, &len, postFile)) != -1 ){
	if (strstr(line,"fname")!=NULL) strcpy(values,line);
	}
	
	key=(char *)malloc(50);
	strcpy(key,values);
	name=strtok(values,"&");
	key=strtok(key,"&");
	key=strtok(NULL," "); key=strtok(key,"="); key=strtok(NULL," "); salary=atoi(key);
	name=strtok(name,"="); name=strtok(NULL," ");
	remove("postFile.txt");
}


int main (int argc, char *argv[] ){
//If directory doesnt exit, creat it, else use existing one
struct stat st;
if(stat("../log/", &st) == -1){
fprintf(stderr, "Directory log doesn't exist, Creating ....\n");
mkdir("../log/", 0700);
}

//execution processs
void serverConfig_func();
void server_logging_func(int i, char *msg);
int requestMethod_func(char *request);
int urlCheck_func(char *request);
char *  contentGet_func(char *type);
char * mimeCheck_func(char *request);
char * file_getting_func(char *request);
void write_Head_func(char *type, int len);
char * fileWrite_func(char *file);
int fileSize_func(char *file);
char * status_func();
void GET_http();
void POST_http(char *buffer);


	int method;
	url=(char *)malloc(BUFSIZE);
	strcpy(url,argv[0]);
	method=requestMethod_func(url);
	if(method==1) 	GET_http();
	else if(method==2) POST_http(argv[0]);
	else printf("%s","Error 501 Not Implemented");
	free(url);
	exit(0);
}

