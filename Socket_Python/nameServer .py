from socket import *
from time import sleep,ctime

HOST = ''
PORT = 8088
ADDR = (HOST,PORT)
BUFSIZ = 1024 

def main():
    tcpSerSock = socket(AF_INET,SOCK_STREAM) #�����׽���
    tcpSerSock.bind(ADDR) 
    tcpSerSock.listen(5) 

    print 'Waiting for connect...'
    tcpCliSock,addr = tcpSerSock.accept() #Ŀ��ͻ����׽���
    print 'connet from:',addr

    tcpCliSock.send('10.125.115.107')
    tcpCliSock.send('9999') 
           
    tcpCliSock.close()
    tcpSerSock.close()

if __name__=='__main__': 
    main()


