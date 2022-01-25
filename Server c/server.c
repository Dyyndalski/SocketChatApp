#include<stdio.h>
#include<stdlib.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<string.h>
#include <arpa/inet.h>
#include <fcntl.h> // for open
#include <unistd.h> // for close
#include<pthread.h>
#include<stdbool.h>
bool flag = false;
char a[10][20];
int b[10];
char message[2000];
char help_message[2000];
char usermsg[50];



char buffer[1024];
pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;

int iindex(){
    for(int i = 0; sizeof(a); i++){
        if(b[i] == 0){
            return i;
        }
    }
    return -1;
}

void * socketThread(void *arg)
{
    //nowy watek
    printf("new thread \n");
    char nick[50];
    int newSocket = *((int *)arg);
    int n;

    //klient podaje swoj nick
    recv(newSocket , nick , sizeof(nick) , 0);

    int indx = iindex();
    //dane klienta do tablic
    strcpy(a[indx], nick);
    b[indx] = newSocket;

//wysyalnie do nowego uzytkownika wszystkich aktywnych
    memset(&usermsg, 0, sizeof (usermsg));
    for(int i = 0; i < iindex(); i++){
        if(b[i] != newSocket){
            printf("%s, %d\n", a[i], b[i]);
            strcpy(usermsg, "user:");
            strcat(usermsg, a[i]);
            strcat(usermsg, "\n");
            printf("%s\n", usermsg);
            sleep(1);
            send(newSocket,usermsg,sizeof(usermsg),0);
            memset(&usermsg, 0, sizeof (usermsg));
        }
    }
//wysylanie do wszystkich nowego dostepnego uzytkownika
    memset(&usermsg, 0, sizeof(usermsg));
    for(int i = 0; i < iindex(); i++){
        if(b[i] != newSocket){
            printf("%s, %d\n", a[i], b[i]);
            strcpy(usermsg, "user:");
            strcat(usermsg, nick);
            strcat(usermsg, "\n");
            printf("%s\n", usermsg);
            sleep(1);
            send(b[i],usermsg,sizeof(usermsg),0);
            memset(&usermsg, 0, sizeof (usermsg));
        }
    }
    memset(&usermsg, 0, sizeof(usermsg));

for(;;){
    n=recv(newSocket , message , 2000 , 0);

    if(n<1){ //kiedy sie rozlaczy
            break;
        }

    sleep(1);
    memset(&help_message, 0, sizeof (help_message));
    strcpy(help_message, "from:");
    strcat(help_message, nick);
    strcat(help_message, message);

    for(int i = 0; i < iindex(); i++){
      send(b[i],help_message,sizeof(help_message),0);
    }

    memset(&message, 0, sizeof (message));
    memset(&help_message, 0, sizeof (message));
}
  //gdy jakis uzytkownik sie rozlaczy; wysylanie do wszystkich info ze sie rozlaczyl
    memset(&message, 0, sizeof (message));
    for(int i = 0; i < iindex(); i++){
      if(b[i] != newSocket){
        strcpy(message, "userout:");
        strcat(message, nick);
        //strcat(message, "/n");
        //printf("%s", message);
        send(b[i],message,sizeof(message),0);
        memset(&message, 0, sizeof (message));
      }
    }
    memset(&message, 0, sizeof (message));

  //usuwanie go z tablicy uzytkownikow
    for(int z = 0; z < iindex(); z++){
      if(b[z]==newSocket){
        b[z] = 0;
        strcpy(a[z], "");
      }
    }
    printf("Exit socketThread \n");

    pthread_exit(NULL);
}


int main(){
  int serverSocket, newSocket;
  struct sockaddr_in serverAddr;
  struct sockaddr_storage serverStorage;
  socklen_t addr_size;

  //Create the socket.
  serverSocket = socket(PF_INET, SOCK_STREAM, 0);

  // Configure settings of the server address struct
  // Address family = Internet
  serverAddr.sin_family = AF_INET;

  //Set port number, using htons function to use proper byte order
  serverAddr.sin_port = htons(8888);

  //Set IP address to localhost
  serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);


  //Set all bits of the padding field to 0
  memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);

  //Bind the address struct to the socket
  bind(serverSocket, (struct sockaddr *) &serverAddr, sizeof(serverAddr));


  //Listen on the socket
  if(listen(serverSocket,50)==0)
    printf("Listening\n");
  else
    printf("Error\n");
    pthread_t thread_id;


    while(1)
    {
        //printf("Tutej");
        //Accept call creates a new socket for the incoming connection
        addr_size = sizeof serverStorage;
        newSocket = accept(serverSocket, (struct sockaddr *) &serverStorage, &addr_size);

        if( pthread_create(&thread_id, NULL, socketThread, &newSocket) != 0 )
           printf("Failed to create thread\n");



        pthread_detach(thread_id);
        pthread_join(thread_id,NULL);
    }
  return 0;
}
